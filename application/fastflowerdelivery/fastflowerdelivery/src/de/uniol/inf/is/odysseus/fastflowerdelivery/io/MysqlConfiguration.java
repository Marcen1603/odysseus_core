package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

/**
 * A configuration object for connections to the mysql database.
 * On constructing this object will register with the global MysqlConnection class. 
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class MysqlConfiguration {

	/**
	 * The address to the mysql server
	 */
	private String address;
	
	/**
	 * The port of the mysql server
	 */
	private int port;
	
	/**
	 * The user name used to connect to the mysql server
	 */
	private String username;
	
	/**
	 * The password used to connect to the mysql server
	 */
	private String password;
	
	/**
	 * The database to use
	 */
	private String database;
	
	/**
	 * Constructs the object and registers it with the MysqlConnection class.
	 * Used by the Configuration class.
	 */
	public MysqlConfiguration() {
		MysqlConnection.getInstance().registerConfiguration(this);
	}
	
	/**
	 * Constructor to use for default configuration
	 * 
	 * @param address
	 * 			The address to the mysql server
	 * @param port
	 * 			The port of the mysql server
	 * @param username
	 * 			The user name used to connect to the mysql server
	 * @param password
	 * 			The password used to connect to the mysql server
	 * @param database
	 * 			The database to use
	 */
	public MysqlConfiguration(String address, int port, String username, String password, String database) {
		this.address = address;
		this.port = port;
		this.username = username;
		this.password = password;
		this.database = database;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @param database the database to set
	 */
	public void setDatabase(String database) {
		this.database = database;
	}
}
