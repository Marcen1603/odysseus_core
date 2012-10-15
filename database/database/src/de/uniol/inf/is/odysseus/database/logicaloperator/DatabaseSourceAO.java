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

package de.uniol.inf.is.odysseus.database.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DATABASESOURCE")
public class DatabaseSourceAO extends AbstractLogicalOperator{
	
	

	private static final long serialVersionUID = -5800479007184861697L;
	private boolean timesensitiv = false;

	private IDatabaseConnection connection;

	private String tablename;
	private long waitMillis;
	private String name;

	public DatabaseSourceAO(String name, IDatabaseConnection connection, String tablename, boolean timesensitiv, long waitMillis) {
		this.name = name;
		this.connection = connection;
		this.tablename = tablename;
		this.timesensitiv = timesensitiv;
		this.waitMillis = waitMillis;

	}

	public DatabaseSourceAO(DatabaseSourceAO original) {
		this.name = original.name;
		this.connection = original.connection;
		this.timesensitiv = original.timesensitiv;
		this.tablename = original.tablename;
		this.waitMillis = original.waitMillis;
	}

	public IDatabaseConnection getConnection() {
		return connection;
	}

	public String getTableName() {
		return tablename;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSourceAO(this);
	}

	public boolean isTimesensitiv() {
		return timesensitiv;
	}

	public String getSourceName() {
		return this.name;
	}

	public long getWaitMillis(){
		return this.waitMillis;
	}

}
