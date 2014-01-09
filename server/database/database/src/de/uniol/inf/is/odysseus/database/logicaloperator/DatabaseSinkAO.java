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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen
 * Created at: 20.10.2011
 */
@LogicalOperator(name = "DATABASESINK", minInputPorts = 1, maxInputPorts = 1, doc="This operator can write data to a relational database.",category={LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE})
public class DatabaseSinkAO extends AbstractDatabaseOperator{
	
	private static final long serialVersionUID = -6513851256783092870L;
	
	private String tablename;		
	private boolean truncate;
	private boolean drop;
	private long batchSize = 10;
	
	public DatabaseSinkAO(){
		super();
	}

	public DatabaseSinkAO(DatabaseSinkAO old) {
		super(old);				
		this.tablename = old.tablename;		
		this.drop = old.drop;
		this.truncate = old.truncate;
		this.batchSize = old.batchSize;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSinkAO(this);
	}

	
	public String getTablename() {
		return this.tablename;
	}
	
	public boolean isDrop() {
		return this.drop;
	}
	
	public boolean isTruncate(){
		return this.truncate;
	}
	

	@Parameter(name = "TABLE", type = StringParameter.class, optional = false, doc="Name of store table")
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}		

	@Parameter(name = "TRUNCATE", type = BooleanParameter.class, optional = true, doc="Empty table at start")
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	@Parameter(name = "DROP", type = BooleanParameter.class, optional = true , doc="Drop table at start")
	public void setDrop(boolean drop) {
		this.drop = drop;
	}	
	
	@Parameter(name = "BatchSize", type = LongParameter.class, optional = true, doc="How many elements should be buffered before storing to database.")
	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}
	
	public long getBatchSize() {
		return batchSize;
	}
	
	@Override
	public boolean isSource() {
		return false;
	}
	
}
