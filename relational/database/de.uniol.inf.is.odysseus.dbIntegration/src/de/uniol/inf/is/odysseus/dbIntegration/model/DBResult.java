package de.uniol.inf.is.odysseus.dbIntegration.model;

import java.util.List;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class DBResult {

	
	private List<RelationalTuple<?>> result;
	
	
	
	public DBResult(List<RelationalTuple<?>> result) {
		super();
		this.result = result;
	}
	
	public List<RelationalTuple<?>> getResult() {
		return result;
	}
	public void setResult(List<RelationalTuple<?>> result) {
		this.result = result;
	}
	
	
}
