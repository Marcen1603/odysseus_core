package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * EventWebSocket
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class EventWebSocket   {
  @JsonProperty("type")
  private String type;

  @JsonProperty("description")
  private String description;

  @JsonProperty("websocket_uri")
  private String websocketUri;

  public EventWebSocket type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public EventWebSocket description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public EventWebSocket websocketUri(String websocketUri) {
    this.websocketUri = websocketUri;
    return this;
  }

   /**
   * Get websocketUri
   * @return websocketUri
  **/
  @ApiModelProperty(value = "")
  public String getWebsocketUri() {
    return websocketUri;
  }

  public void setWebsocketUri(String websocketUri) {
    this.websocketUri = websocketUri;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventWebSocket eventWebSocket = (EventWebSocket) o;
    return Objects.equals(this.type, eventWebSocket.type) &&
        Objects.equals(this.description, eventWebSocket.description) &&
        Objects.equals(this.websocketUri, eventWebSocket.websocketUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, websocketUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventWebSocket {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    websocketUri: ").append(toIndentedString(websocketUri)).append("\n");
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

