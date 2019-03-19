package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.OperatorInfoParameters;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * OperatorInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-15T18:59:56.032Z[GMT]")
public class OperatorInfo   {
  @JsonProperty("operatorName")
  private String operatorName;

  @JsonProperty("doc")
  private String doc;

  @JsonProperty("url")
  private String url;

  @JsonProperty("parameters")
  private List<OperatorInfoParameters> parameters = null;

  @JsonProperty("maxPorts")
  private Integer maxPorts;

  @JsonProperty("minPorts")
  private Integer minPorts;

  @JsonProperty("categories")
  private List<String> categories = null;

  @JsonProperty("hidden")
  private Boolean hidden;

  @JsonProperty("deprecated")
  private Boolean deprecated;

  public OperatorInfo operatorName(String operatorName) {
    this.operatorName = operatorName;
    return this;
  }

   /**
   * Get operatorName
   * @return operatorName
  **/
  @ApiModelProperty(value = "")
  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public OperatorInfo doc(String doc) {
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

  public OperatorInfo url(String url) {
    this.url = url;
    return this;
  }

   /**
   * Get url
   * @return url
  **/
  @ApiModelProperty(value = "")
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public OperatorInfo parameters(List<OperatorInfoParameters> parameters) {
    this.parameters = parameters;
    return this;
  }

  public OperatorInfo addParametersItem(OperatorInfoParameters parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<OperatorInfoParameters>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

   /**
   * Get parameters
   * @return parameters
  **/
  @ApiModelProperty(value = "")
  public List<OperatorInfoParameters> getParameters() {
    return parameters;
  }

  public void setParameters(List<OperatorInfoParameters> parameters) {
    this.parameters = parameters;
  }

  public OperatorInfo maxPorts(Integer maxPorts) {
    this.maxPorts = maxPorts;
    return this;
  }

   /**
   * Get maxPorts
   * @return maxPorts
  **/
  @ApiModelProperty(value = "")
  public Integer getMaxPorts() {
    return maxPorts;
  }

  public void setMaxPorts(Integer maxPorts) {
    this.maxPorts = maxPorts;
  }

  public OperatorInfo minPorts(Integer minPorts) {
    this.minPorts = minPorts;
    return this;
  }

   /**
   * Get minPorts
   * @return minPorts
  **/
  @ApiModelProperty(value = "")
  public Integer getMinPorts() {
    return minPorts;
  }

  public void setMinPorts(Integer minPorts) {
    this.minPorts = minPorts;
  }

  public OperatorInfo categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public OperatorInfo addCategoriesItem(String categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<String>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

   /**
   * Get categories
   * @return categories
  **/
  @ApiModelProperty(value = "")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  public OperatorInfo hidden(Boolean hidden) {
    this.hidden = hidden;
    return this;
  }

   /**
   * Get hidden
   * @return hidden
  **/
  @ApiModelProperty(value = "")
  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public OperatorInfo deprecated(Boolean deprecated) {
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
    OperatorInfo operatorInfo = (OperatorInfo) o;
    return Objects.equals(this.operatorName, operatorInfo.operatorName) &&
        Objects.equals(this.doc, operatorInfo.doc) &&
        Objects.equals(this.url, operatorInfo.url) &&
        Objects.equals(this.parameters, operatorInfo.parameters) &&
        Objects.equals(this.maxPorts, operatorInfo.maxPorts) &&
        Objects.equals(this.minPorts, operatorInfo.minPorts) &&
        Objects.equals(this.categories, operatorInfo.categories) &&
        Objects.equals(this.hidden, operatorInfo.hidden) &&
        Objects.equals(this.deprecated, operatorInfo.deprecated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operatorName, doc, url, parameters, maxPorts, minPorts, categories, hidden, deprecated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorInfo {\n");
    
    sb.append("    operatorName: ").append(toIndentedString(operatorName)).append("\n");
    sb.append("    doc: ").append(toIndentedString(doc)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
    sb.append("    maxPorts: ").append(toIndentedString(maxPorts)).append("\n");
    sb.append("    minPorts: ").append(toIndentedString(minPorts)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    hidden: ").append(toIndentedString(hidden)).append("\n");
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

