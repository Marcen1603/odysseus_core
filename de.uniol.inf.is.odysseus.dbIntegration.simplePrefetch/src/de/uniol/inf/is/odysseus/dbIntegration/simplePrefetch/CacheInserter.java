package de.uniol.inf.is.odysseus.dbIntegration.simplePrefetch;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


/**
 * Der CacheInserter ermoeglicht eine parallele Ausfuehrung von Datenbankanfragen
 * und der Integration dieser Daten in den Cache waehrend die urspruenglich angeforderten
 * Ergebnisse der aufrufenden Instanz uebergeben werden koennen.
 * 
 * @author crolfes
 *
 */
public class CacheInserter extends Thread {

	
	private List<RelationalTuple<?>> distributedTuples;
	private DBQuery dbQuery;
	private ICache cache;
	private IDataAccess dataAccess;
	
	@Override
	public void run() {
		
		try {
			for (RelationalTuple<?> tuple : distributedTuples) {
				DBResult result = dataAccess.executeBaseQuery(tuple);
				cache.addData(result.getInputTuple(), result.getResult(), dbQuery);
			}
		} catch (Exception e) {
			
		}
	}
	
	
	/**
	 * Innerhalb des Konstruktors wird der Daemon auf true gesetzt. 
	 * Dadurch beendet sich der Thread, sobald die Anweisungen innerhalb
	 * der run-Methode abgearbeitet sind.
	 * @param dbQuery - die Datenbankanfrage
	 * @param distributedTuples - die zusaetzlich berechneten Tupel
	 * @param cache - der Cache
	 * @param dataAccess - die Klasse zum Datenbankzugriff
	 */
	public CacheInserter( DBQuery dbQuery, List<RelationalTuple<?>> distributedTuples, ICache cache, IDataAccess dataAccess) {
		this.setDaemon(true);
		this.dbQuery = dbQuery;
		this.distributedTuples = distributedTuples;
		this.cache = cache;
		this.dataAccess = dataAccess;
	}

}
