package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Resource
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class Resource   {
  @JsonProperty("owner")
  private String owner;

  @JsonProperty("name")
  private String name;

  @JsonProperty("schema")
  private Schema schema = null;

  @JsonProperty("type")
  private String type;

  public Resource owner(String owner) {
    this.owner = owner;
    return this;
  }

   /**
   * The user that owns this resource.
   * @return owner
  **/
  @ApiModelProperty(value = "The user that owns this resource.")
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Resource name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the resource.
   * @return name
  **/
  @ApiModelProperty(value = "The name of the resource.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Resource schema(Schema schema) {
    this.schema = schema;
    return this;
  }

   /**
   * Get schema
   * @return schema
  **/
  @ApiModelProperty(value = "")
  public Schema getSchema() {
    return schema;
  }

  public void setSchema(Schema schema) {
    this.schema = schema;
  }

  public Resource type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Resource resource = (Resource) o;
    return Objects.equals(this.owner, resource.owner) &&
        Objects.equals(this.name, resource.name) &&
        Objects.equals(this.schema, resource.schema) &&
        Objects.equals(this.type, resource.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner, name, schema, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Resource {\n");
    
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

