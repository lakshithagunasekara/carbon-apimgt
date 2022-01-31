package org.wso2.carbon.apimgt.rest.api.publisher.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.apimgt.rest.api.publisher.v1.dto.OperationPolicySpecAttributeDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class OperationPolicyDataDTO   {
  
    private String policyCategory = null;
    private String policyId = null;
    private String policyName = null;
    private String policyDisplayName = null;
    private String sharedPolicyRef = null;
    private String policyDescription = null;
    private List<String> applicableFlows = new ArrayList<String>();
    private List<String> supportedGateways = new ArrayList<String>();
    private List<String> supportedApiTypes = new ArrayList<String>();
    private Boolean multipleAllowed = null;
    private List<OperationPolicySpecAttributeDTO> polictAttributes = new ArrayList<OperationPolicySpecAttributeDTO>();

  /**
   **/
  public OperationPolicyDataDTO policyCategory(String policyCategory) {
    this.policyCategory = policyCategory;
    return this;
  }

  
  @ApiModelProperty(example = "Mediation", value = "")
  @JsonProperty("policyCategory")
  public String getPolicyCategory() {
    return policyCategory;
  }
  public void setPolicyCategory(String policyCategory) {
    this.policyCategory = policyCategory;
  }

  /**
   **/
  public OperationPolicyDataDTO policyId(String policyId) {
    this.policyId = policyId;
    return this;
  }

  
  @ApiModelProperty(example = "121223q41-24141-124124124-12414", value = "")
  @JsonProperty("policyId")
  public String getPolicyId() {
    return policyId;
  }
  public void setPolicyId(String policyId) {
    this.policyId = policyId;
  }

  /**
   **/
  public OperationPolicyDataDTO policyName(String policyName) {
    this.policyName = policyName;
    return this;
  }

  
  @ApiModelProperty(example = "removeHeaderPolicy", value = "")
  @JsonProperty("policyName")
  public String getPolicyName() {
    return policyName;
  }
  public void setPolicyName(String policyName) {
    this.policyName = policyName;
  }

  /**
   **/
  public OperationPolicyDataDTO policyDisplayName(String policyDisplayName) {
    this.policyDisplayName = policyDisplayName;
    return this;
  }

  
  @ApiModelProperty(example = "Remove Header Policy", value = "")
  @JsonProperty("policyDisplayName")
  public String getPolicyDisplayName() {
    return policyDisplayName;
  }
  public void setPolicyDisplayName(String policyDisplayName) {
    this.policyDisplayName = policyDisplayName;
  }

  /**
   **/
  public OperationPolicyDataDTO sharedPolicyRef(String sharedPolicyRef) {
    this.sharedPolicyRef = sharedPolicyRef;
    return this;
  }

  
  @ApiModelProperty(example = "AddHeaderSharedPolicy", value = "")
  @JsonProperty("sharedPolicyRef")
  public String getSharedPolicyRef() {
    return sharedPolicyRef;
  }
  public void setSharedPolicyRef(String sharedPolicyRef) {
    this.sharedPolicyRef = sharedPolicyRef;
  }

  /**
   **/
  public OperationPolicyDataDTO policyDescription(String policyDescription) {
    this.policyDescription = policyDescription;
    return this;
  }

  
  @ApiModelProperty(example = "With this policy, user can add a new header to the request", value = "")
  @JsonProperty("policyDescription")
  public String getPolicyDescription() {
    return policyDescription;
  }
  public void setPolicyDescription(String policyDescription) {
    this.policyDescription = policyDescription;
  }

  /**
   **/
  public OperationPolicyDataDTO applicableFlows(List<String> applicableFlows) {
    this.applicableFlows = applicableFlows;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("applicableFlows")
  public List<String> getApplicableFlows() {
    return applicableFlows;
  }
  public void setApplicableFlows(List<String> applicableFlows) {
    this.applicableFlows = applicableFlows;
  }

  /**
   **/
  public OperationPolicyDataDTO supportedGateways(List<String> supportedGateways) {
    this.supportedGateways = supportedGateways;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("supportedGateways")
  public List<String> getSupportedGateways() {
    return supportedGateways;
  }
  public void setSupportedGateways(List<String> supportedGateways) {
    this.supportedGateways = supportedGateways;
  }

  /**
   **/
  public OperationPolicyDataDTO supportedApiTypes(List<String> supportedApiTypes) {
    this.supportedApiTypes = supportedApiTypes;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("supportedApiTypes")
  public List<String> getSupportedApiTypes() {
    return supportedApiTypes;
  }
  public void setSupportedApiTypes(List<String> supportedApiTypes) {
    this.supportedApiTypes = supportedApiTypes;
  }

  /**
   **/
  public OperationPolicyDataDTO multipleAllowed(Boolean multipleAllowed) {
    this.multipleAllowed = multipleAllowed;
    return this;
  }

  
  @ApiModelProperty(example = "true", value = "")
  @JsonProperty("multipleAllowed")
  public Boolean isMultipleAllowed() {
    return multipleAllowed;
  }
  public void setMultipleAllowed(Boolean multipleAllowed) {
    this.multipleAllowed = multipleAllowed;
  }

  /**
   **/
  public OperationPolicyDataDTO polictAttributes(List<OperationPolicySpecAttributeDTO> polictAttributes) {
    this.polictAttributes = polictAttributes;
    return this;
  }

  
  @ApiModelProperty(value = "")
      @Valid
  @JsonProperty("polictAttributes")
  public List<OperationPolicySpecAttributeDTO> getPolictAttributes() {
    return polictAttributes;
  }
  public void setPolictAttributes(List<OperationPolicySpecAttributeDTO> polictAttributes) {
    this.polictAttributes = polictAttributes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperationPolicyDataDTO operationPolicyData = (OperationPolicyDataDTO) o;
    return Objects.equals(policyCategory, operationPolicyData.policyCategory) &&
        Objects.equals(policyId, operationPolicyData.policyId) &&
        Objects.equals(policyName, operationPolicyData.policyName) &&
        Objects.equals(policyDisplayName, operationPolicyData.policyDisplayName) &&
        Objects.equals(sharedPolicyRef, operationPolicyData.sharedPolicyRef) &&
        Objects.equals(policyDescription, operationPolicyData.policyDescription) &&
        Objects.equals(applicableFlows, operationPolicyData.applicableFlows) &&
        Objects.equals(supportedGateways, operationPolicyData.supportedGateways) &&
        Objects.equals(supportedApiTypes, operationPolicyData.supportedApiTypes) &&
        Objects.equals(multipleAllowed, operationPolicyData.multipleAllowed) &&
        Objects.equals(polictAttributes, operationPolicyData.polictAttributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(policyCategory, policyId, policyName, policyDisplayName, sharedPolicyRef, policyDescription, applicableFlows, supportedGateways, supportedApiTypes, multipleAllowed, polictAttributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperationPolicyDataDTO {\n");
    
    sb.append("    policyCategory: ").append(toIndentedString(policyCategory)).append("\n");
    sb.append("    policyId: ").append(toIndentedString(policyId)).append("\n");
    sb.append("    policyName: ").append(toIndentedString(policyName)).append("\n");
    sb.append("    policyDisplayName: ").append(toIndentedString(policyDisplayName)).append("\n");
    sb.append("    sharedPolicyRef: ").append(toIndentedString(sharedPolicyRef)).append("\n");
    sb.append("    policyDescription: ").append(toIndentedString(policyDescription)).append("\n");
    sb.append("    applicableFlows: ").append(toIndentedString(applicableFlows)).append("\n");
    sb.append("    supportedGateways: ").append(toIndentedString(supportedGateways)).append("\n");
    sb.append("    supportedApiTypes: ").append(toIndentedString(supportedApiTypes)).append("\n");
    sb.append("    multipleAllowed: ").append(toIndentedString(multipleAllowed)).append("\n");
    sb.append("    polictAttributes: ").append(toIndentedString(polictAttributes)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

