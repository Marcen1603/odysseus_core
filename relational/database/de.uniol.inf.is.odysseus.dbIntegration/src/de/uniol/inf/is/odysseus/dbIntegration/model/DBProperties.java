package de.uniol.inf.is.odysseus.dbIntegration.model;


public class DBProperties {

	private String database;
	private String url;
	private String driverClass;
	private String user;
	private String password;
	private String schema;
	
	

	
	public DBProperties(String database, String url, String driverClass,
			String user, String password, String schema) {
		this.database = database;
		this.url = url;
		this.driverClass = driverClass;
		this.user = user;
		this.password = password;
		this.schema = schema;
	}
	
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	
	
}
