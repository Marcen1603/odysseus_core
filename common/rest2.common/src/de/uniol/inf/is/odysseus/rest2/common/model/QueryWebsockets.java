package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * QueryWebsockets
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-14T10:51:57.707Z[GMT]")
public class QueryWebsockets   {
  @JsonProperty("operator")
  private String operator;

  @JsonProperty("port")
  private Integer port;

  @JsonProperty("protocol")
  private String protocol;

  @JsonProperty("websocket_url")
  private String websocketUrl;

  public QueryWebsockets operator(String operator) {
    this.operator = operator;
    return this;
  }

   /**
   * Get operator
   * @return operator
  **/
  @ApiModelProperty(value = "")
  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public QueryWebsockets port(Integer port) {
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

  public QueryWebsockets protocol(String protocol) {
    this.protocol = protocol;
    return this;
  }

   /**
   * Get protocol
   * @return protocol
  **/
  @ApiModelProperty(value = "")
  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public QueryWebsockets websocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
    return this;
  }

   /**
   * Get websocketUrl
   * @return websocketUrl
  **/
  @ApiModelProperty(value = "")
  public String getWebsocketUrl() {
    return websocketUrl;
  }

  public void setWebsocketUrl(String websocketUrl) {
    this.websocketUrl = websocketUrl;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryWebsockets queryWebsockets = (QueryWebsockets) o;
    return Objects.equals(this.operator, queryWebsockets.operator) &&
        Objects.equals(this.port, queryWebsockets.port) &&
        Objects.equals(this.protocol, queryWebsockets.protocol) &&
        Objects.equals(this.websocketUrl, queryWebsockets.websocketUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operator, port, protocol, websocketUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryWebsockets {\n");
    
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
    sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
    sb.append("    websocketUrl: ").append(toIndentedString(websocketUrl)).append("\n");
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

