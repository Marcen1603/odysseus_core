package de.uniol.inf.is.odysseus.shared.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * 
 * @author Jens Plümer
 *
 */
@JsonPropertyOrder({
	"id",
	"prediction",
	"type",
	"features"
})
public class PredictedData extends Result {
		
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("prediction")
	private String prediction;

	@JsonProperty("type")
	private String type;

	@JsonProperty("features")
	private Collection<String> features;
	
	public String getClazz() {
		return prediction;
	}

	@JsonSetter
	public void setClazz(Object object) {
		this.prediction = object.toString();
	}
	
	@JsonGetter
	public String getId() {
		return id;
	}

	@JsonSetter
	public void setId(String id) {
		this.id = id;
	}

	@JsonGetter
	public String getType() {
		return type;
	}

	@JsonSetter
	public void setType(String type) {
		this.type = type;
	}

	@JsonGetter
	public Collection<String> getFeatures() {
		return features;
	}

	@JsonSetter
	public void setFeatures(Collection<Object> features) {
		this.features = features.stream().map(e -> e.toString()).collect(Collectors.toList());
	}
	
////////////SCHEMA
	private final Map<String, String> schema = new HashMap<>();
	
	private final String ID = "id";
	
	private final String PREDICTION = "prediction";
	
	private final String TYPE = "type";
	
	private final String FEATURES = "features"; 
	
//////////////
//JSON EXAMPLE
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
	
	@Override
	@SuppressWarnings("unchecked")
	public void setValues(Map<String, Object> values) {
		for(Entry<String, Object> e : values.entrySet()) {
			if (getSchema().get(PREDICTION).equalsIgnoreCase(e.getKey())) {
				setClazz(e.getValue());
			} else if (getSchema().get(ID).equalsIgnoreCase(e.getKey())) {
				setId((String) e.getValue());
			} else if (getSchema().get(TYPE).equalsIgnoreCase(e.getKey())) {
				setType((String) e.getValue());
			} else if (getSchema().get(FEATURES).equalsIgnoreCase(e.getKey())) {
				setFeatures((Collection<Object>) e.getValue());
			}
		}
	}
	
	@Override
	public Map<String, String> getSchema() {
		schema.put(PREDICTION, "prediction");
		schema.put(ID, "id");
		schema.put(TYPE, "type");
		schema.put(FEATURES, "features");
		return schema;
	}

}
