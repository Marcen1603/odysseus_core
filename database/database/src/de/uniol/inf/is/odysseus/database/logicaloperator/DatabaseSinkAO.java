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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen
 * Created at: 20.10.2011
 */
@LogicalOperator(name = "DATABASESINK", minInputPorts = 1, maxInputPorts = 1)
public class DatabaseSinkAO extends AbstractDatabaseOperator{
	
	private static final long serialVersionUID = -6513851256783092870L;
	
	private String tablename;	
	private String sinkName;
	private boolean truncate;
	private boolean drop;
	
	public DatabaseSinkAO(){
		super();
	}

	public DatabaseSinkAO(DatabaseSinkAO old) {
		super(old);		
		this.sinkName = old.getSinkName();
		this.tablename = old.tablename;		
		this.drop = old.drop;
		this.truncate = old.truncate;
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

	public String getSinkName() {
		return sinkName;
	}

	@Parameter(name = "TABLE", type = StringParameter.class, optional = false)
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}	

	@Parameter(name = "SINKNAME", type = StringParameter.class, optional = false)
	public void setSinkName(String sinkName) {
		this.sinkName = sinkName;
	}

	@Parameter(name = "TRUNCATE", type = BooleanParameter.class, optional = true)
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

	@Parameter(name = "DROP", type = BooleanParameter.class, optional = true)
	public void setDrop(boolean drop) {
		this.drop = drop;
	}	
	
	

}
