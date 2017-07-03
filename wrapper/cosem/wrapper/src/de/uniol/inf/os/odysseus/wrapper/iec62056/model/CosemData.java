package de.uniol.inf.os.odysseus.wrapper.iec62056.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"logical_name",
"objects"
})
public class CosemData {

@JsonProperty("logical_name")
private String logicalName;
@JsonProperty("objects")
private List<LogicalDevice> objects = null;
@JsonIgnore
private Map<String, java.lang.Object> additionalProperties = new HashMap<String, java.lang.Object>();

@JsonProperty("logical_name")
public String getLogicalName() {
return logicalName;
}

@JsonProperty("logical_name")
public void setLogicalName(String logicalName) {
this.logicalName = logicalName;
}

@JsonProperty("objects")
public List<LogicalDevice> getObjects() {
return objects;
}

@JsonProperty("objects")
public void setObjects(List<LogicalDevice> objects) {
this.objects = objects;
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