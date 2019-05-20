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
 * Metaschema
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class Metaschema   {
  @JsonProperty("uri")
  private String uri;

  @JsonProperty("typeClass")
  private String typeClass;

  @JsonProperty("attributes")
  private List<Attribute> attributes = null;

  @JsonProperty("metaattributeClass")
  private String metaattributeClass;
  
  public Metaschema uri(String uri) {
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

  public Metaschema typeClass(String typeClass) {
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

  public Metaschema attributes(List<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public Metaschema addAttributesItem(Attribute attributesItem) {
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

  public Metaschema metaattributeClass(String metaattributeClass) {
    this.metaattributeClass = metaattributeClass;
    return this;
  }

   /**
   * Get metaattributeClass
   * @return metaattributeClass
  **/
  @ApiModelProperty(value = "")
  public String getMetaattributeClass() {
    return metaattributeClass;
  }

  public void setMetaattributeClass(String metaattributeClass) {
    this.metaattributeClass = metaattributeClass;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Metaschema metaschema = (Metaschema) o;
    return Objects.equals(this.uri, metaschema.uri) &&
        Objects.equals(this.typeClass, metaschema.typeClass) &&
        Objects.equals(this.attributes, metaschema.attributes) &&
        Objects.equals(this.metaattributeClass, metaschema.metaattributeClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, typeClass, attributes, metaattributeClass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Metaschema {\n");
    
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    typeClass: ").append(toIndentedString(typeClass)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    metaattributeClass: ").append(toIndentedString(metaattributeClass)).append("\n");
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

