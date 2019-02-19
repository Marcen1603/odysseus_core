package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.QueryWebsockets;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * QueryPorts
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-19T12:33:30.209Z[GMT]")
public class QueryPorts   {
  @JsonProperty("port")
  private Integer port;

  @JsonProperty("schema")
  private Schema schema = null;

  @JsonProperty("websockets")
  private List<QueryWebsockets> websockets = null;

  public QueryPorts port(Integer port) {
    this.port = port;
    return this;
  }

   /**
   * Get port
   * @return port
  **/
  @ApiModelProperty(value = "")
  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public QueryPorts schema(Schema schema) {
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

  public QueryPorts websockets(List<QueryWebsockets> websockets) {
    this.websockets = websockets;
    return this;
  }

  public QueryPorts addWebsocketsItem(QueryWebsockets websocketsItem) {
    if (this.websockets == null) {
      this.websockets = new ArrayList<QueryWebsockets>();
    }
    this.websockets.add(websocketsItem);
    return this;
  }

   /**
   * Get websockets
   * @return websockets
  **/
  @ApiModelProperty(value = "")
  public List<QueryWebsockets> getWebsockets() {
    return websockets;
  }

  public void setWebsockets(List<QueryWebsockets> websockets) {
    this.websockets = websockets;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryPorts queryPorts = (QueryPorts) o;
    return Objects.equals(this.port, queryPorts.port) &&
        Objects.equals(this.schema, queryPorts.schema) &&
        Objects.equals(this.websockets, queryPorts.websockets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(port, schema, websockets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryPorts {\n");
    
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    schema: ").append(toIndentedString(schema)).append("\n");
    sb.append("    websockets: ").append(toIndentedString(websockets)).append("\n");
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

