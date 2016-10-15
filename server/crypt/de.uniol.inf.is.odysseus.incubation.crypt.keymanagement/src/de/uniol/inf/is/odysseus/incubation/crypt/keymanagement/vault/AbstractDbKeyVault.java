package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;

/**
 * Abstract class for any KeyVault, stored in a Database.<br>
 * This abstract class handles the connection to the database and some necessary
 * fields.
 * 
 * @author MarkMilster
 *
 */
public abstract class AbstractDbKeyVault {

	private Configuration credentials;
	public static final String type = "MYSQL";

	protected Connection conn;
	protected String keyTableName;

	/**
	 * Constructor, which uses credentials, stored in a Configuration file, to
	 * connect to a database.
	 * 
	 * @param path
	 *            Path to the Configuration file, with connection-credentials to
	 *            the database.
	 */
	public AbstractDbKeyVault(String path) {
		try {
			this.credentials = new PropertiesConfiguration(path);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
		// TODO show a exception to the user, if you could not connect to mysql
		IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getFactory(type);
		IDatabaseConnection dbconn = factory.createConnection(this.credentials.getString("server"),
				this.credentials.getInt("port"), this.credentials.getString("database"),
				this.credentials.getString("user"), this.credentials.getString("password"));
		try {
			this.conn = dbconn.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.keyTableName = this.credentials.getString("keyTableName");
	}

}
