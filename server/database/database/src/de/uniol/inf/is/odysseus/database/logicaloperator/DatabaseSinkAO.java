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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Dennis Geesen
 * @author Marco Grawunder Created at: 20.10.2011
 */
@LogicalOperator(name = "DATABASESINK", minInputPorts = 1, maxInputPorts = 1, doc = "This operator can write data to a relational database.", category = {
		LogicalOperatorCategory.SINK, LogicalOperatorCategory.DATABASE })
public class DatabaseSinkAO extends AbstractDatabaseOperator {

	private static final long serialVersionUID = -6513851256783092870L;

	private String tablename;
	private boolean truncate;
	private boolean drop;
	private long batchSize = 10;
	private int batchTimeout = 0;
	private boolean jdbcAllowsBatch = true;
    private String preparedStatement = "";
	private List<String> tableSchema;
	private List<String> primaryKeys;

	// For recovery
	private boolean recoveryEnabled = false;
	private String recoveryStoreType;
	private OptionMap recoveryStoreOptions;

	private boolean useAttributeNames;
	
	public DatabaseSinkAO() {
		super();
	}

	public DatabaseSinkAO(DatabaseSinkAO other) {
		super(other);
		this.tablename = other.tablename;
		this.drop = other.drop;
		this.truncate = other.truncate;
		this.batchSize = other.batchSize;
		this.useAttributeNames = other.useAttributeNames;
		if (other.tableSchema != null) {
			this.tableSchema = new ArrayList<>(other.tableSchema);
		}
		this.batchTimeout = other.batchTimeout;
        this.preparedStatement = other.preparedStatement;
        this.recoveryEnabled = other.recoveryEnabled;
        this.recoveryStoreType = other.recoveryStoreType;
        if (other.recoveryStoreOptions != null){
        	this.recoveryStoreOptions = new OptionMap(other.recoveryStoreOptions);
        }
        if (other.primaryKeys != null){
        	this.primaryKeys = new ArrayList<String>(other.primaryKeys);
        }
        this.jdbcAllowsBatch = other.jdbcAllowsBatch;
	}

	@Override
	public boolean isSourceOperator() {
		return false;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSinkAO(this);
	}

	@Parameter(name = "TABLE", type = StringParameter.class, optional = false, doc = "Name of store table")
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTablename() {
		return this.tablename;
	}
	
	@Parameter(name = "TRUNCATE", type = BooleanParameter.class, optional = true, doc = "Empty table at start")
	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}
	
	public boolean isTruncate() {
		return this.truncate;
	}
		
	@Parameter(name = "useAttributeNames", type = BooleanParameter.class, optional = true, doc = "Use Attributenames in INSERT Statement")
	public void setUseAttributeNames(boolean useAttributeNames) {
		this.useAttributeNames = useAttributeNames;
	}
	
	public boolean isUseAttributeNames() {
		return useAttributeNames;
	}
	
	@Parameter(name = "DROP", type = BooleanParameter.class, optional = true, doc = "Drop table at start")
	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public boolean isDrop() {
		return this.drop;
	}

	@Parameter(name = "BatchSize", type = LongParameter.class, optional = true, doc = "How many elements should be buffered before storing to database.")
	public void setBatchSize(long batchSize) {
		this.batchSize = batchSize;
	}

	public long getBatchSize() {
		return batchSize;
	}
	
	@Parameter(name = "BatchTimeout", type = IntegerParameter.class, optional = true, doc = "If batchsize is set, write tuple after some time (in ms) after last write even if batch is not full.")
	public void setBatchTimeout(int timeout) {
		this.batchTimeout = timeout;
	}

	public int getBatchTimeout() {
		return batchTimeout;
	}

	@Parameter(name = "UseJDBCBatch", type = BooleanParameter.class, optional = true, doc = "Set to false if driver does not support addBatch (e.g. HIVE)")
	public void setJdbcAllowsBatch(boolean jdbcAllowsBatch) {
		this.jdbcAllowsBatch = jdbcAllowsBatch;
	}
	
	public boolean isJdbcAllowsBatch() {
		return jdbcAllowsBatch;
	}
	
	@Parameter(name = "Tableschema", type = StringParameter.class, optional = true, isList = true, doc = "The types of the target database that should be used to create the target table. Order must be the same as the output schema.")
	public void setTableSchema(List<String> tableSchema) {
		this.tableSchema = tableSchema;
	}

	public List<String> getTableSchema() {
		return tableSchema;
	}
	
	@Parameter(name = "PrimaryKeys", type = StringParameter.class, optional = true, isList = true, doc = "The names of the attributes that should be used as primary keys, when creating a new table.")
	public void setPrimaryKeys(List<String> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	public List<String> getPrimaryKeys() {
		return primaryKeys;
	}

    @Parameter(name = "PreparedStatement", type = StringParameter.class, optional = true, doc = "Prepared INSERT statement to use.")
    public void setPreparedStatement(String preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    /**
     * @return the preparedStatement
     */
    public String getPreparedStatement() {
        return this.preparedStatement;
    }


	
	@Parameter(type = OptionParameter.class, name = "recoveryOptions", optional = true, isList = true, doc = "Additional options for recovery.")
	public void setRecoveryStoreOption2(List<Option> value) {
		this.recoveryStoreOptions = new OptionMap(value);
	}
	
	public OptionMap getRecoveryStoreOptions() {
		return recoveryStoreOptions;
	}
	
	
	@Parameter(name = "RecoveryStoreType", type = StringParameter.class, optional = true, isList = false, doc = "Which store should be used for recovery?")
	public void setRecoveryStoreType(String recoveryStoreType) {
		this.recoveryStoreType = recoveryStoreType;
	}

	public String getRecoveryStoreType() {
		return recoveryStoreType;
	}
	
	@Parameter(name = "RecoveryEnabled", type = BooleanParameter.class, optional = true, isList = false, doc = "Should the operator provide mechanisms to allow recovery?")
	public void setRecoveryEnabled(boolean recoveryEnabled) {
		this.recoveryEnabled = recoveryEnabled;
	}
	
	public boolean isRecoveryEnabled(){
		return recoveryEnabled;
	}

	@Override
	public boolean isValid() {
		boolean isValid = super.isValid();
		if (tableSchema != null
				&& tableSchema.size() != getOutputSchema().size()) {
			addError("TableSchema must have the same size as the output schema!");
			isValid = false;
		}
		if (recoveryEnabled){
			if (recoveryStoreType == null || recoveryStoreType.isEmpty()){
				addError("For recovery a recoveryStoreType must be given!");
				isValid = false;
			}
		}
		return isValid;
	}

}
