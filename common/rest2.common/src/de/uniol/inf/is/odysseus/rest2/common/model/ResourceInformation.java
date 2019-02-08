package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uniol.inf.is.odysseus.rest2.common.model.Resource;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;

import io.swagger.annotations.ApiModelProperty;

/**
 * ResourceInformation
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class ResourceInformation   {
  @JsonProperty("name")
  private Resource name = null;

  @JsonProperty("kind")
  private String kind;

  @JsonProperty("outputSchema")
  private Schema outputSchema = null;

  public ResourceInformation name(Resource name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(value = "")
  public Resource getName() {
    return name;
  }

  public void setName(Resource name) {
    this.name = name;
  }

  public ResourceInformation kind(String kind) {
    this.kind = kind;
    return this;
  }

   /**
   * Get kind
   * @return kind
  **/
  @ApiModelProperty(value = "")
  public String getKind() {
    return kind;
  }

  public void setKind(String kind) {
    this.kind = kind;
  }

  public ResourceInformation outputSchema(Schema outputSchema) {
    this.outputSchema = outputSchema;
    return this;
  }

   /**
   * Get outputSchema
   * @return outputSchema
  **/
  @ApiModelProperty(value = "")
  public Schema getOutputSchema() {
    return outputSchema;
  }

  public void setOutputSchema(Schema outputSchema) {
    this.outputSchema = outputSchema;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceInformation resourceInformation = (ResourceInformation) o;
    return Objects.equals(this.name, resourceInformation.name) &&
        Objects.equals(this.kind, resourceInformation.kind) &&
        Objects.equals(this.outputSchema, resourceInformation.outputSchema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, kind, outputSchema);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResourceInformation {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    outputSchema: ").append(toIndentedString(outputSchema)).append("\n");
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

