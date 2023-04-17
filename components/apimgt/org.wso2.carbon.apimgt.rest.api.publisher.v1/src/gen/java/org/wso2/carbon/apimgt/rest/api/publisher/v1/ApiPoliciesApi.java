package org.wso2.carbon.apimgt.rest.api.publisher.v1;

import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.ErrorDTO;
import java.io.File;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicyDataListDTO;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.ApiPoliciesApiService;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.impl.ApiPoliciesApiServiceImpl;
import org.wso2.carbon.apimgt.api.APIManagementException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.inject.Inject;

import io.swagger.annotations.*;
import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
@Path("/api-policies")

@Api(description = "the api-policies API")




public class ApiPoliciesApi  {

  @Context MessageContext securityContext;

ApiPoliciesApiService delegate = new ApiPoliciesApiServiceImpl();


    @POST
    
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a new common policy", notes = "This operation can be used to add a new common operation policy. ", response = OperationPolicyDataDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:common_operation_policy_manage", description = "Add, Update and Delete common operation policies")
        })
    }, tags={ "API Policies",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "OK. Common policy uploaded ", response = OperationPolicyDataDTO.class),
        @ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error.", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response addCommonPolicy( @Multipart(value = "policyDefinitionFile", required = false) InputStream policyDefinitionFileInputStream, @Multipart(value = "policyDefinitionFile" , required = false) Attachment policyDefinitionFileDetail,  @Multipart(value = "synapsePolicyTemplateFile", required = false) InputStream synapsePolicyTemplateFileInputStream, @Multipart(value = "synapsePolicyTemplateFile" , required = false) Attachment synapsePolicyTemplateFileDetail,  @Multipart(value = "ccPolicyTemplateFile", required = false) InputStream ccPolicyTemplateFileInputStream, @Multipart(value = "ccPolicyTemplateFile" , required = false) Attachment ccPolicyTemplateFileDetail) throws APIManagementException{
        return delegate.addCommonPolicy(policyDefinitionFileInputStream, policyDefinitionFileDetail, synapsePolicyTemplateFileInputStream, synapsePolicyTemplateFileDetail, ccPolicyTemplateFileInputStream, ccPolicyTemplateFileDetail, securityContext);
    }

    @DELETE
    @Path("/{policyId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Delete a common policy", notes = "This operation can be used to delete an existing common opreation policy by providing the Id of the policy. ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:common_operation_policy_manage", description = "Add, Update and Delete common operation policies"),
            @AuthorizationScope(scope = "apim:policies_import_export", description = "Export and import policies related operations")
        })
    }, tags={ "API Policies",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Resource successfully deleted. ", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden. The request must be conditional but no condition has been specified.", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response deleteCommonPolicyByPolicyId(@ApiParam(value = "API policy Id ",required=true) @PathParam("policyId") String policyId) throws APIManagementException{
        return delegate.deleteCommonPolicyByPolicyId(policyId, securityContext);
    }

    @GET
    @Path("/export")
    
    @Produces({ "application/zip", "application/json" })
    @ApiOperation(value = "Export an API Policy by its name and version ", notes = "This operation provides you to export a preferred common API policy ", response = File.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:policies_import_export", description = "Export and import policies related operations")
        })
    }, tags={ "Import Export",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Export Successful. ", response = File.class),
        @ApiResponse(code = 403, message = "Forbidden. The request must be conditional but no condition has been specified.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class) })
    public Response exportAPIPolicy( @ApiParam(value = "Policy name")  @QueryParam("name") String name,  @ApiParam(value = "Version of the policy")  @QueryParam("version") String version,  @ApiParam(value = "Format of the policy definition file")  @QueryParam("format") String format) throws APIManagementException{
        return delegate.exportAPIPolicy(name, version, format, securityContext);
    }

    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get all common policies to all the APIs ", notes = "This operation provides you a list of all common policies that can be used by any API ", response = OperationPolicyDataListDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:common_operation_policy_view", description = "View common operation policies"),
            @AuthorizationScope(scope = "apim:common_operation_policy_manage", description = "Add, Update and Delete common operation policies"),
            @AuthorizationScope(scope = "apim:policies_import_export", description = "Export and import policies related operations")
        })
    }, tags={ "API Policies",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. List of qualifying policies is returned. ", response = OperationPolicyDataListDTO.class),
        @ApiResponse(code = 406, message = "Not Acceptable. The requested media type is not supported.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response getAllCommonPolicies( @ApiParam(value = "Maximum size of policy array to return. ")  @QueryParam("limit") Integer limit,  @ApiParam(value = "Starting point within the complete list of items qualified. ", defaultValue="0") @DefaultValue("0") @QueryParam("offset") Integer offset,  @ApiParam(value = "**Search condition**.  You can search in attributes by using an **\"<attribute>:\"** modifier.  Eg. \"name:addHeader\" will match an API Policy if the provider of the API Policy contains \"addHeader\". \"version:\"v1\"\" will match an API Policy if the provider of the API Policy contains \"v1\".  Also you can use combined modifiers Eg. name:addHeader&version:v1 will match an API Policy if the name of the API Policy is addHeader and version is v1.  Supported attribute modifiers are [**version, name**]  If query attributes are provided, this returns all API policies available under the given limit.  Please note that you need to use encoded URL (URL encoding) if you are using a client which does not support URL encoding (such as curl) ")  @QueryParam("query") String query) throws APIManagementException{
        return delegate.getAllCommonPolicies(limit, offset, query, securityContext);
    }

    @GET
    @Path("/{policyId}")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Get the details of a common policy by providing policy ID", notes = "This operation can be used to retrieve a particular common policy. ", response = OperationPolicyDataDTO.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:common_operation_policy_view", description = "View common operation policies"),
            @AuthorizationScope(scope = "apim:common_operation_policy_manage", description = "Add, Update and Delete common operation policies"),
            @AuthorizationScope(scope = "apim:policies_import_export", description = "Export and import policies related operations")
        })
    }, tags={ "API Policies",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Operation policy returned. ", response = OperationPolicyDataDTO.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class),
        @ApiResponse(code = 406, message = "Not Acceptable. The requested media type is not supported.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response getCommonPolicyByPolicyId(@ApiParam(value = "API policy Id ",required=true) @PathParam("policyId") String policyId) throws APIManagementException{
        return delegate.getCommonPolicyByPolicyId(policyId, securityContext);
    }

    @GET
    @Path("/{policyId}/content")
    
    @Produces({ "application/zip", "application/json" })
    @ApiOperation(value = "Download a common policy", notes = "This operation can be used to download a selected common policy. ", response = File.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:common_operation_policy_view", description = "View common operation policies"),
            @AuthorizationScope(scope = "apim:common_operation_policy_manage", description = "Add, Update and Delete common operation policies")
        })
    }, tags={ "API Policies",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "OK. Operation policy returned. ", response = File.class),
        @ApiResponse(code = 404, message = "Not Found. The specified resource does not exist.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response getCommonPolicyContentByPolicyId(@ApiParam(value = "API policy Id ",required=true) @PathParam("policyId") String policyId) throws APIManagementException{
        return delegate.getCommonPolicyContentByPolicyId(policyId, securityContext);
    }

    @POST
    @Path("/import")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Import an API Policy", notes = "This operation can be used to import an API Policy. ", response = Void.class, authorizations = {
        @Authorization(value = "OAuth2Security", scopes = {
            @AuthorizationScope(scope = "apim:policies_import_export", description = "Export and import policies related operations")
        })
    }, tags={ "Import Export" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Created. Policy Imported Successfully. ", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden. The request must be conditional but no condition has been specified.", response = ErrorDTO.class),
        @ApiResponse(code = 409, message = "Conflict. Specified resource already exists.", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = ErrorDTO.class) })
    public Response importAPIPolicy( @Multipart(value = "file") InputStream fileInputStream, @Multipart(value = "file" ) Attachment fileDetail) throws APIManagementException{
        return delegate.importAPIPolicy(fileInputStream, fileDetail, securityContext);
    }
}
