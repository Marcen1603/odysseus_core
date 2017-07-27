package de.uniol.inf.is.odysseus.wrapper.iec62056.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"timeinterval_start",
	"timeinterval_end",
	"minlstart",
	"maxlstart",
	"lend",
	"latency",
	"datarates"
})
public abstract class Result {
	
/* 
 * Further information about odysseus meta data can be found here: 
 * https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Meta+data 
 */	
	
	@JsonProperty("timeinterval_start")
	private long timeintervalStart;
	
	@JsonProperty("timeinterval_end")
	private long timeintervalEnd;
	
	@JsonProperty("minlstart")
	private long minlStart;
	
	@JsonProperty("maxlstart")
	private long maxlStart;
	
	@JsonProperty("lend")
	private long lend;
	
	@JsonProperty("latency")
	private long latency;
	
	@JsonProperty("datarates")
	private Map<String, Double> datarates;
	
	@JsonGetter
	public long getTimeintervalStart() {
		return timeintervalStart;
	}

	@JsonGetter
	public long getTimeintervalEnd() {
		return timeintervalEnd;
	}

	@JsonGetter
	public long getMinlStart() {
		return minlStart;
	}

	@JsonGetter
	public long getMaxlStart() {
		return maxlStart;
	}
	
	@JsonGetter
	public long getLend() {
		return lend;
	}

	@JsonGetter
	public long getLatency() {
		return latency;
	}

	@JsonGetter
	public Map<String, Double> getDatarates() {
		return datarates;
	}

	@JsonSetter
	public void setTimeintervalStart(long timeintervalStart) {
		this.timeintervalStart = timeintervalStart;
	}

	@JsonSetter
	public void setTimeintervalEnd(long timeintervalEnd) {
		this.timeintervalEnd = timeintervalEnd;
	}

	@JsonSetter
	public void setMinlStart(long minlStart) {
		this.minlStart = minlStart;
	}

	@JsonSetter
	public void setMaxlStart(long maxlStart) {
		this.maxlStart = maxlStart;
	}

	@JsonSetter
	public void setLend(long lend) {
		this.lend = lend;
	}

	@JsonSetter
	public void setLatency(long latency) {
		this.latency = latency;
	}

	@JsonSetter
	public void setDatarate(Map<String, Double> datarates) {
		this.datarates = datarates;
	}
	
	public abstract void setValues(Map<String, Object> values);
	@JsonIgnore
	public abstract Map<String, String> getSchema();
	
}
