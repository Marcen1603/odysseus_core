package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * OperatorInfoParameters
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-15T18:59:56.032Z[GMT]")
public class OperatorInfoParameters   {
  @JsonProperty("parameterType")
  private String parameterType;

  @JsonProperty("parameterName")
  private String parameterName;

  @JsonProperty("list")
  private Boolean list;

  @JsonProperty("doc")
  private String doc;

  @JsonProperty("mandatory")
  private Boolean mandatory;

  @JsonProperty("possibleValues")
  private List<String> possibleValues = null;

  @JsonProperty("deprecated")
  private Boolean deprecated;

  public OperatorInfoParameters parameterType(String parameterType) {
    this.parameterType = parameterType;
    return this;
  }

   /**
   * Get parameterType
   * @return parameterType
  **/
  @ApiModelProperty(value = "")
  public String getParameterType() {
    return parameterType;
  }

  public void setParameterType(String parameterType) {
    this.parameterType = parameterType;
  }

  public OperatorInfoParameters parameterName(String parameterName) {
    this.parameterName = parameterName;
    return this;
  }

   /**
   * Get parameterName
   * @return parameterName
  **/
  @ApiModelProperty(value = "")
  public String getParameterName() {
    return parameterName;
  }

  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  public OperatorInfoParameters list(Boolean list) {
    this.list = list;
    return this;
  }

   /**
   * Get list
   * @return list
  **/
  @ApiModelProperty(value = "")
  public Boolean getList() {
    return list;
  }

  public void setList(Boolean list) {
    this.list = list;
  }

  public OperatorInfoParameters doc(String doc) {
    this.doc = doc;
    return this;
  }

   /**
   * Get doc
   * @return doc
  **/
  @ApiModelProperty(value = "")
  public String getDoc() {
    return doc;
  }

  public void setDoc(String doc) {
    this.doc = doc;
  }

  public OperatorInfoParameters mandatory(Boolean mandatory) {
    this.mandatory = mandatory;
    return this;
  }

   /**
   * Get mandatory
   * @return mandatory
  **/
  @ApiModelProperty(value = "")
  public Boolean getMandatory() {
    return mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public OperatorInfoParameters possibleValues(List<String> possibleValues) {
    this.possibleValues = possibleValues;
    return this;
  }

  public OperatorInfoParameters addPossibleValuesItem(String possibleValuesItem) {
    if (this.possibleValues == null) {
      this.possibleValues = new ArrayList<String>();
    }
    this.possibleValues.add(possibleValuesItem);
    return this;
  }

   /**
   * Get possibleValues
   * @return possibleValues
  **/
  @ApiModelProperty(value = "")
  public List<String> getPossibleValues() {
    return possibleValues;
  }

  public void setPossibleValues(List<String> possibleValues) {
    this.possibleValues = possibleValues;
  }

  public OperatorInfoParameters deprecated(Boolean deprecated) {
    this.deprecated = deprecated;
    return this;
  }

   /**
   * Get deprecated
   * @return deprecated
  **/
  @ApiModelProperty(value = "")
  public Boolean getDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean deprecated) {
    this.deprecated = deprecated;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OperatorInfoParameters operatorInfoParameters = (OperatorInfoParameters) o;
    return Objects.equals(this.parameterType, operatorInfoParameters.parameterType) &&
        Objects.equals(this.parameterName, operatorInfoParameters.parameterName) &&
        Objects.equals(this.list, operatorInfoParameters.list) &&
        Objects.equals(this.doc, operatorInfoParameters.doc) &&
        Objects.equals(this.mandatory, operatorInfoParameters.mandatory) &&
        Objects.equals(this.possibleValues, operatorInfoParameters.possibleValues) &&
        Objects.equals(this.deprecated, operatorInfoParameters.deprecated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameterType, parameterName, list, doc, mandatory, possibleValues, deprecated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorInfoParameters {\n");
    
    sb.append("    parameterType: ").append(toIndentedString(parameterType)).append("\n");
    sb.append("    parameterName: ").append(toIndentedString(parameterName)).append("\n");
    sb.append("    list: ").append(toIndentedString(list)).append("\n");
    sb.append("    doc: ").append(toIndentedString(doc)).append("\n");
    sb.append("    mandatory: ").append(toIndentedString(mandatory)).append("\n");
    sb.append("    possibleValues: ").append(toIndentedString(possibleValues)).append("\n");
    sb.append("    deprecated: ").append(toIndentedString(deprecated)).append("\n");
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

