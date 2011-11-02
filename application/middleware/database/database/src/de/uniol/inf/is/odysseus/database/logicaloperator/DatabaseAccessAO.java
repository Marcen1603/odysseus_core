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

import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen Created at: 28.10.2011
 */
@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DATABASESOURCE")
public class DatabaseAccessAO extends AbstractLogicalOperator implements OutputSchemaSettable {
	
	

	private static final long serialVersionUID = -5800479007184861697L;
	private boolean timesensitiv = false;
	protected SDFSource source = null;

	private SDFAttributeList outputSchema;

	private IDatabaseConnection connection;

	private String tablename;

	public DatabaseAccessAO(SDFSource source, IDatabaseConnection connection, String tablename, boolean timesensitiv) {
		this.source = source;
		this.connection = connection;
		this.tablename = tablename;
		this.timesensitiv = timesensitiv;

	}

	public DatabaseAccessAO(DatabaseAccessAO original) {
		this.source = original.source;
		this.connection = original.connection;
		this.timesensitiv = original.timesensitiv;
		this.tablename = original.tablename;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	public IDatabaseConnection getConnection() {
		return connection;
	}

	public String getTablename() {
		return tablename;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseAccessAO(this);
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema.clone();
	}

	public boolean isTimesensitiv() {
		return timesensitiv;
	}

	public SDFElement getSource() {
		return this.source;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		if (port == 0) {
			setOutputSchema(outputSchema);
		} else {
			throw new IllegalArgumentException("no such port: " + port);
		}

	}
}
