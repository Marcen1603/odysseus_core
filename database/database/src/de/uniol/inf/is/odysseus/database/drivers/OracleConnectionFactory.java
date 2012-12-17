package de.uniol.inf.is.odysseus.database.drivers;

import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

public class OracleConnectionFactory extends AbstractDatabaseConnectionFactory {

	@Override
	public IDatabaseConnection createConnection(String server, int port, String database, String user, String password) {
		Properties connectionProps = getCredentials(user, password);
		String connString = "jdbc:oracle:thin:@" + server + ":" + port + ":" + database;
		return new DatabaseConnection(connString, connectionProps);
	}

}
