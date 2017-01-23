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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.server.store.StoreRegistry;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 * @author Marco Grawunder
 */

public class DatabaseSinkPO extends AbstractSink<Tuple<?>> implements ActionListener {

	final private static Logger LOG = LoggerFactory.getLogger(DatabaseSinkPO.class);
	final private static InfoService INFO = InfoServiceFactory.getInfoService(DatabaseSinkPO.class);

	private Connection jdbcConnection;
	private String preparedStatementString;
	final private PreparedStatementHandler preparedStatementHandler;
	List<Tuple<?>> toStore = new ArrayList<Tuple<?>>();
	private IDataTypeMappingHandler<?>[] dtMappings;

	private int counter = 0;
	final private IDatabaseConnection connection;
	final private String tablename;
	final private boolean truncate;
	final private boolean drop;
	private volatile boolean opened = false;
	final private long batchSize;
	final private int batchTimeout;
	private Timer timer = null;
	private boolean recoveryEnabled = false;
	private String recoveryStoreType;
	private OptionMap recoveryStoreOptions;
	private IStore<Long, Tuple<?>> recoveryStore;

	final private List<String> tableSchema;
	final private List<String> primaryKeys;
	private boolean firstConnectionFailed;
	private boolean recovering;
	private boolean useAttributeNames;

	public DatabaseSinkPO(IDatabaseConnection connection, String tablename, boolean drop, boolean truncate,
			long batchSize, int batchTimeout, List<String> tableSchema, List<String> primaryKeys,
			String preparedStatement, boolean jdcbSupportsBatch) {
		if (connection == null) {
			throw new IllegalArgumentException("Connection must not be null");
		}
		this.connection = connection;
		this.tablename = tablename;
		this.truncate = truncate;
		this.drop = drop;
		this.batchSize = batchSize;
		if (batchSize == -1 || !jdcbSupportsBatch) {
			preparedStatementHandler = new PreparedStatementHandler(false);
		} else {
			preparedStatementHandler = new PreparedStatementHandler(true);
		}
		this.batchTimeout = batchTimeout;
		this.tableSchema = tableSchema;
		this.preparedStatementString = preparedStatement;
		this.primaryKeys = primaryKeys;
	}

	private void initDTMappings() {
		SDFSchema outputSchema = getOutputSchema();
		dtMappings = new IDataTypeMappingHandler<?>[outputSchema.size() + 1];
		for (int i = 0; i < outputSchema.size(); i++) {
			dtMappings[i + 1] = DatatypeRegistry.getDataHandler(outputSchema.get(i).getDatatype());
		}
		if (batchTimeout > 0) {
			timer = new Timer(batchTimeout, this);
			timer.start();
		}
	}

	public void setRecoveryEnabled(boolean recoveryEnabled, String recoveryStoreType, OptionMap recoveryStoreOptions) {
		this.recoveryEnabled = recoveryEnabled;
		this.recoveryStoreType = recoveryStoreType;
		this.recoveryStoreOptions = new OptionMap(recoveryStoreOptions);
	}

	public boolean isRecoveryEnabled() {
		return recoveryEnabled;
	}

	public void setUseAttributeNames(boolean useAttributeNames) {
		this.useAttributeNames = useAttributeNames;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		recovering = false;
		toStore.clear();
		if (recoveryEnabled) {
			try {
				recoveryStore = (IStore<Long, Tuple<?>>) StoreRegistry.createStore(recoveryStoreType,
						recoveryStoreOptions);
				if (recoveryStore == null) {
					throw new OpenFailedException(
							"Cannot create " + recoveryStoreType + " with " + recoveryStoreOptions);
				}
				if (recoveryStore.size() > 0) {
					recovering = true;
				}
			} catch (StoreException e) {
				throw new OpenFailedException(e);
			}
		}

		if (dtMappings == null) {
			initDTMappings();
		}
		try {

			initDBConnection();
			if (jdbcConnection == null || jdbcConnection.isClosed()) {
				firstConnectionFailed = true;
			} else {
				initTable();
			}
			this.counter = 0;
			opened = true;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			opened = false;
			throw new OpenFailedException(e);
		}

	}

	private void initTable() throws SQLException {

		if (!this.connection.tableExists(tablename)) {
			this.connection.createTable(tablename, getOutputSchema(), tableSchema, primaryKeys);
		} else {
			// Do not clear table, if operator is recovering from former
			// state!
			if (!recovering) {
				if (this.drop) {
					dropTable();
					this.connection.createTable(tablename, getOutputSchema(), tableSchema, primaryKeys);
				} else {
					if (this.truncate) {
						truncateTable();
					}
				}
			}
		}

	}

	private void initDBConnection() {
		try {
			this.jdbcConnection = this.connection.getConnection();
			if (jdbcConnection != null) {
				if (jdbcConnection.isClosed()) {
					return;
				}
				if (this.preparedStatementHandler.getPreparedStatement() == null) {
					if ((this.preparedStatementString == null) || ("".equals(this.preparedStatementString))) {
						this.preparedStatementHandler.setPreparedStatement(
								this.jdbcConnection.prepareStatement(createInsertPreparedStatement()));
					} else {
						this.preparedStatementHandler.setPreparedStatement(
								this.jdbcConnection.prepareStatement(this.preparedStatementString));
					}
				}
				this.jdbcConnection.setAutoCommit(false);
			}
		} catch (Exception e) {
			INFO.warning("Database error. Will retry ...", e);
		}

	}

