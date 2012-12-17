/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.database.logicaloperator;

import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * @author Dennis Geesen
 * 
 */
public abstract class AbstractDatabaseOperator extends AbstractLogicalOperator {
	
	private static Logger logger = LoggerFactory.getLogger(DatabaseSourceAO.class);

	private static final long serialVersionUID = -7206781357206560261L;
	private IDatabaseConnection dabasaeconnection;
	
	private String jdbc = "";
	private String password = "";
	private String user = "";
	private String type = "";
	private String db = "";
	private int port = -1;
	private String host;
	private String connectionName;
	private boolean lazyConnectionCheck = true;

	private boolean lazyConnCheck;

	public AbstractDatabaseOperator() {

	}

	public AbstractDatabaseOperator(AbstractDatabaseOperator ds) {		
		this.jdbc = ds.jdbc;
		this.password = ds.password;
		this.user = ds.user;
		this.type = ds.type;
		this.db = ds.db;
		this.port = ds.port;
		this.host = ds.host;
		this.connectionName = ds.connectionName;
		this.lazyConnectionCheck = ds.lazyConnectionCheck;
	}

	@Parameter(type = StringParameter.class, name = "jdbc", optional = true)
	public void setJDBC(String jdbc) {
		this.jdbc = jdbc;
	}

	@Parameter(type = StringParameter.class, name = "user", optional = true)
	public void setUser(String user) {
		this.user = user;
	}

	@Parameter(type = StringParameter.class, name = "password", optional = true)
	public void setPassword(String password) {
		this.password = password;
	}

	@Parameter(type = StringParameter.class, name = "type", optional = true)
	public void setType(String type) {
		this.type = type;
	}

	@Parameter(type = StringParameter.class, name = "db", optional = true)
	public void setDB(String db) {
		this.db = db;
	}

	@Parameter(type = StringParameter.class, name = "host", optional = true)
	public void setHost(String host) {
		this.host = host;
	}

	@Parameter(type = StringParameter.class, name = "connection", optional = true)
	public void setConnectionName(String connection) {
		this.connectionName = connection;
	}

	@Parameter(type = IntegerParameter.class, name = "port", optional = true)
	public void setPort(int port) {
		this.port = port;
	}
	
	@Parameter(type = BooleanParameter.class, name = "lazy_connection_check", optional = true)
	public void setLazyConnCheck(boolean lazyConnCheck) {
		this.lazyConnCheck = lazyConnCheck;
	}
	
	
	public IDatabaseConnection getConnection(){
		return this.dabasaeconnection;
	}
	
	
		
	@Override
	public void initialize() {
		super.initialize();
		// there a 3 possibilities:
		// 1. we reuse an already created connection
		if (!this.connectionName.isEmpty()) {
			createWithReusingConnection();
		} else {
			// 2. we create a jdbc connection
			if (!this.jdbc.isEmpty()) {
				createJDBCConnection();
			} else {
				// 3. we build a new connection
				creatingNewConnection();
			}
		}
	}

	private void creatingNewConnection() {

	}

	private void createJDBCConnection() {
		Properties connectionProps = new Properties();
		if (!this.user.isEmpty()) {
			connectionProps.put("user", user);
		}
		if (!this.user.isEmpty()) {
			connectionProps.put("password", password);
		}
		this.dabasaeconnection = new DatabaseConnection(this.jdbc, connectionProps);
	}

	private void createWithReusingConnection() {
		this.dabasaeconnection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(connectionName);
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (!this.connectionName.isEmpty()) {
			if (DatabaseConnectionDictionary.getInstance().isConnectionExisting(connectionName)) {
				if (!this.host.isEmpty()) {
					logger.warn("Host is ignored when existing connection is used!");
				}
				if (this.port == -1) {
					logger.warn("Port is ignored when existing connection is used!");
				}
				if (!this.type.isEmpty()) {
					logger.warn("Type is ignored when existing connection is used!");
				}
				if (!this.user.isEmpty()) {
					logger.warn("User is ignored when existing connection is used!");
				}
				if (!this.password.isEmpty()) {
					logger.warn("Password is ignored when existing connection is used!");
				}								
			} else {
				addError(new IllegalParameterException("Database connection with name \"" + connectionName + "\" not found!"));
				isValid = false;
			}
		} else {
			if (!this.jdbc.isEmpty()) {
				if (!this.host.isEmpty()) {
					logger.warn("Host is ignored when JDBC string is used!");
				}
				if (this.port == -1) {
					logger.warn("Port is ignored when JDBC string is used!");
				}
				if (!this.type.isEmpty()) {
					logger.warn("Type is ignored when JDBC string is used!");
				}
				if (!this.connectionName.isEmpty()) {
					logger.warn("Connection name is ignored when JDBC string is used!");
				}				
			} else {								
				// do we have everything for building a connection?
				if (this.type.isEmpty()) {
					isValid = false;
					addError(new IllegalParameterException("type must be set"));
				} else {
					Set<String> names = DatabaseConnectionDictionary.getInstance().getConnectionFactoryNames();
					if (!names.contains(this.type)) {
						isValid = false;
						addError(new IllegalParameterException("type must be a valid value, one of: " + names));
					}
				}
				if (this.db.isEmpty()) {
					isValid = false;
					addError(new IllegalParameterException("db (the database where you want to connect to) must be set"));
				}
				if ((!this.password.isEmpty()) && this.user.isEmpty()) {
					logger.warn("there is a password but no user, root is used!");
				}
				
			}
			if(!this.lazyConnCheck){
				IDatabaseConnection connection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(connectionName);
				try {
					connection.checkProperties();
				} catch (SQLException e) {
					addError(e);
					isValid = false;
				}
			}			
		}
		return isValid;
	}
}
