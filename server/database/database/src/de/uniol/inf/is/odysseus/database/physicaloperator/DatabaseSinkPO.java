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

package de.uniol.inf.is.odysseus.database.physicaloperator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 * @author Marco Grawunder
 */
public class DatabaseSinkPO extends AbstractSink<Tuple<ITimeInterval>> {

	private static Logger logger = LoggerFactory
			.getLogger(DatabaseSinkPO.class);

	private Connection jdbcConnection;
	private PreparedStatement preparedStatement;
	private IDataTypeMappingHandler<?>[] dtMappings;

	private int counter = 1;
	final private IDatabaseConnection connection;
	final private String tablename;
	final private boolean truncate;
	final private boolean drop;
	private volatile boolean opened = false;
	final private long batchSize;

	final private List<String> tableSchema;

	public DatabaseSinkPO(IDatabaseConnection connection, String tablename,
			boolean drop, boolean truncate, long batchSize,
			List<String> tableSchema) {
		this.connection = connection;
		this.tablename = tablename;
		this.truncate = truncate;
		this.drop = drop;
		this.batchSize = batchSize;
		this.tableSchema = tableSchema;
	}

	public DatabaseSinkPO(DatabaseSinkPO databaseSinkPO) {
		this.connection = databaseSinkPO.connection;
		this.tablename = databaseSinkPO.tablename;
		this.drop = databaseSinkPO.drop;
		this.truncate = databaseSinkPO.truncate;
		this.batchSize = databaseSinkPO.batchSize;
		this.tableSchema = databaseSinkPO.tableSchema;
	}

	private void initDTMappings() {
		SDFSchema outputSchema = getOutputSchema();
		dtMappings = new IDataTypeMappingHandler<?>[outputSchema.size() + 1];
		for (int i = 0; i < outputSchema.size(); i++) {
			dtMappings[i + 1] = DatatypeRegistry.getDataHandler(outputSchema
					.get(i).getDatatype());
		}

	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (dtMappings == null) {
			initDTMappings();
		}
		try {
			if (!this.connection.tableExists(tablename)) {
				this.connection.createTable(tablename, getOutputSchema(),
						tableSchema);
			} else {
				if (this.drop) {
					dropTable();
					this.connection.createTable(tablename, getOutputSchema(),
							tableSchema);
				} else {
					if (this.truncate) {
						truncateTable();
					}
				}
			}
			this.jdbcConnection = this.connection.getConnection();
			this.preparedStatement = this.jdbcConnection
					.prepareStatement(createPreparedStatement());
			this.jdbcConnection.setAutoCommit(false);
			this.counter = 0;
			opened = true;
		} catch (Exception e) {
			e.printStackTrace();
			opened = false;
			throw new OpenFailedException(e);
		}

	}

	private void dropTable() {
		try {
			this.connection.dropTable(this.tablename);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void truncateTable() {
		try {
			this.connection.truncateTable(this.tablename);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String createPreparedStatement() {
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
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	// private void calcLatency(Tuple<ITimeInterval> tuple) {
	// long start = tuple.getAttribute(0);
	// long diff = System.currentTimeMillis() - start;
	// summe = summe + diff;
	// if ((counter % 1000) == 0) {
	// System.out.println("Bei " + counter + " Elementen:");
	// System.out.println(" - Total: " + summe);
	// System.out.println(" - Avg: " + ((double) summe / (double) counter));
	// }
	//
	// }

	@Override
	protected void process_next(Tuple<ITimeInterval> tuple, int port) {
		if (!opened) {
			System.err.println("Error: not connected to database");
			return;
		}
		if (isOpen()) {
			try {

				for (int i = 0; i < this.getOutputSchema().size(); i++) {
					Object attributeValue = tuple.getAttribute(i);
					dtMappings[i + 1].setValue(this.preparedStatement, i + 1,
							attributeValue);
				}
				// for (SDFAttribute attribute : this.getOutputSchema()) {
				// SDFDatatype datatype = attribute.getDatatype();
				// Object attributeValue = tuple.getAttribute(i);
				// IDataTypeMappingHandler<?> handler = DatatypeRegistry
				// .getDataHandler(datatype);
				// handler.setValue(this.preparedStatement, i + 1,
				// attributeValue);
				// i++;
				// }
				this.preparedStatement.addBatch();
				counter++;
				if ((counter % batchSize) == 0) {
					writeToDB();
				}
				// calcLatency(tuple);
				// logger.debug("Inserted "+count+" rows in database");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void writeToDB() throws SQLException {
		int count = this.preparedStatement.executeBatch().length;
		this.jdbcConnection.commit();
		logger.debug("Inserted " + count + " rows in database");
	}

	@Override
	protected void process_close() {
		try {
			writeToDB();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DatabaseSinkPO clone() {
		return new DatabaseSinkPO(this);
	}

}
