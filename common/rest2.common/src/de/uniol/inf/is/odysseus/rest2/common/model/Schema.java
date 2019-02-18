package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.Attribute;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-18T16:05:16.362Z[GMT]")
public class Schema   {
  @JsonProperty("uri")
  private String uri;

  @JsonProperty("typeClass")
  private String typeClass;

  @JsonProperty("attributes")
  private List<Attribute> attributes = null;

  public Schema uri(String uri) {
    this.uri = uri;
    return this;
  }

   /**
   * Get uri
   * @return uri
  **/
  @ApiModelProperty(value = "")
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public Schema typeClass(String typeClass) {
    this.typeClass = typeClass;
    return this;
  }

   /**
   * Get typeClass
   * @return typeClass
  **/
  @ApiModelProperty(value = "")
  public String getTypeClass() {
    return typeClass;
  }

  public void setTypeClass(String typeClass) {
    this.typeClass = typeClass;
  }

  public Schema attributes(List<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Schema addAttributesItem(Attribute attributesItem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<Attribute>();
    }
    this.attributes.add(attributesItem);
    return this;
  }

   /**
   * Get attributes
   * @return attributes
  **/
  @ApiModelProperty(value = "")
  public List<Attribute> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<Attribute> attributes) {
    this.attributes = attributes;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Schema schema = (Schema) o;
    return Objects.equals(this.uri, schema.uri) &&
        Objects.equals(this.typeClass, schema.typeClass) &&
        Objects.equals(this.attributes, schema.attributes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, typeClass, attributes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Schema {\n");
    
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    typeClass: ").append(toIndentedString(typeClass)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
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

