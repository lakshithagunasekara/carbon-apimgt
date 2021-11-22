package org.wso2.carbon.apimgt.gateway.handlers.security.opa;

import org.apache.synapse.MessageContext;

public class OPAValidator {

    public boolean authenticate (MessageContext messageContext){
        boolean isAuthenticated = false;

        String apiContext = (String) messageContext.getProperty("Context");




        return isAuthenticated;
    }

}
