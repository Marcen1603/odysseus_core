/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.database.cql;

import java.sql.SQLException;
import java.util.Properties;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateSinkCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSourceAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseConnectionCheck;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSink;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSinkOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDropDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTJDBCConnection;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTStreamToStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTime;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public class DatabaseVisitor extends CQLParser {

	@Override
	public void setUser(ISession user) {
		super.setUser(user);
	}

	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		super.setDataDictionary(dataDictionary);
	}

	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		super.setMetaAttribute(metaAttribute);
	}

	@Override
	public Object visit(ASTDropDatabaseConnection node, Object data) throws QueryParseException {
		String connectionName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		DatabaseConnectionDictionary.removeConnection(connectionName);
		return null;
	}

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) throws QueryParseException {
		String name = (String) data;
		String connectionName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String tableName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		long waitMillis = 0L;
		if (node.jjtGetNumChildren() > 2) {
			if (node.jjtGetChild(2) instanceof ASTTime) {
				ASTTime time = (ASTTime) node.jjtGetChild(2);
				String value = time.jjtGetValue().toString();
				waitMillis = Long.parseLong(value);
			}
		}

		// name, connection, tableName, isTimeSensitive, waitMillis
		DatabaseSourceAO source = new DatabaseSourceAO();
		source.setName(name);
		source.setConnectionName(connectionName);
		source.setTableName(tableName);
		source.setWaitInMillis(waitMillis);
		if (!source.isValid()) {
			if (source.getErrors().size() > 0) {
				throw new QueryParseException("Source not correctly set" + source.getErrors().get(0));
			} else {
				throw new QueryParseException("Source not correctly set. Check connection and parameters!");
			}
		}
		source.initialize();
		CreateStreamCommand cmd = new CreateStreamCommand(name, source, getCaller());
		// try {
		// getDataDictionary().setStream(name, source, getCaller());
		// } catch (DataDictionaryException e) {
		// throw new QueryParseException(e.getMessage());
		// }
		return cmd;

	}

	@Override
	public Object visit(ASTDatabaseSink node, Object data) throws QueryParseException {
		String name = (String) data;
		String connectionName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String tableName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		boolean drop = false;
		boolean truncate = false;
		if (node.jjtGetNumChildren() > 2) {
			if (node.jjtGetChild(2) instanceof ASTDatabaseSinkOptions) {
				ASTDatabaseSinkOptions options = (ASTDatabaseSinkOptions) node.jjtGetChild(2);
				String value = (String) options.jjtGetValue();
				if (value.trim().toUpperCase().equals("DROP")) {
					drop = true;
				}
				if (value.trim().toUpperCase().equals("TRUNCATE")) {
					truncate = true;
				}
			}
		}

		// all checks passed (no exception) --> generate AO
		DatabaseSinkAO sinkAO = new DatabaseSinkAO();
		// sinkName, connectionName, tableName, drop, truncate
		sinkAO.setConnectionName(connectionName);
		sinkAO.setTablename(tableName);
		sinkAO.setDrop(drop);
		sinkAO.setTruncate(truncate);
		if (!sinkAO.isValid()) {
			if (sinkAO.getErrors().size() > 0) {
				throw new QueryParseException("Source not correctly set" + sinkAO.getErrors().get(0));
			} else {
				throw new QueryParseException("Source not correctly set. Check connection and parameters!");
			}
		}
		sinkAO.initialize();
		CreateSinkCommand command = new CreateSinkCommand(name, sinkAO, getCaller());
		return command;
	}

	@Override
	public Object visit(ASTStreamToStatement node, Object data) throws QueryParseException {
		DatabaseSinkAO sink = (DatabaseSinkAO) data;
		IDatabaseConnection con = sink.getConnection();
		SDFSchema schema = sink.getOutputSchema();
		if (!sink.isDrop()) {
			try {
				if (!con.equalSchemas(sink.getTablename(), schema)) {
					throw new QueryParseException("Columns between database and datastream are not equal!");
				}
			} catch (SQLException e) {
				throw new QueryParseException("Columns between database and datastream are not equal!");
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTDatabaseSinkOptions node, Object data) throws QueryParseException {
		return super.visit(node, data);
	}

	@Override
	public Object visit(ASTCreateDatabaseConnection node, Object data) throws QueryParseException {		
		String connectionName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (DatabaseConnectionDictionary.isConnectionExisting(connectionName)) {
			throw new QueryParseException("Connection with name \"" + connectionName + "\" already exists!");
		}
		try {
			if (node.jjtGetChild(1) instanceof ASTIdentifier) {
				String dbms = ((ASTIdentifier) node.jjtGetChild(1)).getName();
				String dbname = ((ASTIdentifier) node.jjtGetChild(2)).getName();
				String host = "";
				int port = -1;
				String user = "root";
				String pass = "";
				if (node.jjtGetNumChildren() > 3) {
					if (node.jjtGetChild(3) instanceof ASTHost) {
						host = ((ASTHost) node.jjtGetChild(3)).getValue();
						port = ((ASTInteger) node.jjtGetChild(4)).getValue().intValue();
						// Check for the type cause if the user does not give
						// username and password, but gives the option for no
						// lazy connection check, this would throw an error
						if (node.jjtGetNumChildren() > 5 && node.jjtGetChild(5) instanceof ASTIdentifier) {
							user = ((ASTIdentifier) node.jjtGetChild(5)).getName();
							pass = ((ASTIdentifier) node.jjtGetChild(6)).getName();
						}
					} else if (node.jjtGetChild(3) instanceof ASTIdentifier) {
						user = ((ASTIdentifier) node.jjtGetChild(3)).getName();
						pass = ((ASTIdentifier) node.jjtGetChild(4)).getName();
					}
				}

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
			}
			// otherwise, we have a JDBC based connection
			if (node.jjtGetChild(1) instanceof ASTJDBCConnection) {
				// it's a JDBC String
				ASTJDBCConnection jdbc = (ASTJDBCConnection) node.jjtGetChild(1);
				String str = jdbc.jjtGetValue().toString();
				// 2 and 3 are username and pass
				Properties props = new Properties();
				if (node.jjtGetNumChildren() > 3) {
					String user = ((ASTIdentifier) node.jjtGetChild(2)).getName();
					String pass = ((ASTIdentifier) node.jjtGetChild(3)).getName();
					props.setProperty("user", user);
					props.setProperty("password", pass);
				}
				IDatabaseConnection connection = new DatabaseConnection(str, props);
				DatabaseConnectionDictionary.addConnection(connectionName, connection);
			}
		} catch (Exception e) {
			throw new QueryParseException("Error creating connection", e);
		}		
		
		// is check option used?
		if (node.jjtGetChild(node.jjtGetNumChildren() - 1) instanceof ASTDatabaseConnectionCheck) {
			// check options
			IDatabaseConnection con = DatabaseConnectionDictionary.getDatabaseConnection(connectionName);
			try {
				con.checkProperties();
			} catch (SQLException e) {
				throw new QueryParseException("Check for database connection failed: " + e.getMessage(), e);
			}

		}

		return null;
	}

}
