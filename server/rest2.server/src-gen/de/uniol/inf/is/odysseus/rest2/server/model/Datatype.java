package de.uniol.inf.is.odysseus.rest2.server.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.server.model.Schema;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Datatype
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class Datatype   {
  @JsonProperty("uri")
  private String uri;

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
        Objects.equals(this.subtype, datatype.subtype) &&
        Objects.equals(this.subschema, datatype.subschema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, subtype, subschema);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Datatype {\n");
    
    sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
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

