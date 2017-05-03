package org.wso2.carbon.apimgt.rest.api.admin;

import io.swagger.annotations.ApiParam;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.apimgt.rest.api.admin.dto.TierDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.TierListDTO;
import org.wso2.carbon.apimgt.rest.api.admin.dto.TierPermissionDTO;
import org.wso2.carbon.apimgt.rest.api.admin.factories.PoliciesApiServiceFactory;
import org.wso2.msf4j.Microservice;
import org.wso2.msf4j.Request;

@Component(
    name = "org.wso2.carbon.apimgt.rest.api.admin.PoliciesApi",
    service = Microservice.class,
    immediate = true
)
@Path("/api/am/admin/v1.[\\d]+/policies")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the policies API")
@javax.annotation.Generated(value = "org.wso2.maven.plugins.JavaMSF4JServerCodegen", date = "2017-04-07T14:13:41.057+05:30")
public class PoliciesApi implements Microservice  {
   private final PoliciesApiService delegate = PoliciesApiServiceFactory.getPoliciesApi();

    @GET
    @Path("/tierLevel/{tierLevel}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Get all policies", notes = "This operation can be used to list the available policies for a given policy level. Tier level should be specified as a path parameter and should be one of `api`, `application` and `resource`. ", response = TierListDTO.class, tags={ "Retrieve", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK. List of policies returned. ", response = TierListDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 304, message = "Not Modified. Empty body because the client has already the latest version of the requested resource (Will be supported in future). ", response = TierListDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 406, message = "Not Acceptable. The requested media type is not supported ", response = TierListDTO.class) })
    public Response policiesTierLevelTierLevelGet(@ApiParam(value = "List API or Application or Resource type policies. ",required=true, allowableValues="api, application, resource") @PathParam("tierLevel") String tierLevel
,@ApiParam(value = "Maximum size of resource array to return. ", defaultValue="25") @DefaultValue("25") @QueryParam("limit") Integer limit
,@ApiParam(value = "Starting point within the complete list of items qualified. ", defaultValue="0") @DefaultValue("0") @QueryParam("offset") Integer offset
,@ApiParam(value = "Media types acceptable for the response. Default is application/json. " , defaultValue="application/json")@HeaderParam("Accept") String accept
,@ApiParam(value = "Validator for conditional requests; based on the ETag of the formerly retrieved variant of the resourec. " )@HeaderParam("If-None-Match") String ifNoneMatch
, @Context Request request)
    throws NotFoundException {
        return delegate.policiesTierLevelTierLevelGet(tierLevel,limit,offset,accept,ifNoneMatch, request);
    }
    @POST
    @Path("/tierLevel/{tierLevel}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Create a Tier", notes = "This operation can be used to create a new throttling policy. The only supported policy level is `api` policies. `POST https://127.0.0.1:9443/api/am/admin/v1.0/policies/api`  **IMPORTANT:** * This is only effective when Advanced Throttling is disabled in the Server. If enabled, we need to use Admin REST API for throttling policies modification related operations. ", response = TierDTO.class, tags={ "Create", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 201, message = "Created. Successful response with the newly created object as entity in the body. Location header contains URL of newly created entity. ", response = TierDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error ", response = TierDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 415, message = "Unsupported media type. The entity of the request was in a not supported format. ", response = TierDTO.class) })
    public Response policiesTierLevelTierLevelPost(@ApiParam(value = "Tier object that should to be added " ,required=true) TierDTO body
,@ApiParam(value = "List API or Application or Resource type policies. ",required=true, allowableValues="api, application, resource") @PathParam("tierLevel") String tierLevel
,@ApiParam(value = "Media type of the entity in the body. Default is application/json. " ,required=true, defaultValue="application/json")@HeaderParam("Content-Type") String contentType
, @Context Request request)
    throws NotFoundException {
        return delegate.policiesTierLevelTierLevelPost(body,tierLevel,contentType, request);
    }
    @DELETE
    @Path("/tierLevel/{tierLevel}/tierName/{tierName}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Delete a Tier", notes = "This operation can be used to delete an existing policy. The only supported policy level is `api` policies. `DELETE https://127.0.0.1:9443/api/am/admin/v1.0/policies/api/Low`  **IMPORTANT:** * This is only effective when Advanced Throttling is disabled in the Server. If enabled, we need to use Admin REST API for throttling policies modification related operations. ", response = void.class, tags={ "Throttling Tier (Individual)", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK. Resource successfully deleted. ", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found. Resource to be deleted does not exist. ", response = void.class),
        
        @io.swagger.annotations.ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = void.class) })
    public Response policiesTierLevelTierLevelTierNameTierNameDelete(@ApiParam(value = "Tier name ",required=true) @PathParam("tierName") String tierName
,@ApiParam(value = "List API or Application or Resource type policies. ",required=true, allowableValues="api, application, resource") @PathParam("tierLevel") String tierLevel
,@ApiParam(value = "Validator for conditional requests; based on ETag. " )@HeaderParam("If-Match") String ifMatch
,@ApiParam(value = "Validator for conditional requests; based on Last Modified header. " )@HeaderParam("If-Unmodified-Since") String ifUnmodifiedSince
, @Context Request request)
    throws NotFoundException {
        return delegate.policiesTierLevelTierLevelTierNameTierNameDelete(tierName,tierLevel,ifMatch,ifUnmodifiedSince, request);
    }
    @PUT
    @Path("/tierLevel/{tierLevel}/tierName/{tierName}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update a Tier", notes = "This operation can be used to update an existing policy. The only supported policy level is `api` policies. `PUT https://127.0.0.1:9443/api/am/admin/v1.0/policies/api/Low`  **IMPORTANT:** * This is only effective when Advanced Throttling is disabled in the Server. If enabled, we need to use Admin REST API for throttling policies modification related operations. ", response = TierDTO.class, tags={ "Update", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK. Subscription updated. ", response = TierDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error. ", response = TierDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found. The resource to be updated does not exist. ", response = TierDTO.class),
        
        @io.swagger.annotations.ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = TierDTO.class) })
    public Response policiesTierLevelTierLevelTierNameTierNamePut(@ApiParam(value = "Tier name ",required=true) @PathParam("tierName") String tierName
,@ApiParam(value = "Tier object that needs to be modified " ,required=true) TierDTO body
,@ApiParam(value = "List API or Application or Resource type policies. ",required=true, allowableValues="api, application, resource") @PathParam("tierLevel") String tierLevel
,@ApiParam(value = "Media type of the entity in the body. Default is application/json. " ,required=true, defaultValue="application/json")@HeaderParam("Content-Type") String contentType
,@ApiParam(value = "Validator for conditional requests; based on ETag. " )@HeaderParam("If-Match") String ifMatch
,@ApiParam(value = "Validator for conditional requests; based on Last Modified header. " )@HeaderParam("If-Unmodified-Since") String ifUnmodifiedSince
, @Context Request request)
    throws NotFoundException {
        return delegate.policiesTierLevelTierLevelTierNameTierNamePut(tierName,body,tierLevel,contentType,ifMatch,ifUnmodifiedSince, request);
    }
    @POST
    @Path("/update-permission")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "Update policy permission", notes = "This operation can be used to update policy permissions which controls access for the particular policy based on the subscribers' roles. ", response = TierDTO.class, responseContainer = "List", tags={ "Throttling Tier (Individual)", })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "OK. Successfully updated policy permissions ", response = TierDTO.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request. Invalid request or validation error. ", response = TierDTO.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 403, message = "Forbidden. The request must be conditional but no condition has been specified. ", response = TierDTO.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found. Requested policy does not exist. ", response = TierDTO.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 412, message = "Precondition Failed. The request has not been performed because one of the preconditions is not met. ", response = TierDTO.class, responseContainer = "List") })
    public Response policiesUpdatePermissionPost(@ApiParam(value = "Name of the policy ",required=true) @QueryParam("tierName") String tierName
,@ApiParam(value = "List API or Application or Resource type policies. ",required=true, allowableValues="api, application, resource") @QueryParam("tierLevel") String tierLevel
,@ApiParam(value = "Validator for conditional requests; based on ETag. " )@HeaderParam("If-Match") String ifMatch
,@ApiParam(value = "Validator for conditional requests; based on Last Modified header. " )@HeaderParam("If-Unmodified-Since") String ifUnmodifiedSince
,@ApiParam(value = "" ) TierPermissionDTO permissions
, @Context Request request)
    throws NotFoundException {
        return delegate.policiesUpdatePermissionPost(tierName,tierLevel,ifMatch,ifUnmodifiedSince,permissions, request);
    }
}
