package de.uniol.inf.is.odysseus.test.set;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class TestSet {
	
	private String query;	
	private List<Tuple<? extends ITimeInterval>> expectedOutput;
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public List<Tuple<? extends ITimeInterval>> getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(List<Tuple<? extends ITimeInterval>> expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
}
