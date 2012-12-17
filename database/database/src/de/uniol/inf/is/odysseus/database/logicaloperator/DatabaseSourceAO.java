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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DATABASESOURCE")
public class DatabaseSourceAO extends AbstractDatabaseOperator {
	
	private static final long serialVersionUID = -5800479007184861697L;

	private boolean timesensitiv = false;
	private String tablename;
	private long waitMillis = 0;
	private String name;

	public DatabaseSourceAO() {

	}

	public DatabaseSourceAO(DatabaseSourceAO ds) {
		super(ds);
		this.timesensitiv = ds.timesensitiv;
		this.tablename = ds.tablename;
		this.waitMillis = ds.waitMillis;
		this.name = ds.name;
		
	}

	@Parameter(type = StringParameter.class, name = "table")
	public void setTableName(String tableName) {
		this.tablename = tableName;
	}

	
	@Parameter(type = LongParameter.class, name ="waiteach", optional = true)
	public void setWaitInMillis(long waitMillis){
		this.waitMillis = waitMillis;
	}
	
	@Parameter(type = StringParameter.class, name ="sourcename")
	public void setSourceName(String name){
		this.name = name;
	}

	public String getSourceName(){
		return this.name;
	}

	public String getTableName() {
		return tablename;
	}	

	public boolean isTimesensitiv() {
		return timesensitiv;
	}

	public long getWaitMillis() {
		return this.waitMillis;
	}

	

	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSourceAO(this);
	}

	/**
	 * @param isTimeSensitive
	 */
	public void setTimeSensitive(boolean isTimeSensitive) {
		this.timesensitiv = isTimeSensitive;		
	}

}
