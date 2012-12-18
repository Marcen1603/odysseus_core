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

import java.sql.SQLException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "DATABASESOURCE")
public class DatabaseSourceAO extends AbstractDatabaseOperator {
	
	private static final long serialVersionUID = -5800479007184861697L;
	private String tablename;
	private long waitMillis = 0;
	private boolean fetchAttributes = true;
	private List<SDFAttribute> givenSchema;	

	public DatabaseSourceAO() {

	}

	public DatabaseSourceAO(DatabaseSourceAO ds) {
		super(ds);		
		this.tablename = ds.tablename;
		this.waitMillis = ds.waitMillis;
	}

	@Parameter(type = StringParameter.class, name = "table")
	public void setTableName(String tableName) {
		this.tablename = tableName;
	}

	
	@Parameter(type = LongParameter.class, name ="waiteach", optional = true)
	public void setWaitInMillis(long waitMillis){
		this.waitMillis = waitMillis;
	}		
	
	@Parameter(type = CreateSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		this.givenSchema  = outputSchema;
		this.fetchAttributes = false;
		setOutputSchema(new SDFSchema("", outputSchema));
	}
	
	@Parameter(type = BooleanParameter.class, name="FETCH_ATTRIBUTES", optional = true)
	public void setFetchAttributes(boolean fetch){
		this.fetchAttributes  = fetch;
	}

	public String getTableName() {
		return tablename;
	}	

	public long getWaitMillis() {
		return this.waitMillis;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSourceAO(this);
	}
	
	@Override
	public boolean isValid() {	
		boolean valid = super.isValid();
		if(this.givenSchema == null && this.fetchAttributes == false){
			addError(new IllegalParameterException("You have to either use ATTRIBUTES to define a schema or to use FETCH_ATTRIBUTES to invoke fetching the schema from the database"));
			valid = false;
		}		
		return valid;
	}
	
	@Override
	public void initialize() {	
		super.initialize();
		if(this.fetchAttributes){
			try {
				SDFSchema schema = getConnection().getSchema(tablename);
				this.setOutputSchema(schema);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


}
