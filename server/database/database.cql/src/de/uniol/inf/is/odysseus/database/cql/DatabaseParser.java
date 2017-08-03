package de.uniol.inf.is.odysseus.database.cql;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Command;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDataBaseConnectionGeneric;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.CreateDataBaseConnectionJDBC;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.DropDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Query;
import de.uniol.inf.is.odysseus.parser.cql2.server.IExtension;

public class DatabaseParser implements IExtension {

	private Map<String, String> databaseConnections = new HashMap<>();

	@Override
	public Object parse(Query command) {
		throw new QueryParseException("this method has no implementation");
	}

	@Override
	public Object parse(Command command) {
		if (command instanceof CreateDataBaseConnectionGeneric) {
			CreateDataBaseConnectionGeneric cast = ((CreateDataBaseConnectionGeneric) command);
			createDatabaseConnection(cast.getName(), cast.getDriver(), cast.getSource(), cast.getHost(), cast.getPort(),
					cast.getUser(), cast.getPassword(), cast.getLazy());
		} else if (command instanceof CreateDataBaseConnectionJDBC) {
			CreateDataBaseConnectionJDBC cast = ((CreateDataBaseConnectionJDBC) command);
			createDatabaseConnection(cast.getName(), null, null, cast.getServer(), -1, cast.getUser(),
					cast.getPassword(), cast.getLazy());
		} else if (command instanceof DropDatabaseConnection) {
			String connectionName = ((DropDatabaseConnection) command).getName();
			if (DatabaseConnectionDictionary.isConnectionExisting(connectionName)) {
				DatabaseConnectionDictionary.removeConnection(connectionName);
				databaseConnections.remove(connectionName);
			}
		}
		return databaseConnections;
	}

	private void createDatabaseConnection(String connectionName, String dbms, String dbname, String host, int port,
			String user, String pass, String lazy) {
		if (DatabaseConnectionDictionary.isConnectionExisting(connectionName))
			throw new QueryParseException("Connection with name \"" + connectionName + "\" already exists!");
		if (user == null) {
			user = "root";
			pass = "";
		}
		try {
			if (dbms != null && dbname != null) {
				IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getFactory(dbms);
				if (factory == null) {
					String currentInstalled = "";
					String sep = "";
					for (String n : DatabaseConnectionDictionary.getConnectionFactoryNames()) {
						currentInstalled = currentInstalled + sep + n;
						sep = ", ";
					}
					throw new QueryParseException(
							"DBMS \"" + dbms + "\" not supported! Currently available: " + currentInstalled);
				}
				IDatabaseConnection con = factory.createConnection(host, port, dbname, user, pass);
				DatabaseConnectionDictionary.addConnection(connectionName, con);
				databaseConnections.put(connectionName, dbms);
			} else// otherwise, we have a JDBC based connection
			{
				Properties props = new Properties();
				props.setProperty("user", user);
				props.setProperty("password", pass);
				IDatabaseConnection connection = new DatabaseConnection(host, props);
				DatabaseConnectionDictionary.addConnection(connectionName, connection);
				databaseConnections.put(connectionName, dbms);
			}
		} catch (Exception e) {
			throw new QueryParseException("Error creating connection", e);
		}

		// is check option used?
		if (lazy != null) {
			IDatabaseConnection con = DatabaseConnectionDictionary.getDatabaseConnection(connectionName);
			try {
				con.checkProperties();
			} catch (SQLException e) {
				throw new QueryParseException("Check for database connection failed: " + e.getMessage(), e);
			}
		}
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void setUser(ISession user) {
	}

	@Override
	public void setDataDictionary(IDataDictionary dd) {
	}

	@Override
	public void setCommands(List<IExecutorCommand> commands) {
	}

	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
	}

}
