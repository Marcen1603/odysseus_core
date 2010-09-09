package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.CacheMissException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Das Interface ICache wird benoetigt, um Cachingservices fuer das Datenbankframework 
 * zu benutzen. Dafuer muss ein OSGi-Service dieses Interface implementieren und an 
 * der Service-Registry anmelden.
 * 
 * @author crolfes
 *
 */
public interface ICache {
	
	/**
	 * Liefert zu einem eingegangenen Datenstromtupel die zugehoerigen Werte.
	 * 
	 * @param streamParam - das eingegangene Datenstromtupel
	 * @param dbQuery - die zugehoerige Datenbankanfrage
	 * @return eine Liste mit RelationalTuple-Objekten, falls die Werte im Cache vorhanden sind.
	 * @return falls nicht, null.
	 * @throws CacheMissException
	 */
	public List<RelationalTuple<?>> getCachedData(RelationalTuple<?> streamParam, DBQuery dbQuery) throws CacheMissException;
	
	
	/**
	 * Mit dieser Methode koennen dem Cache Tupel hinzugefuegt werden.
	 * 
	 * @param streamParam - das eingegangene Datenstromtupel
	 * @param inputData - die Ergbnis-Daten
	 * @param dbQuery - die zugehoerige Datenbankanfrage
	 */
	public void addData(RelationalTuple<?> streamParam, List<RelationalTuple<?>> inputData, DBQuery dbQuery);
	
	
	/**
	 * Mit Hilfe von addQuery wird dem Cache eine Datenbankanfrage hinzugefuegt. 
	 * 
	 * @param dbQuery - die Datenbankanfrage
	 * @param queryPrefs - eventuelle Konfigurationseinstellungen
	 * @param dataAccess - ein DataAccess-Objekt zum Datenbankzugriff
	 */
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs, IDataAccess dataAccess); 
	
	
	/**
	 * Mit dieser Methode wird eine Datenbankanfrage zusammen mit allen zugehoerigen Ergebnissen 
	 * aus dem Cache entfernt. 
	 * 
	 * @param dbQuery - die zu entfernende Datenbankanfrage
	 */
	public void closeQuery(DBQuery dbQuery);
	
	
	/**
	 * Prueft, ob zu einem RelationalTuple<?>-Objekt Daten im Cache enthalten sind.
	 * 
	 * @param dbQuery - die Datenbankanfrage, zu der das Tupel gehoert
	 * @param streamParam - das Tupel, zu dem geprueft werden soll
	 * @return true - falls Daten vorhanden
	 * 			false - falls keine Daten vorhanden.
	 */
	public boolean dataCached(DBQuery dbQuery, RelationalTuple<?> streamParam);
	
}
