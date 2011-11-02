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

package de.uniol.inf.is.odysseus.database.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary.DatabaseType;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 */
public class DatabaseSinkPO extends AbstractSink<RelationalTuple<ITimeInterval>> {

	private static Logger logger = LoggerFactory.getLogger(DatabaseSinkPO.class);
	private IDatabaseConnection connection;
	private Connection jdbcConnection;
	private PreparedStatement preparedStatement;

	

	private int counter = 1;
	private long summe = 0L;
	private String connctionName;
	private String tablename;
	private boolean truncate;
	private boolean drop;

	public DatabaseSinkPO(String connectionName,String tablename, boolean drop, boolean truncate) {		
		this.connctionName = connectionName;
		this.tablename = tablename;	
		this.truncate = truncate;
		this.drop = drop;

	}

	public DatabaseSinkPO(DatabaseSinkPO databaseSinkPO) {		
		this.connctionName = databaseSinkPO.connctionName;
		this.tablename = databaseSinkPO.tablename;		
		this.drop = databaseSinkPO.drop;
		this.truncate = databaseSinkPO.truncate;
	}

	

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		try {			
			this.connection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(connctionName);
			if (!this.connection.tableExists(tablename)) {
				this.connection.createTable(tablename, getOutputSchema());
			} else {
				if (this.drop) {
					dropTable();
					this.connection.createTable(tablename, getOutputSchema());
				}else{
					if(this.truncate){
						truncateTable();
					}
				}
			}
			this.jdbcConnection = this.connection.getConnection();
			this.preparedStatement = this.jdbcConnection.prepareStatement(createPreparedStatement());
			this.jdbcConnection.setAutoCommit(false);
			this.counter = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenFailedException(e);
		}
	}

	private void dropTable() {
		this.connection.dropTable(this.tablename);
	}
	
	private void truncateTable() {
		this.connection.truncateTable(this.tablename);
	}

	private String createPreparedStatement() {
		// String s = "INSERT INTO  \"GEESEN\".\"testtable\" VALUES(";
		String s = "INSERT INTO " + this.tablename + " VALUES(";
		int count = super.getOutputSchema().size();
		String sep = "";
		for (int i = 0; i < count; i++) {
			s = s + sep + "?";
			sep = ",";
		}
		s = s + ")";
		return s;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
	}

	private void calcLatency(RelationalTuple<ITimeInterval> tuple) {
		long start = tuple.getAttribute(0);
		long diff = System.currentTimeMillis() - start;
		summe = summe + diff;
		if ((counter % 1000) == 0) {
			System.out.println("Bei " + counter + " Elementen:");
			System.out.println(" - Total: " + summe);
			System.out.println(" - Avg: " + ((double) summe / (double) counter));
		}

	}

	@Override
	protected void process_next(RelationalTuple<ITimeInterval> tuple, int port, boolean isReadOnly) {
		try {
			int i = 0;

			for (SDFAttribute attribute : this.getOutputSchema()) {
				SDFDatatype datatype = attribute.getDatatype();
				Object attributeValue = tuple.getAttribute(i);
				DatabaseType mapping = DatabaseConnectionDictionary.getInstance().getDatabaseType(datatype);
				switch (mapping) {
				case Boolean:
					this.preparedStatement.setBoolean(i + 1, (Boolean) attributeValue);
					break;
				case Integer:
					this.preparedStatement.setInt(i + 1, (Integer) attributeValue);
					break;
				case Double:
					this.preparedStatement.setDouble(i + 1, (Double) attributeValue);
					break;
				case Float:
					this.preparedStatement.setFloat(i + 1, (Float) attributeValue);
					break;
				case Long:
					this.preparedStatement.setLong(i + 1, (Long) attributeValue);
					break;
				case String:
					this.preparedStatement.setString(i + 1, (String) attributeValue);
					break;
				}
				i++;
			}
			this.preparedStatement.addBatch();
			counter++;
			if ((counter % 10) == 0) {
				int count = this.preparedStatement.executeBatch().length;
				this.jdbcConnection.commit();
				logger.debug("Inserted " + count + " rows in database");
			}
			calcLatency(tuple);
			// logger.debug("Inserted "+count+" rows in database");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public DatabaseSinkPO clone() {
		return new DatabaseSinkPO(this);
	}

}
