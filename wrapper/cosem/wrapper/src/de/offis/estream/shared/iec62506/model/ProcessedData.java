package de.offis.estream.shared.iec62506.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Defines processed cosem data as json where {@link ProcessedData#queryId} is a
 * unique ID and {@link ProcessedData#result} is the actually data that always
 * contains meta data information.
 * 
 * @author Jens Plümer
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result"
})
public final class ProcessedData {

	@JsonProperty("result")
	private Result result;

	@JsonProperty("result")
	public Result getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(Result result) {
		this.result = result;
	}

    /**
     * Returns an object of type {@link Result} by the given class name.
     * @param classname
     * @return object of type {@link Result}
     */
	public static Result getResultType(String classname) {
		switch (classname.toUpperCase()) {
		case ("RAWDATA"):
			return new RawData();
		case ("AGGREGATEDDATA"):
			return new AggregatedData();
		}
		return null;
	}
    
}
