/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
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

package org.wso2.carbon.apimgt.rest.api.publisher.v1.impl;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIMgtResourceNotFoundException;
import org.wso2.carbon.apimgt.api.APIProvider;
import org.wso2.carbon.apimgt.api.ExceptionCodes;
import org.wso2.carbon.apimgt.api.model.OperationPolicyDataHolder;
import org.wso2.carbon.apimgt.api.model.OperationPolicySpecification;
import org.wso2.carbon.apimgt.impl.importexport.utils.CommonUtil;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.rest.api.common.RestApiCommonUtil;
import org.wso2.carbon.apimgt.rest.api.common.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.OperationPoliciesApiService;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.common.mappings.OperationPolicyMappingUtil;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.utils.RestApiPublisherUtils;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;

import javax.ws.rs.core.Response;

public class OperationPoliciesApiServiceImpl implements OperationPoliciesApiService {

    private static final Log log = LogFactory.getLog(OperationPoliciesApiServiceImpl.class);

    /**
     * Add a common operation policy that can be used by all the APIs
     *
     * @param policySpecFileInputStream       Input stream of the common policy specification file
     * @param policySpecFileDetail            Common policy specification
     * @param policyDefinitionFileInputStream Input stream of the common policy definition file
     * @param policyDefinitionFileDetail      Definition of the common policy
     * @param messageContext                  message context
     * @return Added common operation policy DTO as response
     */
    @Override
    public Response addCommonOperationPolicy(InputStream policySpecFileInputStream, Attachment policySpecFileDetail,
                                             InputStream policyDefinitionFileInputStream,
                                             Attachment policyDefinitionFileDetail, MessageContext messageContext) {

        try {
            String policySpec = "";
            String jsonContent = "";
            String policyDefinition = "";
            OperationPolicySpecification policySpecification;
            APIProvider apiProvider = RestApiCommonUtil.getLoggedInUserProvider();
            String organization = RestApiUtil.getValidatedOrganization(messageContext);

            if (policySpecFileInputStream != null) {
                policySpec = RestApiPublisherUtils.readInputStream(policySpecFileInputStream, policySpecFileDetail);

                String fileName = policySpecFileDetail.getDataHandler().getName();
                String fileContentType = URLConnection.guessContentTypeFromName(fileName);
                if (org.apache.commons.lang3.StringUtils.isBlank(fileContentType)) {
                    fileContentType = policySpecFileDetail.getContentType().toString();
                }
                log.info(fileName + fileContentType);

                jsonContent = CommonUtil.yamlToJson(policySpec);
                Schema schema = APIUtil.retrieveOperationPolicySpecificationJsonSchema();
                if (schema != null) {
                    try {
                        org.json.JSONObject uploadedConfig = new org.json.JSONObject(jsonContent);
                        schema.validate(uploadedConfig);
                    } catch (ValidationException e) {
                        List<String> errors = e.getAllMessages();
                        String errorMessage =
                                errors.size() + " validation error(s) found. Error(s) :" + errors.toString();
                        throw new APIManagementException("Policy specification validation failure. " + errorMessage,
                                ExceptionCodes.from(ExceptionCodes.INVALID_OPERATION_POLICY_SPECIFICATION,
                                        errorMessage));
                    }
                }

                policySpecification = new Gson().fromJson(jsonContent, OperationPolicySpecification.class);

                if (policyDefinitionFileInputStream != null) {
                    policyDefinition =
                            RestApiPublisherUtils.readInputStream(policyDefinitionFileInputStream,
                                    policyDefinitionFileDetail);
                }

                OperationPolicyDataHolder operationPolicyData = new OperationPolicyDataHolder();
                operationPolicyData.setOrganization(organization);
                operationPolicyData.setMd5Hash(APIUtil.getMd5OfOperationPolicy(policySpecification, policyDefinition));
                operationPolicyData.setSpecification(policySpecification);
                operationPolicyData.setDefinition(policyDefinition);

                OperationPolicyDataHolder existingPolicy =
                        apiProvider.getCommonOperationPolicyByPolicyName(policySpecification.getName(), organization,
                                false);
                String policyID;
                if (existingPolicy != null) {
                    policyID = existingPolicy.getPolicyId();
                    apiProvider.updateOperationPolicy(policyID, operationPolicyData, organization);
                    if (log.isDebugEnabled()) {
                        log.debug("Existing common operation policy with name " + policySpecification.getName()
                                + " has been updated");
                    }
                } else {
                    policyID = apiProvider.addCommonOperationPolicy(operationPolicyData, organization);
                    if (log.isDebugEnabled()) {
                        log.debug(
                                "A common operation policy has been added with name " + policySpecification.getName());
                    }
                }
                operationPolicyData.setPolicyId(policyID);
                OperationPolicyDataDTO createdPolicy = OperationPolicyMappingUtil
                        .fromOperationPolicyDataToDTO(operationPolicyData);
                URI createdPolicyUri = new URI(RestApiConstants.REST_API_PUBLISHER_VERSION + "/"
                        + RestApiConstants.RESOURCE_PATH_OPERATION_POLICIES + "/" + policyID);
                return Response.created(createdPolicyUri).entity(createdPolicy).build();
            }
        } catch (APIManagementException e) {
            String errorMessage = "Error while adding a common operation policy." + e.getMessage();
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        } catch (Exception e) {
            RestApiUtil.handleInternalServerError("An Error has occurred while adding common operation policy",
                    e, log);
        }
        return null;
    }

