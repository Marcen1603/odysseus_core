package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * BundleInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-18T08:59:53.113Z[GMT]")
public class BundleInfo   {
  @JsonProperty("bundleId")
  private Long bundleId = null;

  @JsonProperty("lastModified")
  private Long lastModified = null;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    ACTIVE("ACTIVE"),
    
    INSTALLED("INSTALLED"),
    
    RESOLVED("RESOLVED"),
    
    UNINSTALLED("UNINSTALLED"),
    
    STARTING("STARTING"),
    
    STOPPING("STOPPING");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
  }

  @JsonProperty("state")
  private StateEnum state;

  @JsonProperty("symbolicName")
  private String symbolicName;

  @JsonProperty("version")
  private String version;

  public BundleInfo bundleId(Long bundleId) {
    this.bundleId = bundleId;
    return this;
  }

   /**
   * Get bundleId
   * @return bundleId
  **/
  @ApiModelProperty(value = "")
  public Long getBundleId() {
    return bundleId;
  }

  public void setBundleId(Long bundleId) {
    this.bundleId = bundleId;
  }

  public BundleInfo lastModified(Long lastModified) {
    this.lastModified = lastModified;
    return this;
  }

   /**
   * Get lastModified
   * @return lastModified
  **/
  @ApiModelProperty(value = "")
  public Long getLastModified() {
    return lastModified;
  }

  public void setLastModified(Long lastModified) {
    this.lastModified = lastModified;
  }

  public BundleInfo state(StateEnum state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public BundleInfo symbolicName(String symbolicName) {
    this.symbolicName = symbolicName;
    return this;
  }

   /**
   * Get symbolicName
   * @return symbolicName
  **/
  @ApiModelProperty(value = "")
  public String getSymbolicName() {
    return symbolicName;
  }

  public void setSymbolicName(String symbolicName) {
    this.symbolicName = symbolicName;
  }

  public BundleInfo version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(value = "")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BundleInfo bundleInfo = (BundleInfo) o;
    return Objects.equals(this.bundleId, bundleInfo.bundleId) &&
        Objects.equals(this.lastModified, bundleInfo.lastModified) &&
        Objects.equals(this.state, bundleInfo.state) &&
        Objects.equals(this.symbolicName, bundleInfo.symbolicName) &&
        Objects.equals(this.version, bundleInfo.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bundleId, lastModified, state, symbolicName, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BundleInfo {\n");
    
    sb.append("    bundleId: ").append(toIndentedString(bundleId)).append("\n");
    sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    symbolicName: ").append(toIndentedString(symbolicName)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

