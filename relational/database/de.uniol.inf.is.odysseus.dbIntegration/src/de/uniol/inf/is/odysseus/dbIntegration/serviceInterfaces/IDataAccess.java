package de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Das Interface IDataAccess stellt Methoden zum Datenbankzugriff zur Verfuegung.
 * @author crolfes
 *
 */
public interface IDataAccess {

	/**
	 * Definieren einer speziellen Query fuer Cachingkomponenten. Dadurch koennen andere
	 * Bereiche immer noch die urspruengliche Anfrage ausfuehren.
	 * @param dbQuery - die Datenbankanfrage
	 * @param schema - das Schema der eingehenden Tupel
	 */
	public void setCacheQuery(DBQuery dbQuery, SDFAttributeList schema);
	
	
	/**
	 * Definieren einer speziellen Query fuer Prefetchingkomponenten. Dadurch koennen andere
	 * Bereiche immer noch die urspruengliche Anfrage ausfuehren.
	 * @param dbQuery - die Datenbankanfrage
	 * @param schema - das Schema der eingehenden Tupel
	 */
	public void setPrefetchQuery(DBQuery dbQuery, SDFAttributeList schema);
	
	
	/**
	 * Ausfuhren der urspruenglichen Datenbankanfrage
	 * @param tuple - das Datenstromtupel, zu dem Ergebnisse aus der Datenbank geladen werden sollen
	 * @return ein DBResult-Objekt, welches eine Liste mit Ergebnisstupeln und das 
	 * eingegangene Tupel enthaelt 
	 */
	public DBResult executeBaseQuery(RelationalTuple<?> tuple);
	
	
	/**
	 * Ausfuhren der Cache Datenbankanfrage
	 * @param tuple - das Datenstromtupel, zu dem Ergebnisse aus der Datenbank geladen werden sollen
	 * @return ein DBResult-Objekt, welches eine Liste mit Ergebnisstupeln und das 
	 * eingegangene Tupel enthaelt 
	 */
	public DBResult executeCacheQuery(RelationalTuple<?> tuple);
	
	
	/**
	 * Ausfuhren der Prefetch Datenbankanfrage
	 * @param tuple - das Datenstromtupel, zu dem Ergebnisse aus der Datenbank geladen werden sollen
	 * @return ein DBResult-Objekt, welches eine Liste mit Ergebnisstupeln und das 
	 * eingegangene Tupel enthaelt 
	 */
	public DBResult executePrefetchQuery(RelationalTuple<?> tuple);
	
	
	/**
	 * Liefert zu einem Tupel und einem Schema ein Tupel zurueck, das nur die Attribute
	 * enthaelt, welche fuer eine Datenbankanfrage benoetigt werden. 
	 * @param tuple - das verwendete Tupel
	 * @param schema - das Schema.
	 * @return ein Tupel, das nur die Attribute enthaelt, welche fuer die Datenbankanfrage benoetigt werden.
	 */
	public RelationalTuple<?> getRelevantSQLObjects(RelationalTuple<?> tuple, SDFAttributeList schema);
	
	
	/**
	 * Liefert die Attribute der Datenbankanfrage in Form von Strings.
	 * @param dbQuery - die Datenbankanfrage
	 * @return eine Liste von Strings, welche die Attribute einer Datenbankanfrage darstellen.
	 */
	public List<String> getSqlOutputSchema(DBQuery dbQuery);
	
	
	/**
	 * Sucht aus einem uebergebenen SQL-Statement alle Referenzen
	 * zur Datenstromanfrage heraus. Diese beginnen mit "$".
	 * @param sql
	 * @return eine Liste der Referenzen
	 */
	public LinkedList<String> findDataStreamAttr(String sql);
	
}
