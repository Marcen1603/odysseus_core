package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryPorts;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * QueryRootOperators
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-18T14:18:36.268Z[GMT]")
public class QueryRootOperators   {
  @JsonProperty("operatorName")
  private String operatorName;

  @JsonProperty("operatorDisplayName")
  private String operatorDisplayName;

  @JsonProperty("operatorType")
  private String operatorType;

  @JsonProperty("operatorImplementation")
  private String operatorImplementation;

  @JsonProperty("ports")
  private List<QueryPorts> ports = null;

  public QueryRootOperators operatorName(String operatorName) {
    this.operatorName = operatorName;
    return this;
  }

   /**
   * The name that identifies the operator instance (usually a UUID).
   * @return operatorName
  **/
  @ApiModelProperty(value = "The name that identifies the operator instance (usually a UUID).")
  public String getOperatorName() {
    return operatorName;
  }

  public void setOperatorName(String operatorName) {
    this.operatorName = operatorName;
  }

  public QueryRootOperators operatorDisplayName(String operatorDisplayName) {
    this.operatorDisplayName = operatorDisplayName;
    return this;
  }

   /**
   * The name that is set as 'NAME' property by the user.
   * @return operatorDisplayName
  **/
  @ApiModelProperty(value = "The name that is set as 'NAME' property by the user.")
  public String getOperatorDisplayName() {
    return operatorDisplayName;
  }

  public void setOperatorDisplayName(String operatorDisplayName) {
    this.operatorDisplayName = operatorDisplayName;
  }

  public QueryRootOperators operatorType(String operatorType) {
    this.operatorType = operatorType;
    return this;
  }

   /**
   * The name of the operator type (logical operator type, e.g. SELECT or PROJECT).
   * @return operatorType
  **/
  @ApiModelProperty(value = "The name of the operator type (logical operator type, e.g. SELECT or PROJECT).")
  public String getOperatorType() {
    return operatorType;
  }

  public void setOperatorType(String operatorType) {
    this.operatorType = operatorType;
  }

  public QueryRootOperators operatorImplementation(String operatorImplementation) {
    this.operatorImplementation = operatorImplementation;
    return this;
  }

   /**
   * The name of the operator type implementation (physical operator type, e.g. SelectPO or RelationalProjectPO).
   * @return operatorImplementation
  **/
  @ApiModelProperty(value = "The name of the operator type implementation (physical operator type, e.g. SelectPO or RelationalProjectPO).")
  public String getOperatorImplementation() {
    return operatorImplementation;
  }

  public void setOperatorImplementation(String operatorImplementation) {
    this.operatorImplementation = operatorImplementation;
  }

  public QueryRootOperators ports(List<QueryPorts> ports) {
    this.ports = ports;
    return this;
  }

  public QueryRootOperators addPortsItem(QueryPorts portsItem) {
    if (this.ports == null) {
      this.ports = new ArrayList<QueryPorts>();
    }
    this.ports.add(portsItem);
    return this;
  }

   /**
   * Get ports
   * @return ports
  **/
  @ApiModelProperty(value = "")
  public List<QueryPorts> getPorts() {
    return ports;
  }

  public void setPorts(List<QueryPorts> ports) {
    this.ports = ports;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryRootOperators queryRootOperators = (QueryRootOperators) o;
    return Objects.equals(this.operatorName, queryRootOperators.operatorName) &&
        Objects.equals(this.operatorDisplayName, queryRootOperators.operatorDisplayName) &&
        Objects.equals(this.operatorType, queryRootOperators.operatorType) &&
        Objects.equals(this.operatorImplementation, queryRootOperators.operatorImplementation) &&
        Objects.equals(this.ports, queryRootOperators.ports);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operatorName, operatorDisplayName, operatorType, operatorImplementation, ports);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryRootOperators {\n");
    
    sb.append("    operatorName: ").append(toIndentedString(operatorName)).append("\n");
    sb.append("    operatorDisplayName: ").append(toIndentedString(operatorDisplayName)).append("\n");
    sb.append("    operatorType: ").append(toIndentedString(operatorType)).append("\n");
    sb.append("    operatorImplementation: ").append(toIndentedString(operatorImplementation)).append("\n");
    sb.append("    ports: ").append(toIndentedString(ports)).append("\n");
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

