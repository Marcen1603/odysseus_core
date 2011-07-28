/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.storing.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Kai Pancratz
 *
 */
public class DatabaseSinkPO extends AbstractSink<Object> {

	private Connection connection;
	private String table;
	
	private boolean savemetadata;
	private boolean create;
	private boolean ifnotexists;
	private boolean truncate;
	private boolean opened;
	
	//Make the BATCH SIZE CONFIGERABLE 
	private static final int SELECT_BATCH_SIZE = 10;
	
	private Queue<RelationalTuple<?>> values = new LinkedList<RelationalTuple<?>>();
	private PreparedStatement preparedStatement;

	private volatile static Logger LOGGER = LoggerFactory.getLogger(DatabaseSinkPO.class);;

	
	public DatabaseSinkPO(DatabaseSinkPO databaseSinkPO) {
		super();
		this.connection = databaseSinkPO.connection;
		this.table = databaseSinkPO.table;
		this.savemetadata = databaseSinkPO.savemetadata;
		this.create = databaseSinkPO.create;
		this.ifnotexists = databaseSinkPO.ifnotexists;
		this.truncate = databaseSinkPO.truncate;
		this.opened = databaseSinkPO.opened;
		LOGGER.debug("Instance of DatabaseSinkPO: " + " Connection:" + this.connection + " Table:" + this.table + " SaveMetaData:" + this.savemetadata + " Create:" + this.create + " Ifnotexists:" + this.ifnotexists);
	}
	
	public DatabaseSinkPO(Connection connection, String table,
			boolean savemetadata, boolean create, boolean ifnotexists,
			boolean truncate) {
		super();
		this.connection = connection;
		this.table = table;
		this.savemetadata = savemetadata;
		this.create = create;
		this.ifnotexists = ifnotexists;
		this.truncate = truncate;
		LOGGER.debug("Instance of DatabaseSinkPO: " + " Connection:" + this.connection + " Table:" + this.table + " SaveMetaData:" + this.savemetadata + " Create:" + this.create     );
	}


	@Override
	protected void process_open() throws OpenFailedException {
		this.opened = true;
		// CREATE TABLE
		if (create) {
			// IF NOT EXISTS
			if (ifnotexists) {
				// also, not exists
				if (!tableExists()) {
					createTable();
				} else {
					// nothing, because only create if NOT exists!
//					dropTable();
//					createTable();
				}
			}else{
				dropTable();
				createTable();
			}
		} else {
			if (truncate) {
				truncate();
			}
		}

		if (checkDatabaseValidity()) {
			try {
				String ps = "INSERT INTO " + table + " (";

				int count = 0;
				String del = "";
				for (SDFAttribute a : this.getOutputSchema()) {
					ps = ps + del + a.getAttributeName();
					count++;
					del = ", ";
				}
				ps = ps + ") VALUES (";
				del = "";
				for (int i = 0; i < count; i++) {
					ps = ps + del + "?";
					del = ", ";
				}
				ps = ps + ")";
				System.out.println("insert statement created: " + ps);
				preparedStatement = this.connection.prepareStatement(ps);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException(
					"Table already exists, but its schema does not match the query schema! Use CREATE option for dropping the old table and recreating a new table.");
		}

	}

	private boolean tableExists() {		
		try {
			this.connection.createStatement().execute("SELECT count(*) FROM "+table);
			return true;
		} catch (SQLException e) {
			return false;
		}		
	}

	private boolean checkDatabaseValidity() {
		return true;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		LOGGER.error("Method processPunctuation is not implemented.");
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {
		if(!opened){
			LOGGER.warn("Not opened.");
			try {
				process_open();
				LOGGER.debug("open.");
			} catch (OpenFailedException e) {				
				LOGGER.error("OpenFailedException",e.getStackTrace());
			}
		}
		
		synchronized (values) {
			values.offer((RelationalTuple)object);
			if (values.size() >= SELECT_BATCH_SIZE) {
				//LOGGER.debug("Values " + values);
				writeToDatabase(values);
			}
		}
	}

	private void writeToDatabase(Queue<RelationalTuple<?>> queue) {
		try {
			while (!queue.isEmpty()) {
				RelationalTuple<?> tuple = queue.poll();
				for (int i = 1; i <= tuple.getAttributeCount(); i++) {
					preparedStatement.setObject(i, tuple.getAttribute(i - 1));
				}
				if(savemetadata){
					tuple.getMetadata();
					
				}
				preparedStatement.addBatch();
			}
			preparedStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DatabaseSinkPO clone() {
		return new DatabaseSinkPO(this);
	}

	private void createTable() {
		Statement statememt;
		try {
			statememt = this.connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		String query = "CREATE TABLE " + this.table + "(";
		String delimiter = "";
		for (SDFAttribute attribute : getOutputSchema()) {
			String name = attribute.getAttributeName();
			SDFDatatype type = attribute.getDatatype();
			String typeString = getSQLType(type);
			query += delimiter + name + " " + typeString;
			delimiter = ", ";
		}
		query = query + ")";
		LOGGER.debug("Create Database: " + query);
		
		try {
			statememt.execute(query);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	private void dropTable() {
		if(tableExists()){			
			try {
				String query = "DROP TABLE " + table;
				System.out.println("Dropping table: " + query);
				Statement s = this.connection.createStatement();
				s.execute(query);
			} catch (SQLException e1) {
				System.err.println("Error while dropping table "+table);
			}
		}
	}

	private void truncate() {
		dropTable();
		createTable();
	}

	private String getSQLType(SDFDatatype type) {
		if (type.getURI().equals(SDFDatatype.DOUBLE)) {
			return "DOUBLE";
		}
		if (type.isDate()) {
			return "DATE";
		}
		if (type.getURI().equals(SDFDatatype.INTEGER)) {
			return "INTEGER";
		}
		if (type.getURI().equals(SDFDatatype.STRING)) {
			return "VARCHAR(255)";
		}
		if (type.getURI().equals(SDFDatatype.LONG)) {
			return "BIGINT";
		}
		if (type.getURI(false).toUpperCase().endsWith("TIMESTAMP")){
			return "BIGINT";
		}
		return "VARCHAR(255)";
	}
}
