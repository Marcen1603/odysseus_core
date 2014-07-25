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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.database.connection.DatatypeRegistry;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.physicaloperator.access.IDataTypeMappingHandler;

/**
 * 
 * @author Dennis Geesen Created at: 02.11.2011
 */
public class DatabaseSourcePO extends AbstractSource<Tuple<?>> {
	
	Logger LOG = LoggerFactory.getLogger(DatabaseSourcePO.class);

	final private IDatabaseConnection connection;
	final private String tablename;
	private Connection jdbcConnection;
	private PreparedStatement preparedStatement;
	private TransferThread thread;
	final private long waitTimeMillis;
	final private boolean escapeNames;
	private IDataTypeMappingHandler<?>[] dtMappings;
	final boolean useDtMapper;

	public DatabaseSourcePO(String tableName, IDatabaseConnection connection,
			long waitTimeMillis, boolean escapeNames, boolean useDtMapper) {
		super();
		this.tablename = tableName;
		this.connection = connection;
		this.waitTimeMillis = waitTimeMillis;
		this.useDtMapper = useDtMapper;
		this.escapeNames = escapeNames;
	}

	public DatabaseSourcePO(DatabaseSourcePO databaseSourcePO) {
		super(databaseSourcePO);
		this.connection = databaseSourcePO.connection;
		this.tablename = databaseSourcePO.tablename;
		this.waitTimeMillis = databaseSourcePO.waitTimeMillis;
		this.escapeNames = databaseSourcePO.escapeNames;
		this.useDtMapper = databaseSourcePO.useDtMapper;
	}

	private void initDTMappings() {
		if (useDtMapper) {
			SDFSchema outputSchema = getOutputSchema();
			dtMappings = new IDataTypeMappingHandler<?>[outputSchema.size() + 1];
			for (int i = 0; i < outputSchema.size(); i++) {
				dtMappings[i + 1] = DatatypeRegistry
						.getDataHandler(outputSchema.get(i).getDatatype());
			}
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			if (useDtMapper && dtMappings == null) {
				initDTMappings();
			}
			this.jdbcConnection = this.connection.getConnection();
			this.preparedStatement = this.jdbcConnection
					.prepareStatement(createPreparedStatement());
			this.preparedStatement.setFetchSize(100);
			this.jdbcConnection.setAutoCommit(false);
			this.thread = new TransferThread();
			this.thread.start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new OpenFailedException(e);
		}

	}

	private String createPreparedStatement() {
		String s = "SELECT ";
		String sep = "";
		String escape = escapeNames ? "\"" : "";
		for (SDFAttribute a : this.getOutputSchema()) {

			String name = escape + a.getAttributeName() + escape;
			s = s + sep + name;
			sep = ", ";
		}
		s = s + " FROM " + escape + tablename + escape;
		return s;
	}

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new DatabaseSourcePO(this);
	}

	@Override
	protected void process_close() {
		super.process_close();
		thread.interrupt();
	}

	private class TransferThread extends Thread {

		@Override
		public void run() {
			// TODO: why the min waiting time of 10 ms?
			long waitTime = waitTimeMillis == 0?10:waitTimeMillis;
			try {
				ResultSet rs = preparedStatement.executeQuery();
				int count = rs.getMetaData().getColumnCount();

				while (rs.next() && !interrupted()) {
					List<Object> attributes = new ArrayList<Object>();
					for (int i = 1; i <= count; i++) {
						if (useDtMapper) {
							try {
								attributes.add(dtMappings[i].getValue(rs, i));
							} catch (Exception e) {
								LOG.error("Error translating element"+e.getMessage());
								attributes.add(null);
							}
						} else {
							attributes.add(rs.getObject(i));
						}
					}
					Tuple<?> t = new Tuple<IMetaAttribute>(
							attributes.toArray(), false);
					transfer(t);
					sleep(waitTime);
				}
				propagateDone();
			} catch (SQLException e) {
				e.printStackTrace();
				interrupt();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
	}

}
