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
 * @author MarkMilster
 *
 */
public abstract class AbstractDbKeyVault {

	private Configuration credentials;
	public static final String type = "MYSQL";

	protected Connection conn;
	protected String keyTableName;

	public AbstractDbKeyVault(String path) {
		try {
			this.credentials = new PropertiesConfiguration(path);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
		
		IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getFactory(type);
		IDatabaseConnection dbconn = factory.createConnection(this.credentials.getString("server"), this.credentials.getInt("port"), this.credentials.getString("database"), this.credentials.getString("user"), this.credentials.getString("password"));
		try {
			this.conn = dbconn.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.keyTableName = this.credentials.getString("keyTableName");
	}

}
