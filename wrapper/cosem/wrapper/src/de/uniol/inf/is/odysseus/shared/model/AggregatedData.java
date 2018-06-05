package de.uniol.inf.is.odysseus.shared.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 
 * @author Jens Plümer
 *
 */
@JsonPropertyOrder({
	"avg_value",
	"max_value",
	"min_value",
	"sum_value",
	"stddev_value",
	"var_value"
})
public class AggregatedData extends Result {
	
//////////////SCHEMA
	@JsonIgnore
	private static final String AVG_VALUE = "avg_value" ;
	
	@JsonIgnore
	private static final String MAX_VALUE = "max_value" ;
	
	@JsonIgnore
	private static final String MIN_VALUE = "min_value" ;
	
	@JsonIgnore
	private static final String SUM_VALUE = "sum_value" ;
	
	@JsonIgnore
	private static final String STDDEV_VALUE = "stddev_value" ;
	
	@JsonIgnore
	private static final String VAR_VALUE = "var_value" ;
//////////////
// JSON EXAMPLE
//////////////	
/*	
 {  
  "result":
  {  
    "avg_value":1.8400733853306126E7,
    "max_value":3.67711465E7,
    "min_value":30321.206612253598,
    "sum_value":3.680146770661225E7,
    "stddev_value":2.59796867113447E7,
    "var_value":6.749441216196204E14,
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
	
	@JsonProperty("avg_value")
	private Double AVG_value = -1.0;

	@JsonProperty("max_value")
	private Double MAX_value = -1.0;

	@JsonProperty("min_value")
	private Double MIN_value = -1.0;

	@JsonProperty("sum_value")
	private Double SUM_value = -1.0;

	@JsonProperty("stddev_value")
	private Double STDDEV_value = -1.0;

	@JsonProperty("var_value")
	private Double VAR_value = -1.0;
	
	@JsonGetter
	public Double getAvg_value() {
		return AVG_value;
	}

	@JsonGetter
	public Double getMax_value() {
		return MAX_value;
	}

	@JsonGetter
	public Double getMin_value() {
		return MIN_value;
	}

	@JsonGetter
	public Double getSum_value() {
		return SUM_value;
	}
	
	@JsonGetter
	public Double getStddev_value() {
		return STDDEV_value;
	}
	
	@JsonGetter
	public Double getVar_value() {
		return VAR_value;
	}
	
	@JsonSetter
	public void setAvg_value(Double AVG_value) {
		this.AVG_value = AVG_value;
	}
	@JsonSetter
	public void setMax_value(Double MAX_value) {
		this.MAX_value = MAX_value;
	}
	
	@JsonSetter
	public void setMin_value(Double MIN_value) {
		this.MIN_value = MIN_value;
	}
	
	@JsonSetter
	public void setSum_value(Double SUM_value) {
		this.SUM_value = SUM_value;
	}
	
	@JsonSetter
	public void setStddev_value(Double STDDEV_value) {
		this.STDDEV_value = STDDEV_value;
	}

	@JsonSetter
	public void setVar_value(Double VAR_value) {
		this.VAR_value = VAR_value;
	}

	@Override
	public void setValues(Map<String, Object> values) {
		for(Entry<String, Object> e : values.entrySet()) {
			if (getSchema().get(AVG_VALUE).equalsIgnoreCase(e.getKey())) {
				setAvg_value((Double) e.getValue());
			} else if (getSchema().get(MAX_VALUE).equalsIgnoreCase(e.getKey())) {
				setMax_value((Double) e.getValue());
			} else if (getSchema().get(MIN_VALUE).equalsIgnoreCase(e.getKey())) {
				setMin_value((Double) e.getValue());
			} else if (getSchema().get(SUM_VALUE).equalsIgnoreCase(e.getKey())) {
				setSum_value((Double) e.getValue());
			} else if (getSchema().get(STDDEV_VALUE).equalsIgnoreCase(e.getKey())) {
				setStddev_value((Double) e.getValue());
			} else if (getSchema().get(VAR_VALUE).equalsIgnoreCase(e.getKey())) {
				setVar_value((Double) e.getValue());
			}

		}
	}

	@Override
	public Map<String, String> getSchema() {
		Map<String, String> schema = new HashMap<>();
		schema.put(AVG_VALUE, "avg_value");
		schema.put(MAX_VALUE, "max_value");
		schema.put(MIN_VALUE, "min_value");
		schema.put(SUM_VALUE, "sum_value");
		schema.put(STDDEV_VALUE, "stddev_value");
		schema.put(VAR_VALUE, "var_value");
		
		return schema;
	}


}
