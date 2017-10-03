package de.uniol.inf.is.odysseus.wrapper.iec62056.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Defines raw cosem data that is neither processed nor manipulated but enriched
 * with odysseus meta data.
 * 
 * @author Jens Plümer
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "smgw_device_name",
    "logical_name",
    "value",
    "unit",
    "scaler",
    "status",
    "capture_time"
})
public class RawData extends Result {

//////////////SCHEMA
	@JsonIgnore
	private static final String SMGW_DEVICE_NAME_KEY = "smgw_device_name";
	
	@JsonIgnore
	private static final String LOGICAL_NAME_KEY = "logical_name";
	
	@JsonIgnore
	private static final String VALUE_KEY = "value";
	
	@JsonIgnore
	private static final String UNIT_KEY = "unit";
	
	@JsonIgnore
	private static final String SCALER_KEY = "scaler";
	
	@JsonIgnore
	private static final String STATUS_KEY = "status";
	
	@JsonIgnore
	private static final String CAPTURE_TIME_KEY = "capture_time";
//////////////	
	
	@JsonProperty("smgw_device_name")
	private String smgwDeviceName;
	
	@JsonProperty("logical_name")
	private String logicalName;
	
	@JsonProperty("value")
	private Double value;
	
	@JsonProperty("unit")
	private int unit;
	
	@JsonProperty("scaler")
	private int scaler;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("capture_time")
	private long captureTime;

	public String getSmgwDeviceName() {
		return smgwDeviceName;
	}

	@JsonGetter
	public String getLogicalName() {
		return logicalName;
	}

	@JsonGetter
	public Double getValue() {
		return value;
	}

	@JsonGetter
	public int getUnit() {
		return unit;
	}

	@JsonGetter
	public int getScaler() {
		return scaler;
	}

	@JsonGetter
	public String getStatus() {
		return status;
	}

	@JsonGetter
	public long getCaptureTime() {
		return captureTime;
	}
	
	@JsonSetter
	public void setSmgwDeviceName(String smgwDeviceName) {
		this.smgwDeviceName = smgwDeviceName;
	}

	@JsonSetter
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	@JsonSetter
	public void setValue(Double value) {
		this.value = value;
	}

	@JsonSetter
	public void setUnit(int unit) {
		this.unit = unit;
	}

	@JsonSetter
	public void setScaler(int scaler) {
		this.scaler = scaler;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonSetter
	public void setCaptureTime(long captureTime) {
		this.captureTime = captureTime;
	}
	
	@Override
	public void setValues(Map<String, Object> values) {
		for (Entry<String, Object> e : values.entrySet()) {
			if (getSchema().get(SMGW_DEVICE_NAME_KEY).equalsIgnoreCase(e.getKey())) {
				setSmgwDeviceName((String) e.getValue());
			} else if (getSchema().get(LOGICAL_NAME_KEY).equalsIgnoreCase(e.getKey())) {
				setLogicalName((String) e.getValue());
			} else if (getSchema().get(VALUE_KEY).equalsIgnoreCase(e.getKey())) {
				setValue((Double) e.getValue());
			} else if (getSchema().get(UNIT_KEY).equalsIgnoreCase(e.getKey())) {
				setUnit((int) e.getValue());
			} else if (getSchema().get(SCALER_KEY).equalsIgnoreCase(e.getKey())) {
				setScaler((int) e.getValue());
			} else if (getSchema().get(STATUS_KEY).equalsIgnoreCase(e.getKey())) {
				setStatus((String) e.getValue());
			} else if (getSchema().get(STATUS_KEY).equalsIgnoreCase(e.getKey())) {
				setCaptureTime((long) e.getValue());
			}
		}
	}
	
	@Override
	public Map<String, String> getSchema() {
		Map<String, String> schema = new HashMap<>();
		schema.put(SMGW_DEVICE_NAME_KEY, "smgw_device_name");
		schema.put(LOGICAL_NAME_KEY, "logical_name");
		schema.put(VALUE_KEY, "value");
		schema.put(UNIT_KEY, "unit");
		schema.put(SCALER_KEY, "scaler");
		schema.put(STATUS_KEY, "status");
		schema.put(CAPTURE_TIME_KEY, "capture_time");
		return schema;
	}
	
}
