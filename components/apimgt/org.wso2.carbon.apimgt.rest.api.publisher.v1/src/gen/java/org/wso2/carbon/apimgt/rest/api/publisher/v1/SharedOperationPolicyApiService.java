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


public interface SharedOperationPolicyApiService {
      public Response addSharedOperationPolicy(InputStream sharedPolicySpecFileInputStream, Attachment sharedPolicySpecFileDetail, InputStream sharedPolicyDefinitionFileInputStream, Attachment sharedPolicyDefinitionFileDetail, MessageContext messageContext) throws APIManagementException;
      public Response deleteSharedOperationPolicyByPolicyId(String operationPolicyId, MessageContext messageContext) throws APIManagementException;
      public Response getAllSharedOperationPolicies(Integer limit, Integer offset, String query, MessageContext messageContext) throws APIManagementException;
      public Response getSharedOperationPolicyByPolicyId(String operationPolicyId, MessageContext messageContext) throws APIManagementException;
      public Response getSharedOperationPolicyContentByPolicyId(String operationPolicyId, MessageContext messageContext) throws APIManagementException;
}
