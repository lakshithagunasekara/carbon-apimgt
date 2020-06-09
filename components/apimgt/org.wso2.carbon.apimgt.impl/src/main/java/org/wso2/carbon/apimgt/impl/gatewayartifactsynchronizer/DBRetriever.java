package org.wso2.carbon.apimgt.impl.gatewayartifactsynchronizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.gateway.GatewayAPIDTO;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.gatewayartifactsynchronizer.exception.ArtifactSynchronizerException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DBRetriever implements ArtifactRetriever {

    private static final Log log = LogFactory.getLog(DBRetriever.class);
    protected ApiMgtDAO apiMgtDAO = ApiMgtDAO.getInstance();

    @Override
    public void init() throws ArtifactSynchronizerException {
        //not required
    }

    @Override
    public GatewayAPIDTO retrieveArtifact(String APIId, String gatewayLabel, String gatewayInstruction)
            throws ArtifactSynchronizerException {

        GatewayAPIDTO gatewayAPIDTO = null;
        try {
            ByteArrayInputStream byteStream =
                    apiMgtDAO.getGatewayPublishedAPIArtifacts(APIId, gatewayLabel, gatewayInstruction);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            gatewayAPIDTO = (GatewayAPIDTO) objectStream.readObject();
            if (log.isDebugEnabled()) {
                log.debug("Successfully retrieved Artifact of " + gatewayAPIDTO.getName());
            }
        } catch (APIManagementException | IOException | ClassNotFoundException e) {
            throw new ArtifactSynchronizerException("Error retrieving Artifact belongs to  " + APIId + " from DB", e);
        }
        return gatewayAPIDTO;
    }

    @Override
    public void disconnect() {
        //not required
    }

    @Override
    public String getName() {

        return APIConstants.GatewayArtifactSynchronizer.DEFAULT_RETRIEVER_NAME;
    }
}
