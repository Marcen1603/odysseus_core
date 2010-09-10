package de.uniol.inf.is.odysseus.dbIntegration.model;

/**
 * Buendelt Informationen zum Zugriff auf Datenbanken.
 * @author crolfes
 *
 */
public class DBProperties {

	//das Schema der Datenbank. 
	private String database;
	
	//in der Form: jdbc:mysql://localhost:3306/auction
	private String url;
	
	//bspw: com.mysql.jdbc.Driver
	private String driverClass;
	
	private String user;
	private String password;
	
	

	
	public DBProperties(String database, String url, String driverClass,
			String user, String password) {
		this.database = database;
		this.url = url;
		this.driverClass = driverClass;
		this.user = user;
		this.password = password;
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
	
	
	
}
