package de.uniol.inf.is.odysseus.fastflowerdelivery.io;

import java.sql.DriverManager;
import java.sql.Statement;

import java.sql.Connection;

/**
 * Used to execute simple queries on the mysql server.
 * As it is mostly used in web services, this class does not need to
 * establish a permanent connection.
 * A connection is established on each request and closed right after
 * executing the query.
 *
 * @author Weert Stamm
 * @version 1.0
 */
public class MysqlConnection {

	/**
	 * The configuration to use to connect to the database
	 */
	private MysqlConfiguration config = null;
	
	
	/**
	 * Singleton implementation
	 */
	public synchronized static MysqlConnection getInstance() {
		if(instance == null)
			instance = new MysqlConnection();
		return instance;
	}
	private static MysqlConnection instance = null;
	private MysqlConnection() {}
	
	/**
	 * Registers an configuration
	 * @param config
	 * 			the config containing the credentials
	 */
	public synchronized void registerConfiguration(MysqlConfiguration config) {
		this.config = config;
	}
	
	/**
	 * Establishes a connection to the database
	 * @return the connection object
	 */
	private Connection createConnection() {
		try {
			return DriverManager.getConnection("jdbc:mysql://"+config.getAddress()+":"+config.getPort()+"/"+config.getDatabase()+"?user="+config.getUsername()+"&password="+config.getPassword());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	/**
	 * Returns true if the query delivered a ResultSet
	 * @param sql
	 * 			the query to check for
	 * @return true if the query delivered a ResultSet, false otherwise
	 */
	public synchronized boolean existence(String sql) {
		boolean result = false;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = createConnection();
		    stmt = conn.createStatement();

	        stmt.execute(sql);
	        result = stmt.getResultSet().next();
	        
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		    if (stmt != null)
		        try {
		            stmt.close();
		        } catch (Exception ex) {}
		    if(conn != null)
		    	try {
					conn.close();
				} catch (Exception ex) {}
		}
		    
		return result;
	}
	
	/**
	 * Executes a query that does not need to return anything.
	 * Used only to create new data in the database, though it
	 * could be used for any other query without return statement.
	 * @param sql
	 * 			the query
	 */
	public synchronized void create(String sql) {
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = createConnection();
		    stmt = conn.createStatement();

	        stmt.execute(sql); 
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		    if (stmt != null)
		        try {
		            stmt.close();
		        } catch (Exception ex) {}
		    if(conn != null)
		    	try {
					conn.close();
				} catch (Exception ex) {}
		}
	}
}




