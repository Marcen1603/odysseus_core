package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import java.sql.Connection;
import java.sql.SQLException;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
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

	/**
	 * Enum which specifies the type of vault which will be used. With the Enum
	 * there will be used different default configurations.
	 * 
	 * @author MarkMilster
	 *
	 */
	public enum Configuration {
		encSymKeys, pubKeys, symKeys;
	}

	public static final String type = "MYSQL";

	protected Connection conn;
	protected String keyTableName;

	/**
	 * Constructor, which uses credentials, stored in a Configuration file, to
	 * connect to a database.
	 * 
	 * @param configuration
	 *            Configuration of this key vault. This configuration specifies
	 *            connection details.
	 */
	public AbstractDbKeyVault(AbstractDbKeyVault.Configuration configuration) {
		String vault = null;
		switch (configuration) {
		case encSymKeys:
			vault = "crypt.encSymKeyVault.";
			break;
		case pubKeys:
			vault = "crypt.pubKeyVault.";
			break;
		case symKeys:
			vault = "crypt.symKeyVault.";
			break;
		default:
			break;
		}
		IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getFactory(type);
		IDatabaseConnection dbconn = factory.createConnection(OdysseusConfiguration.instance.get(vault + "server"),
				Integer.parseInt(OdysseusConfiguration.instance.get(vault + "port")),
				OdysseusConfiguration.instance.get(vault + "database"), OdysseusConfiguration.instance.get(vault + "user"),
				OdysseusConfiguration.instance.get(vault + "password"));
		try {
			this.conn = dbconn.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.keyTableName = OdysseusConfiguration.instance.get(vault + "keyTableName");

	}
}