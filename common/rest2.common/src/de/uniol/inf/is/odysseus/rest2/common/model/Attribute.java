package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;

import io.swagger.annotations.ApiModelProperty;

/**
 * Attribute
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class Attribute   {
  @JsonProperty("sourcename")
  private String sourcename;

  @JsonProperty("attributename")
  private String attributename;

  @JsonProperty("datatype")
  private Datatype datatype = null;

  @JsonProperty("subschema")
  private Schema subschema = null;

  public Attribute sourcename(String sourcename) {
    this.sourcename = sourcename;
    return this;
  }

   /**
   * Get sourcename
   * @return sourcename
  **/
  @ApiModelProperty(value = "")
  public String getSourcename() {
    return sourcename;
  }

  public void setSourcename(String sourcename) {
    this.sourcename = sourcename;
  }

  public Attribute attributename(String attributename) {
    this.attributename = attributename;
    return this;
  }

   /**
   * Get attributename
   * @return attributename
  **/
  @ApiModelProperty(value = "")
  public String getAttributename() {
    return attributename;
  }

  public void setAttributename(String attributename) {
    this.attributename = attributename;
  }

  public Attribute datatype(Datatype datatype) {
    this.datatype = datatype;
    return this;
  }

   /**
   * Get datatype
   * @return datatype
  **/
  @ApiModelProperty(value = "")
  public Datatype getDatatype() {
    return datatype;
  }

  public void setDatatype(Datatype datatype) {
    this.datatype = datatype;
  }

  public Attribute subschema(Schema subschema) {
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
    Attribute attribute = (Attribute) o;
    return Objects.equals(this.sourcename, attribute.sourcename) &&
        Objects.equals(this.attributename, attribute.attributename) &&
        Objects.equals(this.datatype, attribute.datatype) &&
        Objects.equals(this.subschema, attribute.subschema);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourcename, attributename, datatype, subschema);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Attribute {\n");
    
    sb.append("    sourcename: ").append(toIndentedString(sourcename)).append("\n");
    sb.append("    attributename: ").append(toIndentedString(attributename)).append("\n");
    sb.append("    datatype: ").append(toIndentedString(datatype)).append("\n");
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

