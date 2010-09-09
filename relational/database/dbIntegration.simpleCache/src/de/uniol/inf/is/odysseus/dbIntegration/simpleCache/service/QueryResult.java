package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Diese Klasse dient der Speicherung von Ergebnisstupeln einer einzelnen Datenbankanfrage.
 * Dazu wird ebenfalls das zugrunde liegende Datenstromtupeln, die Erstellzeit und zum Verweis 
 * auf die Datenbankanfrage der Hash des DBQuery-Objekts.
 * 
 * @author crolfes
 *
 */
public class QueryResult {
	
	private RelationalTuple<?> dataStreamParam;
	private LinkedList<RelationalTuple<?>> results;
	private long timeInCache;
	//DBQuery.hashCode
	private Integer queryHash;
	
	/**
	 * Konstruktor der Klasse.
	 * 
	 * @param dataStreamTuple - das eingehende Datenstromtupel, dieses wurde in die Datenbankanfrage eingefuegt
	 * @param results - eine Liste von Ergebnissen
	 * @param timeInCache - der Zeitpunkt der Erstellung des Ergebnisses
	 * @param queryHash - Hashwert des jeweiligen DBQuery-Objekts
	 */
	public QueryResult(RelationalTuple<?> dataStreamTuple, LinkedList<RelationalTuple<?>> results, 
			long timeInCache, Integer queryHash) {
		this.results = results;
		this.dataStreamParam = dataStreamTuple;
		this.timeInCache = timeInCache;
		this.queryHash = queryHash;
		
	}
	
	public LinkedList<RelationalTuple<?>> getResults() {
		return results;
	}
	
	public RelationalTuple<?> getDataStreamTuple() {
		return dataStreamParam;
	}
	
	public long getTimeInCache() {
		return timeInCache;
	}

	public Integer getQueryHash() {
		return queryHash;
	}
}
