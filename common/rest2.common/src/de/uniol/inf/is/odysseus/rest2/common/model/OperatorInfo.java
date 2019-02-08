package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * OperatorInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class OperatorInfo   {
  @JsonProperty("operatorname")
  private String operatorname;

  public OperatorInfo operatorname(String operatorname) {
    this.operatorname = operatorname;
    return this;
  }

   /**
   * Get operatorname
   * @return operatorname
  **/
  @ApiModelProperty(value = "")
  public String getOperatorname() {
    return operatorname;
  }

  public void setOperatorname(String operatorname) {
    this.operatorname = operatorname;
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
    return Objects.equals(this.operatorname, operatorInfo.operatorname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operatorname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorInfo {\n");
    
    sb.append("    operatorname: ").append(toIndentedString(operatorname)).append("\n");
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

