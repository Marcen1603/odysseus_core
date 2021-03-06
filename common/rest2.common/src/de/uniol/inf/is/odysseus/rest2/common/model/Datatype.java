package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import io.swagger.annotations.ApiModelProperty;

/**
 * Datatype
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class Datatype   {
  @JsonProperty("uri")
  private String uri;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    BASE("BASE"),
    
    TUPLE("TUPLE"),
    
    MULTI_VALUE("MULTI_VALUE"),
    
    BEAN("BEAN"),
    
    LIST("LIST"),
    
    GENERIC("GENERIC");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
  }

  @JsonProperty("type")
  private TypeEnum type;

  @JsonProperty("subtype")
  private Datatype subtype = null;

  @JsonProperty("subschema")
  private Schema subschema = null;

  public Datatype uri(String uri) {
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

  public Datatype type(TypeEnum type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Datatype subtype(Datatype subtype) {
    this.subtype = subtype;
    return this;
  }

   /**
   * Get subtype
   * @return subtype
  **/
  @ApiModelProperty(value = "")
  public Datatype getSubtype() {
    return subtype;
  }

  public void setSubtype(Datatype subtype) {
    this.subtype = subtype;
  }

  public Datatype subschema(Schema subschema) {
    this.subschema = subschema;
    return this;
  }

   /**
   * Get subschema
   * @return subschema
  **/
  @ApiModelProperty(value = "")
  public Schema getSubschema() {
    return subschema;
  }

  public void setSubschema(Schema subschema) {
    this.subschema = subschema;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Datatype datatype = (Datatype) o;
    return Objects.equals(this.uri, datatype.uri) &&
        Objects.equals(this.type, datatype.type) &&
        Objects.equals(this.subtype, datatype.subtype) &&
        Objects.equals(this.subschema, datatype.subschema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, type, subtype, subschema);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Datatype {\n");
    
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    subtype: ").append(toIndentedString(subtype)).append("\n");
    sb.append("    subschema: ").append(toIndentedString(subschema)).append("\n");
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

