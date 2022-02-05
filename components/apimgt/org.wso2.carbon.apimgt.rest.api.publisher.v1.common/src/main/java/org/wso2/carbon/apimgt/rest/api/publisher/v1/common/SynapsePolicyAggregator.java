/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.apimgt.rest.api.publisher.v1.common;

import com.hubspot.jinjava.Jinjava;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.OperationPolicy;
import org.wso2.carbon.apimgt.api.model.URITemplate;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.OperationPolicyComparator;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.common.mappings.ImportUtils;
import org.wso2.carbon.utils.CarbonUtils;
import org.wso2.carbon.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class used to generate Synapse Artifact.
 */
public class SynapsePolicyAggregator {

    private static final Log log = LogFactory.getLog(SynapsePolicyAggregator.class);
    private static final String POLICY_SEQUENCE_TEMPLATE_LOCATION = CarbonUtils.getCarbonHome() + File.separator
            + "repository" + File.separator + "resources" + File.separator + "api_templates" + File.separator
            + "operation_policy_template.j2";

    public static String generatePolicySequenceForUriTemplateSet(Set<URITemplate> uriTemplates, String sequenceName,
                                                                 String flow, String pathToAchieve)
            throws APIManagementException, IOException {

        List<Object> caseList = new ArrayList<>();
        for (URITemplate template : uriTemplates) {
            populatePolicyCaseList(template, pathToAchieve, flow, caseList);
        }

        if (caseList.size() != 0) {
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("sequence_name", sequenceName);
            configMap.put("case_list", caseList);
            String operationPolicyTemplate = FileUtil.readFileToString(POLICY_SEQUENCE_TEMPLATE_LOCATION)
                    .replace("\\", ""); //Removing escape characters from the template
            return renderPolicyTemplate(operationPolicyTemplate, configMap);
        } else {
            return "";
        }
    }

    public static List<Object> populatePolicyCaseList(URITemplate template, String pathToAchieve, String flow,
                                                      List<Object> caseList) throws APIManagementException {

        Map<String, Object> caseMap = new HashMap<>();
        String uriTemplateString = template.getUriTemplate();
        String method = template.getHTTPVerb();
        String key = method + "_" + uriTemplateString.replaceAll("[\\W]", "\\\\$0");

        // This will replace & with &amp; for query params
        key = StringEscapeUtils.escapeXml(StringEscapeUtils.unescapeXml(key));

        List<String> caseBody = new ArrayList<>();

        List<OperationPolicy> operationPolicies = template.getOperationPolicies();
        Collections.sort(operationPolicies, new OperationPolicyComparator());
        for (OperationPolicy policy : operationPolicies) {
            if (flow.equals(policy.getDirection())) {
                Map<String, Object> policyParameters = policy.getParameters();
                String policyDefinition = ImportUtils.getOperationPolicyDefinitionFromFile(pathToAchieve,
                        policy.getPolicyName());
                if (policyDefinition != null) {
                    String renderedTemplate = renderPolicyTemplate(policyDefinition, policyParameters);
                    if (renderedTemplate != null && !renderedTemplate.isEmpty()) {
                        caseBody.add(renderedTemplate);
                    }
                } else {
                    log.error("Policy definition for " + policy.getPolicyName() + " is not found in the artifact");
                }
            }
        }

        if (caseBody.size() != 0) {
            caseMap.put("case_regex", key);
            caseMap.put("policy_sequence", caseBody);
            caseList.add(caseMap);
        }

        return caseList;
    }

    public static String renderPolicyTemplate(String template, Map<String, Object> configMap) {

        Jinjava jinjava = new Jinjava();
        return jinjava.render(template, configMap);
    }

    public static String getSequenceExtensionFlow(String flow) {

        String sequenceExtension = null;
        switch (flow) {
            case APIConstants.OPERATION_SEQUENCE_TYPE_REQUEST:
                sequenceExtension = APIConstants.API_CUSTOM_SEQ_IN_EXT;
                break;
            case APIConstants.OPERATION_SEQUENCE_TYPE_RESPONSE:
                sequenceExtension = APIConstants.API_CUSTOM_SEQ_OUT_EXT;
                break;
            case APIConstants.OPERATION_SEQUENCE_TYPE_FAULT:
                sequenceExtension = APIConstants.API_CUSTOM_SEQ_FAULT_EXT;
                break;
        }
        return sequenceExtension;
    }

}
