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

@JsonPropertyOrder({
	"score",
	"features"
})
public class AnomalyData extends Result {

	@JsonProperty("score")
	private Double score;
	
	@JsonProperty("features")
	private Collection<String> features;
	
	@JsonGetter
	public Double getScore() {
		return score;
	}

	@JsonSetter
	public void setScore(Double score) {
		this.score = score;
	}
	
	@JsonGetter
	public Collection<String> getFeatures() {
		return features;
	}

	@JsonSetter
	public void setFeatures(Collection<Object> features) {
		this.features = features.stream().map(e -> e.toString()).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValues(Map<String, Object> values) {
		for(Entry<String, Object> e : values.entrySet()) {
			if (getSchema().get(SCORE).equalsIgnoreCase(e.getKey())) {
				setScore((Double) e.getValue());
			} else if (getSchema().get(FEATURES).equalsIgnoreCase(e.getKey())) {
				setFeatures((Collection<Object>) e.getValue());
			}
		}
	}
	
	private final String SCORE = "score";
	private final String FEATURES = "features";
	
	@Override
	public Map<String, String> getSchema() {
		
		Map<String, String> schema = new HashMap<>();
		schema.put(SCORE, "score");
		schema.put(FEATURES, "features");
		
		return schema;
	}

}
