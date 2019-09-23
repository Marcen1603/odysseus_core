package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.uniol.inf.is.odysseus.rest2.common.model.OperatorPortWebsockets;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * OperatorPort
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class OperatorPort   {
  @JsonProperty("port")
  private Integer port;

  @JsonProperty("schema")
  private Schema schema = null;

  @JsonProperty("websockets")
  private List<OperatorPortWebsockets> websockets = null;

  public OperatorPort port(Integer port) {
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

  public OperatorPort schema(Schema schema) {
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

  public OperatorPort websockets(List<OperatorPortWebsockets> websockets) {
    this.websockets = websockets;
    return this;
  }

  public OperatorPort addWebsocketsItem(OperatorPortWebsockets websocketsItem) {
    if (this.websockets == null) {
      this.websockets = new ArrayList<OperatorPortWebsockets>();
    }
    this.websockets.add(websocketsItem);
    return this;
  }

   /**
   * Get websockets
   * @return websockets
  **/
  @ApiModelProperty(value = "")
  public List<OperatorPortWebsockets> getWebsockets() {
    return websockets;
  }

  public void setWebsockets(List<OperatorPortWebsockets> websockets) {
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
    OperatorPort operatorPort = (OperatorPort) o;
    return Objects.equals(this.port, operatorPort.port) &&
        Objects.equals(this.schema, operatorPort.schema) &&
        Objects.equals(this.websockets, operatorPort.websockets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(port, schema, websockets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OperatorPort {\n");
    
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