    /**
     * Delete a common operation policy by providing the policy ID
     *
     * @param operationPolicyId UUID of the operation policy
     * @param messageContext    message context
     * @return ok if deleted successfully
     */
    @Override
    public Response deleteCommonOperationPolicyByPolicyId(String operationPolicyId, MessageContext messageContext) {

        try {
            APIProvider apiProvider = RestApiCommonUtil.getLoggedInUserProvider();
            String organization = RestApiUtil.getValidatedOrganization(messageContext);

            OperationPolicyDataHolder existingPolicy =
                    apiProvider.getCommonOperationPolicyByPolicyId(operationPolicyId, organization, false);
            if (existingPolicy != null) {
                apiProvider.deleteOperationPolicyById(operationPolicyId, organization);
                if (log.isDebugEnabled()) {
                    log.debug("The common operation policy " + operationPolicyId + " has been deleted");
                }
                return Response.ok().build();
            } else {
                throw new APIMgtResourceNotFoundException("Couldn't retrieve an existing common policy with ID: "
                        + operationPolicyId, ExceptionCodes.from(ExceptionCodes.OPERATION_POLICY_NOT_FOUND,
                        operationPolicyId));
            }
        } catch (APIManagementException e) {
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_PATH_OPERATION_POLICIES,
                        operationPolicyId, e, log);
            } else {
                String errorMessage = "Error while deleting the common operation policy with ID: " + operationPolicyId
                        + " " + e.getMessage();
                RestApiUtil.handleInternalServerError(errorMessage, e, log);
            }
        } catch (Exception e) {
            RestApiUtil.handleInternalServerError("An Error has occurred while deleting the common operation policy + "
                    + operationPolicyId, e, log);
        }
        return null;
    }

    /**
     * Get the list of all common operation policies for a given organization
     *
     * @param limit          max number of records returned
     * @param offset         starting index
     * @param messageContext message context
     * @return A list of operation policies available for the API
     */
    @Override
    public Response getAllCommonOperationPolicies(Integer limit, Integer offset, String query,
                                                  MessageContext messageContext) throws APIManagementException {

        try {
            limit = limit != null ? limit : RestApiConstants.PAGINATION_LIMIT_DEFAULT;
            offset = offset != null ? offset : RestApiConstants.PAGINATION_OFFSET_DEFAULT;

            APIProvider apiProvider = RestApiCommonUtil.getLoggedInUserProvider();
            String organization = RestApiUtil.getValidatedOrganization(messageContext);

            // Since policy definition is bit bulky, we don't query the definition unnecessarily.
            List<OperationPolicyDataHolder> commonOperationPolicyLIst =
                    apiProvider.getAllCommonOperationPolicies(organization);
            OperationPolicyDataListDTO policyListDTO = OperationPolicyMappingUtil
                    .fromOperationPolicyDataListToDTO(commonOperationPolicyLIst, offset, limit);
            return Response.ok().entity(policyListDTO).build();
        } catch (APIManagementException e) {
            String errorMessage = "Error while retrieving the list of all common operation policies." + e.getMessage();
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        } catch (Exception e) {
            RestApiUtil.handleInternalServerError("An error has occurred while getting the list of all common " +
                    " operation policies", e, log);
        }
        return null;
    }

    /**
     * Get the common operation policy by providing the policy ID
     *
     * @param operationPolicyId UUID of the operation policy
     * @param messageContext    message context
     * @return Operation policy DTO as response
     */
    @Override
    public Response getCommonOperationPolicyByPolicyId(String operationPolicyId, MessageContext messageContext) {

        try {
            APIProvider apiProvider = RestApiCommonUtil.getLoggedInUserProvider();
            String organization = RestApiUtil.getValidatedOrganization(messageContext);

            OperationPolicyDataHolder existingPolicy =
                    apiProvider.getCommonOperationPolicyByPolicyId(operationPolicyId, organization, false);
            if (existingPolicy != null) {
                OperationPolicyDataDTO policyDataDTO =
                        OperationPolicyMappingUtil.fromOperationPolicyDataToDTO(existingPolicy);
                return Response.ok().entity(policyDataDTO).build();
            } else {
                throw new APIMgtResourceNotFoundException("Couldn't retrieve an existing common policy with ID: "
                        + operationPolicyId, ExceptionCodes.from(ExceptionCodes.OPERATION_POLICY_NOT_FOUND,
                        operationPolicyId));
            }

        } catch (APIManagementException e) {
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_PATH_OPERATION_POLICIES,
                        operationPolicyId, e, log);
            } else {
                String errorMessage = "Error while getting the common operation policy with ID :" + operationPolicyId
                        + " " + e.getMessage();
                RestApiUtil.handleInternalServerError(errorMessage, e, log);
            }
        } catch (Exception e) {
            RestApiUtil.handleInternalServerError("An error has occurred while getting the common operation " +
                    " policy with ID: " + operationPolicyId, e, log);
        }
        return null;
    }

    /**
     * Download the operation policy specification and definition for a given common operation policy
     *
     * @param operationPolicyId UUID of the operation policy
     * @param messageContext    message context
     * @return A zip file containing both (if exists) operation policy specification and policy definition
     */
    @Override
    public Response getCommonOperationPolicyContentByPolicyId(String operationPolicyId, MessageContext messageContext) {

        try {
            APIProvider apiProvider = RestApiCommonUtil.getLoggedInUserProvider();
            String organization = RestApiUtil.getValidatedOrganization(messageContext);

            OperationPolicyDataHolder policyData =
                    apiProvider.getCommonOperationPolicyByPolicyId(operationPolicyId, organization, true);
            if (policyData != null) {
                File file = RestApiPublisherUtils.exportOperationPolicyData(policyData);
                return Response.ok(file).header(RestApiConstants.HEADER_CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"").build();
            } else {
                throw new APIMgtResourceNotFoundException("Couldn't retrieve an existing common policy with ID: "
                        + operationPolicyId, ExceptionCodes.from(ExceptionCodes.OPERATION_POLICY_NOT_FOUND,
                        operationPolicyId));
            }
        } catch (APIManagementException e) {
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_PATH_OPERATION_POLICIES,
                        operationPolicyId, e, log);
            } else {
                String errorMessage = "Error while getting the content of common operation policy with ID :"
                        + operationPolicyId + " " + e.getMessage();
                RestApiUtil.handleInternalServerError(errorMessage, e, log);
            }
        } catch (Exception e) {
            RestApiUtil.handleInternalServerError(
                    "An error has occurred while getting the content of the common operation " +
                            " policy with ID " + operationPolicyId, e, log);
        }
        return null;
    }
}
