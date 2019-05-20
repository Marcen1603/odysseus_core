package de.uniol.inf.is.odysseus.client.communication.transformStrategies;

import java.util.List;

/**
 * This class stores a result tuple of a query as a list of objects
 * Created by Thore Stratmann
 */
public class QueryResults {
    private List<Object> values;



    public QueryResults(List<Object> values) {
        this.values = values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public List<Object> getValues() {
        return this.values;
    }

}
