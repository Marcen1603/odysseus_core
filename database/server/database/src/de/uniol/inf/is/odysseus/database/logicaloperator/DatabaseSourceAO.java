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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 * @author Tobias Brandt (metadata) 
 */ 
@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "DATABASESOURCE", doc = "This operator can read data from a relational database.", category = {
		LogicalOperatorCategory.SOURCE, LogicalOperatorCategory.DATABASE })
public class DatabaseSourceAO extends AbstractDatabaseOperator {

	private static final long serialVersionUID = -5800479007184861697L;
	private String tablename;
	private long waitMillis = 0;  
	private boolean fetchAttributes = true;
	private boolean escapeNames = false;
	private List<SDFAttribute> givenSchema;
	private boolean useDatatypeMappings = true;
 
	public DatabaseSourceAO() { 

	}

	public DatabaseSourceAO(DatabaseSourceAO ds) { 
		super(ds);
		this.tablename = ds.tablename;
		this.waitMillis = ds.waitMillis;
		this.escapeNames = ds.escapeNames;
		this.useDatatypeMappings = ds.useDatatypeMappings;
	}

	@Parameter(type = StringParameter.class, name = "table")
	public void setTableName(String tableName) {
		this.tablename = tableName;
	}

	@Parameter(type = LongParameter.class, name = "waiteach", optional = true)
	public void setWaitInMillis(long waitMillis) {
		this.waitMillis = waitMillis;
	}

	@Parameter(type = CreateSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true, optional = true)
	public void setOutputSchemaWithList(List<SDFAttribute> outputSchema) {
		this.givenSchema = outputSchema;
		this.fetchAttributes = false;
		setOutputSchema(SDFSchemaFactory.createNewTupleSchema("", outputSchema));
	}

	@Parameter(type = BooleanParameter.class, name = "escape_names", optional = true)
	public void setEscapeNames(boolean escapeNames) {
		this.escapeNames = escapeNames;
	}

	public boolean isEscapeNames() {
		return escapeNames;
	}

	@Parameter(type = BooleanParameter.class, name = "Use_Datatype_Mappings", optional = true)
	public void setUseDatatypeMappings(boolean useDatatypeMappings) {
		this.useDatatypeMappings = useDatatypeMappings;
	}

	public boolean isUseDatatypeMappings() {
		return useDatatypeMappings;
	}

	public List<SDFAttribute> getOutputSchemaWithList() {
		return this.givenSchema;
	}

	@Parameter(type = BooleanParameter.class, name = "FETCH_ATTRIBUTES", optional = true)
	public void setFetchAttributes(boolean fetch) {
		this.fetchAttributes = fetch;
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
		if (this.givenSchema == null && this.fetchAttributes == false) {
			addError(
					"You have to either use ATTRIBUTES to define a schema or to use FETCH_ATTRIBUTES to invoke fetching the schema from the database");
			valid = false;
		}
		return valid;
	}

	@Override
	public void initialize() {
		super.initialize();

		// The schema we aim to fill
		SDFSchema schema = null;

		// The main part of the schema
		if (this.fetchAttributes) {
			// We want to get the schema from the database
			try {
				IDatabaseConnection conn = getConnection();
				if (conn != null) {
					schema = getConnection().getSchema(tablename);

					if (schema == null) {
						throw new SQLException("Error reading schema. Does table " + tablename + " exist?");
					}
				} else {
					throw new SQLException("No connection to " + getConnectionName() + "!");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// The user entered a schema
			schema = SDFSchemaFactory.createNewTupleSchema("", this.givenSchema);
		}

		// The meta-attributes of the schema

		if (schema != null) {

			// Use the metadata schema from the query
			IMetaAttribute metaAttribute = getMetaAttribute();
			if (metaAttribute != null) {
				List<SDFMetaSchema> metaSchema = metaAttribute.getSchema();
				schema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
			}
			// Finally, set the output schema
			this.setOutputSchema(schema);
		}
	}

	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
