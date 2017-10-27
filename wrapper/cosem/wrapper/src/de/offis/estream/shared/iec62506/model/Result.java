package de.offis.estream.shared.iec62506.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Basic class that every json schema class have to extend in some manner. It
 * describes meta data and abstract methods like {@link Result#setValues(Map)}.
 * 
 * @author Jens Plümer
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"timeinterval_start",
	"timeinterval_end",
	"minlstart",
	"maxlstart",
	"lend",
	"latency",
})
public abstract class Result {
	
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

	/**
	 * Sets values for json properties that are defined by the class.
	 * @param values a mapping from json properties to their values
	 */
	public abstract void setValues(Map<String, Object> values);

	/**
	 * Returns the schema of a class. A schema is a mapping from json properties to attribute names given by a tuple schema:
	 * 
	 * <pre>
	 * Tuple schema defined by an odysseus access operator looks like 
	 * [attributeName1|attributeName2|...] and is mapped to the json 
	 * properties that are defined by the schema class:
	 * 
	 * jsonProperty1 -> attributeName1
	 * jsonProperty2 -> attributeName2
	 * ...
	 * 
	 * </pre>
	 * @return describes a mapping from its json properties to a tuple schema
	 */
	@JsonIgnore
	public abstract Map<String, String> getSchema();
	
}
