package org.wso2.carbon.apimgt.rest.api.publisher.v1;

import org.wso2.carbon.apimgt.rest.api.publisher.v1.*;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.*;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import org.wso2.carbon.apimgt.api.APIManagementException;

import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ErrorDTO;
import java.io.File;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataListDTO;

import java.util.List;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


public interface ApiPoliciesApiService {
      public Response addCommonPolicy(InputStream policyDefinitionFileInputStream, Attachment policyDefinitionFileDetail, InputStream synapsePolicyTemplateFileInputStream, Attachment synapsePolicyTemplateFileDetail, InputStream ccPolicyTemplateFileInputStream, Attachment ccPolicyTemplateFileDetail, MessageContext messageContext) throws APIManagementException;
      public Response deleteCommonPolicyByPolicyId(String policyId, MessageContext messageContext) throws APIManagementException;
      public Response exportAPIPolicy(String name, String version, String format, MessageContext messageContext) throws APIManagementException;
      public Response getAllCommonPolicies(Integer limit, Integer offset, String query, MessageContext messageContext) throws APIManagementException;
      public Response getCommonPolicyByPolicyId(String policyId, MessageContext messageContext) throws APIManagementException;
      public Response getCommonPolicyContentByPolicyId(String policyId, MessageContext messageContext) throws APIManagementException;
      public Response importAPIPolicy(InputStream fileInputStream, Attachment fileDetail, MessageContext messageContext) throws APIManagementException;
}
