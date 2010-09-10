package de.uniol.inf.is.odysseus.dbIntegration.model;

/**
 * Die Klasse DBQuery kapselt Informationen zu einer Datenbankanfrage.
 * @author crolfes
 *
 */
public class DBQuery {

	
	private String database;
	private String sqlQuery;
	private boolean isUpdate;
	
	
	public DBQuery(String database, String baseQuery, boolean isUpdate) {
		super();
		this.isUpdate = isUpdate;
		this.database = database;
		this.sqlQuery = baseQuery;
	}
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getQuery() {
		return sqlQuery;
	}
	public void setQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}
	public boolean isUpdate() {
		return isUpdate;
	}
	
	
}
