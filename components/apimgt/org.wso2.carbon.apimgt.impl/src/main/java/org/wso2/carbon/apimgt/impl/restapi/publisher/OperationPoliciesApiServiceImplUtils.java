/*
 * Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.impl.restapi.publisher;

import org.wso2.carbon.apimgt.api.model.APIPolicyData;
import org.wso2.carbon.apimgt.api.model.APIPolicyTemplate;
import org.wso2.carbon.apimgt.api.model.APIPolicySpecification;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.util.HashMap;
import java.util.Map;

public class OperationPoliciesApiServiceImplUtils {

    private OperationPoliciesApiServiceImplUtils() {
    }

    /**
     * @param policySpecification Operation policy spec
     * @param organization        Validated organization
     * @return OperationPolicyData object
     */
    public static APIPolicyData prepareOperationPolicyData(APIPolicySpecification policySpecification,
                                                           String organization) {
        APIPolicyData operationPolicyData = new APIPolicyData();
        operationPolicyData.setOrganization(organization);
        operationPolicyData.setSpecification(policySpecification);

        return operationPolicyData;
    }

    /**
     * @param policySpecification Operation policy spec
     * @param organization        Validated organization
     * @param apiId               API UUID
     * @return OperationPolicyData object
     */
    public static APIPolicyData prepareOperationPolicyData(APIPolicySpecification policySpecification,
                                                           String organization, String apiId) {
        APIPolicyData operationPolicyData = new APIPolicyData();
        operationPolicyData.setOrganization(organization);
        operationPolicyData.setApiUUID(apiId);
        operationPolicyData.setSpecification(policySpecification);

        return operationPolicyData;
    }

    /**
     * @param policyData       Operation policy data
     * @param policyDefinition Operation policy definition object
     * @param definition       Policy definition
     * @param gatewayType      Policy gateway type
     */
    public static void preparePolicyDefinition(
            APIPolicyData policyData, APIPolicyTemplate policyDefinition,
            String definition, APIPolicyTemplate.GatewayType gatewayType) {
        policyDefinition.setContent(definition);
        policyDefinition.setGatewayType(gatewayType);
        policyDefinition.setMd5Hash(APIUtil.getMd5OfOperationPolicyDefinition(policyDefinition));

        if (APIPolicyTemplate.GatewayType.Synapse.equals(gatewayType)) {
            policyData.setSynapsePolicyTemplate(policyDefinition);
        } else if (APIPolicyTemplate.GatewayType.ChoreoConnect.equals(gatewayType)) {
            policyData.setCcPolicyDefinition(policyDefinition);
        }

        policyData.setMd5Hash(APIUtil.getMd5OfOperationPolicy(policyData));
    }

    /**
     * Get query parameters from the string
     *
     * @param query Request query
     * @return Map of query params
     */
    public static Map<String, String> getQueryParams(String query) {
        Map<String, String> queryParamMap = new HashMap<>();
        String[] queryParams = query.split(" ");
        for (String param : queryParams) {
            String[] keyVal = param.split(":");
            if (keyVal.length == 2) {
                queryParamMap.put(keyVal[0], keyVal[1]);
            }
        }

        return queryParamMap;
    }

}