	private void dropTable() {
		try {
			this.connection.dropTable(this.tablename);
			LOG.debug("DROPED TABLE " + this.tablename);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
			INFO.error(e.getMessage(), e);
		}
	}

	private void truncateTable() {
		try {
			this.connection.truncateTable(this.tablename);
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	private String createInsertPreparedStatement() {
		int count = super.getOutputSchema().size();
		SDFSchema outputSchema = super.getOutputSchema();

		StringBuffer s = new StringBuffer("INSERT INTO " + this.tablename);

		String sep = "";
		if (useAttributeNames) {
			s.append("(");
			for (int i = 0; i < count; i++) {
				s.append(sep).append(outputSchema.getAttribute(i).getAttributeName());
				sep = ",";
			}
			s.append(")");
		}
		s.append(" VALUES(");

		sep = "";
		for (int i = 0; i < count; i++) {
			s.append(sep).append("?");
			sep = ",";
		}
		s.append(")");
		LOG.trace("Prepared statement: {}", s.toString());
		return s.toString();
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		if (!opened) {
			LOG.error("Error: not connected to database");
			return;
		}
		if (isOpen()) {
			if (recoveryEnabled) {
				// Check if there are some elements in the recovery store
				// that not have been send to the database
				while (recoveryStore.size() > counter) {
					// TODO: Process RecoveryStore
					// 1.Removed elements that are already stored?

					// 2. Add remaining elements to storeList
					List<Entry<Long, Tuple<?>>> values = recoveryStore.getOrderedByKey(batchSize - counter);

					for (int i = 0; i < values.size(); i++) {
						addTupleToStoreBatch(values.get(i).getValue());
					}

				}
				// Keep current element
				// Use Timestamp for order
				recoveryStore.put(System.currentTimeMillis(), tuple.clone());
				recoveryStore.commit();
			}

			addTupleToStoreBatch(tuple);
		}
	}

	private void addTupleToStoreBatch(Tuple<?> tuple) {
		toStore.add(tuple);
		counter++;
		if (counter % batchSize == 0) {
			if (writeToDB()) {
				counter = 0;
			}
		}
	}

	private void prepare(Tuple<?> tuple) throws SQLException {
		for (int i = 0; i < this.getOutputSchema().size(); i++) {
			Object attributeValue = tuple.getAttribute(i);
			if (attributeValue != null) {
				// ToDo:
				dtMappings[i + 1].setValue(this.preparedStatementHandler.getPreparedStatement(), i + 1, attributeValue);
			} else {
				this.preparedStatementHandler.setNull(i + 1, Types.NULL);
			}
		}
	}

	public synchronized boolean writeToDB() {
		int count = 0;

		if (timer != null) {
			timer.restart();
		}

		if (toStore.size() == 0) {
			return true;
		}

		try {
			initDBConnection();

			if (jdbcConnection != null && !jdbcConnection.isClosed()) {

				if (firstConnectionFailed) {
					initTable();
					firstConnectionFailed = false;
				}
				for (int i = 0; i < toStore.size(); i++) {
					prepare(toStore.get(i));
					this.preparedStatementHandler.addBatch();
				}

				boolean batchProcessingFailed = false;
				try {
					count = this.preparedStatementHandler.executeBatch();
				} catch (BatchUpdateException e) {
					preparedStatementHandler.clearBatch();
					e.printStackTrace();
					INFO.warning("Error inserting elements", e);
					batchProcessingFailed = true;
				}

				// Retry with single inserts
				if (batchProcessingFailed) {
					count = 0;
					this.jdbcConnection.rollback();
					for (int i = 0; i < toStore.size(); i++) {
						try {
							prepare(toStore.get(i));
							this.preparedStatementHandler.executeUpdate();
							count++;
						} catch (SQLException e) {
							INFO.warning("Insertion of " + toStore.get(i) + " failed", e);
						}
					}
				}

				LOG.trace("Inserted " + count + " rows in database");

				this.jdbcConnection.commit();
				toStore.clear();

				if (recoveryEnabled) {
					StringBuffer buffer = new StringBuffer();
					recoveryStore.dumpTo(buffer);
					// /System.err.println("COUNT "+count+" STORE
					// "+recoveryStore.size()+" "+buffer);
					if (count >= recoveryStore.size()) {
						recoveryStore.clear();
					} else {
						// TODO: Retrieve elements from target DB to find out,
						// what
						// has been written and remove from recovery store
						throw new RuntimeException("NEEDS IMPLEMENTATION!!");
					}

				}

				return true;
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e, jdbcConnection, connection);

			// INFO.error("ERROR WRTING TO DATABASE " + preparedStatement,
			// e.getNextException());
		}

		return false;
	}

	@Override
	protected void process_close() {
		writeToDB();
		if (timer != null) {
			timer.stop();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		writeToDB();
	}

}
