package de.uniol.inf.is.odysseus.shared.model;

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
 * Defines raw cosem data that is neither processed nor manipulated but enriched with odysseus meta data.
 * 
 * @author Jens Plümer
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"smgw_id",
    "sm_id",
    "value",
    "scaler",
    "unit",
    "status",
    "capture_time",
    "type"
})
public class RawData extends Result {

	
//////////////SCHEMA
			
	@JsonIgnore
	private static final String SMWG_ID = "smgw_id";		
			
	@JsonIgnore
	private static final String SM_ID = "sm_id";
	
	@JsonIgnore
	private static final String VALUE_KEY = "value";
	
	@JsonIgnore
	private static final String SCALER_KEY = "scaler";
	
	@JsonIgnore
	private static final String UNIT_KEY = "unit";
	
	@JsonIgnore
	private static final String STATUS_KEY = "status";
	
	@JsonIgnore
	private static final String CAPTURE_TIME_KEY = "capture_time";
	
	@JsonIgnore
	private static final String TYPE = "type";
//////////////
// JSON EXAMPLE
//////////////
/*	
{  
	"result":
	{  
		"timeinterval_start":1518697014015,
		"timeinterval_end":1518697014082,
		"minlstart":-1,
		"maxlstart":-1,
		"lend":-1,
		"latency":-1,
		"smgw_id":"smgw_91f4e068-a71f-4de2-9783-830de605f141",
		"sm_id":"sm_6228a1c3-52c0-4db8-b448-66d6340b58d9",
		"value":30321.206612253598,
		"scaler":-3,
		"unit":30,
		"status":"000",
		"capture_time":4281556,
		"type":"producer"
	}
}
*/
//////////////
	
	@JsonProperty("smgw_id")
	private String smgw_id;
	
	@JsonProperty("sm_id")
	private String sm_id;
	
	@JsonProperty("value")
	private Double value;
	
	@JsonProperty("scaler")
	private Integer scaler;
	
	@JsonProperty("unit")
	private Integer unit;
	
	@JsonProperty("status")
	private String status;
	
	@JsonProperty("capture_time")
	private Long captureTime;
	
	@JsonProperty("type")
	private String type;

	@JsonGetter
	public String getSmgw_id() {
		return smgw_id;
	}
	
	@JsonGetter
	public String getSm_id() {
		return sm_id;
	}

	@JsonGetter
	public Double getValue() {
		return value;
	}

	@JsonGetter
	public Integer getUnit() {
		return unit;
	}

	@JsonGetter
	public Integer getScaler() {
		return scaler;
	}

	@JsonGetter
	public String getStatus() {
		return status;
	}

	@JsonGetter
	public Long getCaptureTime() {
		return captureTime;
	}
	
	@JsonGetter
	public String getType() {
		return type;
	}

	@JsonSetter
	public void setSmgw_id(String smgw_id) {
		this.smgw_id = smgw_id;
	}
	
	@JsonSetter
	public void setSMID(String sm_id) {
		this.sm_id = sm_id;
	}

	@JsonSetter
	public void setValue(Double value) {
		this.value = value;
	}

	@JsonSetter
	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	@JsonSetter
	public void setScaler(Integer scaler) {
		this.scaler = scaler;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonSetter
	public void setCaptureTime(Long captureTime) {
		this.captureTime = captureTime;
	}
	
	@JsonSetter
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public void setValues(Map<String, Object> values) {
		for (Entry<String, Object> e : values.entrySet()) {
			if (getSchema().get(SMWG_ID).equalsIgnoreCase(e.getKey())) {
				setSmgw_id((String) e.getValue());
			}else if (getSchema().get(SM_ID).equalsIgnoreCase(e.getKey())){
				setSMID((String) e.getValue());
			} else if (getSchema().get(VALUE_KEY).equalsIgnoreCase(e.getKey())) {
				setValue((Double) cast(e.getValue(), new Double(1)));
			} else if (getSchema().get(UNIT_KEY).equalsIgnoreCase(e.getKey())) {
				setUnit((Integer) cast(e.getValue(), new Integer(1)));
			} else if (getSchema().get(SCALER_KEY).equalsIgnoreCase(e.getKey())) {
				setScaler((Integer) cast(e.getValue(), new Integer(1)));
			} else if (getSchema().get(STATUS_KEY).equalsIgnoreCase(e.getKey())) {
				setStatus((String) e.getValue());
			} else if (getSchema().get(CAPTURE_TIME_KEY).equalsIgnoreCase(e.getKey())) {
				setCaptureTime((Long) cast(e.getValue(), new Long(1)));
			} else if (getSchema().get(TYPE).equals(e.getKey())) {
				setType((java.lang.String) e.getValue());
			}
		}
	}

	protected <T> Object cast(Object value, T clazz) {
		if (value instanceof String) {
			if (clazz instanceof Integer) {
				return Integer.parseInt(((String) value));
			}
			if (clazz instanceof Double) {
				return Double.parseDouble((String) value);
			}
			if (clazz instanceof Long) {
				return Long.parseLong((String) value);
			}
		}
		return value;
	}
	
	@Override
	public Map<String, String> getSchema() {
		Map<String, String> schema = new HashMap<>();
		schema.put(SMWG_ID, "smgw_id");
		schema.put(SM_ID, "sm_id");
		schema.put(VALUE_KEY, "value");
		schema.put(UNIT_KEY, "unit");
		schema.put(SCALER_KEY, "scaler");
		schema.put(STATUS_KEY, "status");
		schema.put(CAPTURE_TIME_KEY, "capture_time");
		schema.put(TYPE, "type");
		return schema;
	}
	
}
