package org.wso2.carbon.apimgt.gateway.mediators.opa;

import org.apache.axis2.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.synapse.transport.passthru.ServerWorker;
import org.apache.synapse.transport.passthru.SourceRequest;
import org.wso2.carbon.utils.CarbonUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;

public class OPAUtils {

    private static final Log log = LogFactory.getLog(OPAUtils.class);

    static final String HTTP_VERSION_CONNECTOR = ".";
    private static final String STRICT = "Strict";
    private static final String ALLOW_ALL = "AllowAll";
    private static final String HOST_NAME_VERIFIER = "httpclient.hostnameVerifier";
    private static int maxOpenConnections = 150;
    private static int maxPerRoute = 50;
    private static int socketTimeout = 30;
    private static CloseableHttpClient httpClient = null;
    private static CloseableHttpClient httpsClient = null;

    public static String getIp(org.apache.axis2.context.MessageContext axis2MessageContext) {

        //Set transport headers of the message
        TreeMap<String, String> transportHeaderMap = (TreeMap<String, String>) axis2MessageContext
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);
        // Assigning an Empty String so that when doing comparisons, .equals method can be used without explicitly
        // checking for nullity.
        String remoteIP = "";
        //Check whether headers map is null and x forwarded for header is present
        if (transportHeaderMap != null) {
            remoteIP = transportHeaderMap.get("X-Forwarded-For");
        }

        //Setting IP of the client by looking at x forded for header and  if it's empty get remote address
        if (remoteIP != null && !remoteIP.isEmpty()) {
            if (remoteIP.indexOf(",") > 0) {
                remoteIP = remoteIP.substring(0, remoteIP.indexOf(","));
            }
        } else {
            remoteIP = (String) axis2MessageContext.getProperty(org.apache.axis2.context.MessageContext.REMOTE_ADDR);
        }
        if (remoteIP.indexOf(":") > 0) {
            remoteIP = remoteIP.substring(0, remoteIP.indexOf(":"));
        }
        return remoteIP;
    }

    public static String getHttpVersion(org.apache.axis2.context.MessageContext axis2MessageContext) {

        ServerWorker worker = (ServerWorker) axis2MessageContext.getProperty(Constants.OUT_TRANSPORT_INFO);
        SourceRequest sourceRequest = worker.getSourceRequest();
        ProtocolVersion httpProtocolVersion = sourceRequest.getVersion();

        return httpProtocolVersion.getMajor() + HTTP_VERSION_CONNECTOR + httpProtocolVersion
                .getMinor();
    }

    /**
     * Return a CloseableHttpClient instance
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient createHttpClient() throws OPAPolicyException {
        RequestConfig params = RequestConfig.custom()
                .setSocketTimeout(socketTimeout * 1000)
                .build();

        return HttpClients.custom().setDefaultRequestConfig(params).build();

    }

    public static CloseableHttpClient createHttpsClient() throws OPAPolicyException {
        PoolingHttpClientConnectionManager pool = getPoolingHttpClientConnectionManager("https");
        pool.setMaxTotal(maxOpenConnections);
        pool.setDefaultMaxPerRoute(maxPerRoute);

        RequestConfig params = RequestConfig.custom()
                .setSocketTimeout(socketTimeout * 1000)
                .build();

        return HttpClients.custom().setConnectionManager(pool).setDefaultRequestConfig(params).build();

    }

    /**
     * Return a PoolingHttpClientConnectionManager instance
     *
     * @param protocol- service endpoint protocol. It can be http/https
     * @return PoolManager
     */
    private static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager(String protocol)
            throws OPAPolicyException {

        PoolingHttpClientConnectionManager poolManager;
        if ("https".equals(protocol)) {
            SSLConnectionSocketFactory sslsf = createSocketFactory();
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf).build();
            poolManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        } else {
            poolManager = new PoolingHttpClientConnectionManager();
        }
        return poolManager;
    }

    private static SSLConnectionSocketFactory createSocketFactory() throws OPAPolicyException {

        SSLContext sslContext;
        try {
            String keyStorePath =
                    CarbonUtils.getServerConfiguration().getFirstProperty("Security.TrustStore.Location");
            String keyStorePassword =
                    CarbonUtils.getServerConfiguration().getFirstProperty("Security.TrustStore.Password");
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
            sslContext = SSLContexts.custom().loadTrustMaterial(trustStore).build();
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.error("Error occurred during the certificate validation of the endpoint", e);
            throw new OPAPolicyException("Internal server error.", e);
        } catch (CertificateException e) {
            log.error("Error occurred while loading the trust store", e);
            throw new OPAPolicyException("Internal server error.", e);
        }

        X509HostnameVerifier hostnameVerifier;
        String hostnameVerifierOption = System.getProperty(HOST_NAME_VERIFIER);

        if (ALLOW_ALL.equalsIgnoreCase(hostnameVerifierOption)) {
            hostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        } else if (STRICT.equalsIgnoreCase(hostnameVerifierOption)) {
            hostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
        } else {
            hostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        }

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        return sslsf;
    }

    public static CloseableHttpClient getClient (String url) throws OPAPolicyException {
        String protocol;
        try {
            protocol = new URL(url).getProtocol();
            if ("https".equals(protocol)) {
                if (httpsClient == null) {
                    httpsClient = createHttpsClient();
                }
                return httpsClient;
            } else {
                if (httpClient == null) {
                    httpClient = createHttpClient();
                }
                return httpClient;
            }
        } catch (MalformedURLException e) {
            log.error("Error when getting the endpoint protocol", e);
            throw new OPAPolicyException("Internal server error.", e);
        }
    }
}
