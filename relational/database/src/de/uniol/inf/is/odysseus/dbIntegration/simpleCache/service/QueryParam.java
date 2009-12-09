package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class QueryParam {
	
	private RelationalTuple<?> dataStreamParam;
	private LinkedList<RelationalTuple<?>> results;
	private long maxTime;
	private long time;
	
	public QueryParam(RelationalTuple<?> dataStreamParam, LinkedList<RelationalTuple<?>> results, long maxTime) {
		this.results = results;
		this.dataStreamParam = dataStreamParam;
		this.maxTime = maxTime;
		this.time = 0;
		
	}
	
	public LinkedList<RelationalTuple<?>> getResults() {
		return results;
	}
	
	public RelationalTuple<?> getDataStreamParam() {
		return dataStreamParam;
	}
	
//	public void setMaxTime(long maxTime) {
//		this.maxTime = maxTime;
//	}
	
	public long getMaxTime() {
		return maxTime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
