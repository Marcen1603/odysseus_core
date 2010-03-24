package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;


import java.util.LinkedList;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;

/**
 * Stellt eine Datenstromanfrage innerhalb des Caches dar. Zu dieser werden das 
 * Datenbankobjekt und deine Liste mit den Hashwerten von Ergebnissen gespeichert.
 * Die Ergebnisse selbst liegen in der CacheService-Klasse. Des Weiteren wird zu jeder 
 * Query vermerkt, wie lange sie im Cache verbleiben kann und wie groß die zugehoerigen 
 * Ein- und Ausgabetupel sind. Diese Groessenwerte haengen von der Methode memSize der Klasse
 * RelationalObject ab.
 * 
 * @author crolfes
 *
 */
public class CacheQuery extends DBQuery{

	//key = ResultKey.hashCode
	private LinkedList<Integer> dbResults;
	private IDataAccess dataAccess;
	
	//die Groesse eines eingehenden Datenstromtupels
	private long tupelSize = -1;
	
	//die Groesse eines einzigen Tupels
	private long resultTupelSize = -1;
	private long maxTimeInCache;
	
	/**
	 * Zur Initialisierung werden die SQL-Anweisung der Datenstromanfrage, ein Datenbankobjekt
	 * und die Zeit, wie lange ein Objekt im Cache verweilen darf, benoetigt.
	 * 
	 * @param dbQuery - die Datenbankanfrage
	 * @param dal - das zugehoerige Datenbankobjekt
	 * @param maxTimeInCache - Verweildauer von Ergebnissen im Cache
	 */
	public CacheQuery(DBQuery dbQuery, IDataAccess dal, long maxTimeInCache) {
		super(dbQuery.getDatabase(), dbQuery.getQuery(), dbQuery.isUpdate());
		this.dataAccess = dal;
		dbResults = new LinkedList<Integer>();
		this.maxTimeInCache = maxTimeInCache;
	}
	
	/**
	 * Setzt die Groesse von eingehenden Tupeln.
	 * @param size - die neue Groesse
	 */
	public void setTupelSize(long size) {
		this.tupelSize = size;
	}
	
	/**
	 * Setzt die Groesse von Ergebnisstupeln.
	 * @param size - die Groesse
	 */
	public void setResultTupelSize(long size) {
		this.resultTupelSize = size;
	}
	
	public long getResultTupleSize() {
		return resultTupelSize;
	}
	
	public LinkedList<Integer> getDBResults() {
		return dbResults;
	}
	
	public void addParam(Integer resultHash) {
		dbResults.add(resultHash);
	}
	
	public void removeResultHash(Integer resultHash) {
		dbResults.remove(resultHash);
	}
	
	public IDataAccess getDataAccess() {
		return dataAccess;
	}
	
	public long getTupelSize() {
		return tupelSize;
	}
	
	public long getMaxTimeInCache() {
		return maxTimeInCache;
	}	
}
