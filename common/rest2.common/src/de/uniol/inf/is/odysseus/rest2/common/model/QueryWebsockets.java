package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * QueryWebsockets
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T17:35:32.862Z[GMT]")
public class QueryWebsockets   {
  @JsonProperty("operator")
  private String operator;

  @JsonProperty("port")
  private Integer port;

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
        Objects.equals(this.websocketUrl, queryWebsockets.websocketUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(operator, port, websocketUrl);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryWebsockets {\n");
    
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
    sb.append("    port: ").append(toIndentedString(port)).append("\n");
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

