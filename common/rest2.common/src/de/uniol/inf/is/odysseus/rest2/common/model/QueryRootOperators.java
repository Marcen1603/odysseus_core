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
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-19T12:33:30.209Z[GMT]")
public class QueryRootOperators   {
  @JsonProperty("operatorName")
  private String operatorName;

  @JsonProperty("ports")
  private List<QueryPorts> ports = null;

  public QueryRootOperators operatorName(String operatorName) {
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
        Objects.equals(this.ports, queryRootOperators.ports);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operatorName, ports);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryRootOperators {\n");
    
    sb.append("    operatorName: ").append(toIndentedString(operatorName)).append("\n");
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

