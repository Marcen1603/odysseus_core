package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class QueryParam {
	
	private RelationalTuple<?> dataStreamParam;
	private LinkedList<RelationalTuple<?>> results;
	private long timeInCache;
	//DBQuery.hashCode
	private Integer queryHash;
	
	public QueryParam(RelationalTuple<?> dataStreamParam, LinkedList<RelationalTuple<?>> results, 
			long timeInCache, Integer queryHash) {
		this.results = results;
		this.dataStreamParam = dataStreamParam;
		this.timeInCache = timeInCache;
		this.queryHash = queryHash;
		
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
	
	public long getTimeInCache() {
		return timeInCache;
	}

	
	public Integer getQueryHash() {
		return queryHash;
	}
}
