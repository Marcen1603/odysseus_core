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

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.TimestampToPayloadAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateDatabaseConnection;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSink;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSinkOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseTimeSensitiv;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTStreamToStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTime;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSourceAO;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public class DatabaseVisitor extends CQLParser {

	@Override
	public void setUser(User user) {
		super.setUser(user);
	}

	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		super.setDataDictionary(dataDictionary);
	}

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) throws QueryParseException {
		String name = (String)data;
		String connectionName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String tableName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		boolean isTimeSensitive = false;
		long waitMillis = 0L;
		if(node.jjtGetNumChildren()>2){
			if(node.jjtGetChild(2) instanceof ASTDatabaseTimeSensitiv){
				isTimeSensitive = true;
			}
			if(node.jjtGetChild(2) instanceof ASTTime){
				ASTTime time = (ASTTime)node.jjtGetChild(2);
				String value = time.jjtGetValue().toString();
				waitMillis = Long.parseLong(value);
			}
		}		

		IDatabaseConnection connection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(connectionName);
		if(connection == null){
			throw new QueryParseException("No connection with name \""+connectionName+"\" found. You have to create one first");
		}
		if(!connection.tableExists(tableName)){
			throw new QueryParseException("Table \""+tableName+"\" does not exist!");
		}
		
		DatabaseSourceAO source = new DatabaseSourceAO(new SDFSource(name, "DatabaseAccesAO"), connection, tableName, isTimeSensitive, waitMillis);		
		getDataDictionary().setStream(name, source, getCaller());
		return source;		

	}

	@Override
	public Object visit(ASTDatabaseSink node, Object data) throws QueryParseException {
		String sinkName = (String) data;

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
		DatabaseSinkAO sinkAO = new DatabaseSinkAO(sinkName, connectionName, tableName, drop, truncate);
		ILogicalOperator transformMeta = new TimestampToPayloadAO();
		sinkAO.subscribeToSource(transformMeta, 0, 0, null);
		getDataDictionary().addSink(sinkName, sinkAO);
		return null;
	}

	@Override
	public Object visit(ASTStreamToStatement node, Object data) throws QueryParseException {
		DatabaseSinkAO sink = (DatabaseSinkAO) data;
		String name = sink.getConnectionName();
		SDFAttributeList schema = sink.getOutputSchema();
		IDatabaseConnection con = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(name);
		if (!con.equalSchemas(sink.getTablename(), schema)) {
			throw new QueryParseException("Columns between database and datastream are not equal!");
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
		String dbms = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		String dbname = ((ASTIdentifier) node.jjtGetChild(2)).getName();
		String host = "localhost";
		int port = -1;
		String user = "";
		String pass = "";
		int offset = 0;
		if (node.jjtGetChild(3) instanceof ASTHost) {
			host = ((ASTHost) node.jjtGetChild(3)).getValue();
			port = ((ASTInteger) node.jjtGetChild(4)).getValue().intValue();
			offset = 2;
		}
		if (node.jjtGetChild(3 + offset) instanceof ASTIdentifier) {
			user = ((ASTIdentifier) node.jjtGetChild(3 + offset)).getName();
			pass = ((ASTIdentifier) node.jjtGetChild(4 + offset)).getName();
		}
		// check if type is supported
		IDatabaseConnectionFactory factory = DatabaseConnectionDictionary.getInstance().getFactory(dbms);
		if (factory == null) {
			String currentInstalled = "";
			String sep = "";
			for (String n : DatabaseConnectionDictionary.getInstance().getConnectionFactoryNames()) {
				currentInstalled = currentInstalled + sep + n;
				sep = ", ";
			}
			throw new QueryParseException("DBMS \"" + dbms + "\" not supported! Currently available: " + currentInstalled);
		}

		try {
			IDatabaseConnection con = factory.createConnection(host, port, dbname, user, pass);
			DatabaseConnectionDictionary.getInstance().addConnection(connectionName, con);

		} catch (SQLException e) {
			throw new QueryParseException("Testing connection to database failed: " + e.getLocalizedMessage());
		}

		return null;
	}

}
