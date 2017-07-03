package de.uniol.inf.os.odysseus.wrapper.iec62056.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"value",
"unit",
"scaler",
"status",
"capture_time",
"logical_name"
})
public class LogicalDevice {

@JsonProperty("value")
private Double value;
@JsonProperty("unit")
private Integer unit;
@JsonProperty("scaler")
private Integer scaler;
@JsonProperty("status")
private String status;
@JsonProperty("capture_time")
private Integer captureTime;
@JsonProperty("logical_name")
private String logicalName;
@JsonIgnore
private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

@JsonProperty("value")
public Double getValue() {
return value;
}

@JsonProperty("value")
public void setValue(Double value) {
this.value = value;
}

@JsonProperty("unit")
public Integer getUnit() {
return unit;
}

@JsonProperty("unit")
public void setUnit(Integer unit) {
this.unit = unit;
}

@JsonProperty("scaler")
public Integer getScaler() {
return scaler;
}

@JsonProperty("scaler")
public void setScaler(Integer scaler) {
this.scaler = scaler;
}

@JsonProperty("status")
public String getStatus() {
return status;
}

@JsonProperty("status")
public void setStatus(String status) {
this.status = status;
}

@JsonProperty("capture_time")
public Integer getCaptureTime() {
return captureTime;
}

@JsonProperty("capture_time")
public void setCaptureTime(Integer captureTime) {
this.captureTime = captureTime;
}

@JsonProperty("logical_name")
public String getLogicalName() {
return logicalName;
}

@JsonProperty("logical_name")
public void setLogicalName(String logicalName) {
this.logicalName = logicalName;
}

@JsonAnyGetter
public Map<String, java.lang.Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, java.lang.Object value) {
this.additionalProperties.put(name, value);
}

}
