package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.PrefetchException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Das Interface IPrefech wird benoetigt, um Prefetchingservices fuer das Datenbankframework 
 * zu benutzen. Dafuer muss ein OSGi-Service dieses Interface implementieren und an 
 * der Service-Registry anmelden.
 * 
 * @author crolfes
 *
 */
public interface IPrefetch {
	
	/**
	 * Mit dieser Methode kann die Prefetchingkomponente um Ergebnisse zu 
	 * einem Datenstromtupel angefragt werden.
	 * 
	 * @param streamTuple - das eingehende Datenstromtupel
	 * @param dbQuery - die Datenbankanfrage
	 * @return eine Liste mit Ergebnissen
	 * @throws PrefetchException falls es zu einem Fehler innerhalb der Komponente
	 * kommt, kann diese Exception geworfen werden.
	 */
	public List<RelationalTuple<?>> getData(RelationalTuple<?> streamTuple, DBQuery dbQuery) throws PrefetchException;
	
	
	/**
	 * Ermoeglicht das Hinzufuegen einer Datenbankanfrage zur Prefetchingkomponente.
	 * 
	 * @param dbQuery - die Datenbankanfrage
	 * @param queryPrefs - optionale Einstellungen zur Prefetchingkomponente, welche Moeglichkeiten es gibt, 
	 * haengt von der jeweiligen Komponente ab.
	 * @param dataAccess - die Datenbankschnittstelle
	 * @param inputSchema - das Schema der eingehenden Tupel
	 * @param cache - der verwendete Cache
	 */
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs, IDataAccess dataAccess, SDFAttributeList inputSchema, ICache cache); 
	
	
	/**
	 * Schliessen einer Anfrage
	 * 
	 * @param dbQuery - die Datenbankanfrage, die geschlossen werden soll.
	 */
	public void closeQuery(DBQuery dbQuery);
	
	
	/**
	 * Methode zum Hinzufuegen von Datenstromtupeln, mit deren Hilfe die Prefetchingkomponente 
	 * Muster lernen kann.
	 * 
	 * @param dbQuery - die Datenbankanfrage
	 * @param streamTuple - das eingehende Datenstromtupel
	 */
	public void addDataStreamTuple(DBQuery dbQuery, RelationalTuple<?> streamTuple);
}
