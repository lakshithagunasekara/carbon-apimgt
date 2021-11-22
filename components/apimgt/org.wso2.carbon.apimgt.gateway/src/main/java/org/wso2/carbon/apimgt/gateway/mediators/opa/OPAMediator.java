package org.wso2.carbon.apimgt.gateway.mediators.opa;

import com.google.gson.Gson;
import org.apache.axis2.util.JavaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.common.utils.JSONUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.TreeMap;

public class OPAMediator extends AbstractMediator  {

    private static final Log log = LogFactory.getLog(OPAMediator.class);
    public static final String HTTP_METHOD_STRING = "HTTP_METHOD";
    public static final String API_BASEPATH_STRING = "TransportInURL";
    public static final int SERVER_RESPONSE_CODE_SUCCESS = 200;
    public static final int SERVER_RESPONSE_BAD_REQUEST = 400;
    public static final int SERVER_RESPONSE_SERVER_ERROR = 500;

    public void init(){
    }

    @Override
    public boolean mediate(MessageContext messageContext) {

        JSONObject opaPayload = getOPAInput(messageContext);
        String opaServerUrl = (String) messageContext.getProperty("OPA_SERVER_URL");
        String opaToken = (String) messageContext.getProperty("OPA_TOKEN");
        boolean opaResponse = false;
        try {
            opaResponse = publish(opaPayload, opaServerUrl, opaToken);
        } catch (APIManagementException e) {
            e.printStackTrace();
        }

        return opaResponse;
    }

    public boolean publish(JSONObject opaPayload, String url, String token) throws APIManagementException {

        String jsonContent = JSONUtils.buildJSON(opaPayload);
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("Authorization", token);
        postRequest.addHeader("Content-type", "application/json");
        CloseableHttpResponse response = null;
        boolean serverResponse = false;
        try {
            postRequest.setEntity(new StringEntity(jsonContent));

            if (log.isDebugEnabled()) {
                log.debug("Sending POST to " + url);
            }

            CloseableHttpClient httpClient = OPAUtils.getClient(url);
            if (httpClient != null) {
                long publishingStartTime = System.nanoTime();
                response = httpClient.execute(postRequest);
                long publishingEndTime = System.nanoTime();

                if (log.isDebugEnabled()) {
                    log.debug("Time taken to communicate with OPA server:" + (publishingEndTime - publishingStartTime));
                }

                if (response != null) {
                    int serverResponseCode = response.getStatusLine().getStatusCode();
                    switch (serverResponseCode) {
                        case SERVER_RESPONSE_BAD_REQUEST:
                            log.error("Incorrect JSON format sent for the server from the request ");
                            break;
                        case SERVER_RESPONSE_SERVER_ERROR:
                            if (log.isDebugEnabled()) {
                                log.debug("OPA Server error code sent for the request ");
                            }
                            break;
                        case SERVER_RESPONSE_CODE_SUCCESS:
                            HttpEntity entity = response.getEntity();
                            String responseString = EntityUtils.toString(entity, "UTF-8");
                            if (log.isDebugEnabled()) {
                                log.debug("OPA Server Response for for the request "
                                        + " was " + responseString);
                            }
                            if (responseString.equals("{}")) {
                                //The policy for this API has not been created at the OPA server. Request will be sent to
                                // backend without validation
                                if (log.isDebugEnabled()) {
                                    log.debug("OPA Policy was not defined for the API ");
                                }
                            } else {
                                JSONParser parser = new JSONParser();
                                try {
                                    JSONObject responseObject = (JSONObject) parser.parse(responseString);
                                    Object resultObject = responseObject.get("result");
                                    if (resultObject != null) {
                                        serverResponse = JavaUtils.isTrueExplicitly(resultObject);
                                    }
                                } catch (ParseException e) {
                                    log.error("Parsing exception for response");
                                }
                            }
                            break;
                    }

                    if (log.isDebugEnabled()) {
                        log.debug("OPA Server connection time for the request in nano seconds is "
                                + (publishingEndTime - publishingStartTime));
                    }
                } else {
                    log.error("Null response returned from OPA server for the request");
                }
            } else {
                log.error("Cannot find a valid http client");
                throw new APIManagementException("Internal server error.");
            }
        } catch (SocketTimeoutException e) {
            log.error("Connection timed out. Socket Timeout");
            throw new APIManagementException("Internal server error. Backend takes more time than expected", e);
        } catch (IOException e) {
            log.error("Error occurred while sending POST request to OPA endpoint.", e);
            throw new APIManagementException("Internal server error. Unable to send request to the backend endpoint" +
                    ".", e);
        } catch (OPAPolicyException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consumeQuietly(response.getEntity());
                    response.close();
                } catch (IOException e) {
                    log.error("Error occurred when closing the response of the post request", e);
                }
            }
        }
        return serverResponse;

    }

    public JSONObject getOPAInput(MessageContext messageContext) {

        JSONObject opaPayload = new JSONObject();

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                .getAxis2MessageContext();
        TreeMap<String, String> transportHeadersMap = (TreeMap<String, String>) axis2MessageContext
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        String requestOriginIP = OPAUtils.getIp(axis2MessageContext);
        String requestMethod = (String) axis2MessageContext.getProperty(HTTP_METHOD_STRING);
        String requestPath = (String) axis2MessageContext.getProperty(API_BASEPATH_STRING);
        String requestHttpVersion = OPAUtils.getHttpVersion(axis2MessageContext);

        AuthenticationContext authContext = (AuthenticationContext) messageContext.getProperty("__API_AUTH_CONTEXT");
        if (authContext != null) {
            String authContextSting = new Gson().toJson(authContext);
            JSONParser parser = new JSONParser();
            JSONObject authContextJson;
            try {
                authContextJson = (JSONObject) parser.parse(authContextSting);
                //User Info is added only if the request is Authenticated
                opaPayload.put("AuthContext", authContextJson);
            } catch (ParseException e) {
                log.error("Error occurred when parsing authContext String", e);
                opaPayload.put("AuthContext", authContextSting);
            }
        }

        opaPayload.put("IP", requestOriginIP);
        opaPayload.put("Method", requestMethod);
        opaPayload.put("Path", requestPath);
        opaPayload.put("http_version", requestHttpVersion);
        return opaPayload;

    }

}
