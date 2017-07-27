package de.uniol.inf.is.odysseus.wrapper.iec62056.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "query_id",
    "result"
})
public final class ProcessedData {

    @JsonProperty("query_id")
    private String queryId;
    @JsonProperty("result")
    private Result result;

    @JsonProperty("query_id")
    public String getQueryId() {
        return queryId;
    }

    @JsonProperty("query_id")
    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    @JsonProperty("result")
    public Result getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result result) {
        this.result = result;
    }

    public static Result getResultType(String type) {
    	switch(type.toUpperCase()) {
    	case("RAWDATA"): return new RawData();
    	case("AGGREGATEDDATA") : return new AggregatedData();
    	}
		return null;
    }
    
}
