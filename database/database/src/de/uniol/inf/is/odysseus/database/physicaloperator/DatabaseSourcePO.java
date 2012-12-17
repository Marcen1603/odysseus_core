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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 02.11.2011
 */
public class DatabaseSourcePO extends AbstractSource<Tuple<?>> {

	private IDatabaseConnection connection;
	private String tablename;
	private boolean timesensitive;
	private Connection jdbcConnection;
	private PreparedStatement preparedStatement;
	private TransferThread thread;
	private long waitTimeMillis;

	public DatabaseSourcePO(String tableName, IDatabaseConnection connection, boolean timesensitive, long waitTimeMillis) {
		super();
		this.tablename = tableName;
		this.connection = connection;
		this.timesensitive = timesensitive;
		this.waitTimeMillis  = waitTimeMillis;
	}

	public DatabaseSourcePO(DatabaseSourcePO databaseSourcePO) {
		super(databaseSourcePO);
		this.connection = databaseSourcePO.connection;
		this.tablename = databaseSourcePO.tablename;
		this.timesensitive = databaseSourcePO.timesensitive;
		this.waitTimeMillis = databaseSourcePO.waitTimeMillis;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {

			this.jdbcConnection = this.connection.getConnection();
			this.preparedStatement = this.jdbcConnection.prepareStatement(createPreparedStatement());
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
		for (SDFAttribute a : this.getOutputSchema()) {
			String name = "`"+a.getAttributeName()+"`";
			s = s + sep + name;
			sep = ", ";
		}
		s = s + " FROM `" + tablename + "`";
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
			super.run();					
			if(waitTimeMillis==0){
				waitTimeMillis = 10;
			}
			try {
				ResultSet rs = preparedStatement.executeQuery();
				int count = rs.getMetaData().getColumnCount();
				List<Object> attributes = new ArrayList<Object>();
				while (rs.next() && !interrupted()) {
					for (int i = 1; i <= count; i++) {
						attributes.add(rs.getObject(i));
					}
					Tuple<?> t = new Tuple<IMetaAttribute>(attributes.toArray(), false);
					transfer(t);
					attributes.clear();
					sleep(waitTimeMillis);
				}
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
