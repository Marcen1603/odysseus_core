package de.uniol.inf.is.odysseus.test.set;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class TestSet {
	
	private String name = "";
	private String query;	
	private List<Pair<String, String>> expectedOutput;
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public List<Pair<String, String>> getExpectedOutput() {
		return expectedOutput;
	}

	public void setExpectedOutput(List<Pair<String, String>> expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
}
