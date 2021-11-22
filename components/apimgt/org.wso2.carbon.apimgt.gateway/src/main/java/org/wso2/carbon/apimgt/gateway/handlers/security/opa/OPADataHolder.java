package org.wso2.carbon.apimgt.gateway.handlers.security.opa;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class OPADataHolder {

    private Map<String, String> opaServerConfigs = new ConcurrentHashMap<>();
    private Set<String> opaEnabledAPIs = ConcurrentHashMap.newKeySet();
    private boolean applyForAllApis = false;


    public void addAPItoOPAEnabledAPIsSet(String apiContext) {
        opaEnabledAPIs.add(apiContext);
    }

    public void populateOPAEnabledAPIsSet(Set<String> data) {
        if(data.size() > 0) {
            opaEnabledAPIs.addAll(data);
        }
    }

    public boolean isApplyForAllApis() {

        return applyForAllApis;
    }

    public void setApplyForAllApis(boolean applyForAllApis) {

        this.applyForAllApis = applyForAllApis;
    }

    public void removeAPIFromOPAEnabledAPIsSet(String apiContext) {
        opaEnabledAPIs.remove(apiContext);
    }

    public boolean isOPAEnabledForAPI(String apiContext){
        return isApplyForAllApis() || opaEnabledAPIs.contains(apiContext);

    }

    public Map<String, String> getOpaServerConfigs() {

        return opaServerConfigs;
    }

    public void setOpaServerConfigs(Map<String, String> opaServerConfigs) {

        this.opaServerConfigs = opaServerConfigs;
    }
}
