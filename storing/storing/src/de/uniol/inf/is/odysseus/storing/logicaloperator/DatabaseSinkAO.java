/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.storing.logicaloperator;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DatabaseSinkAO extends AbstractDatabaseAO {

	private static final long serialVersionUID = -7905452360184301303L;
	private boolean savemetadata;
	private boolean create;
	private boolean ifnotexists;
	private boolean truncate;
	
	public DatabaseSinkAO(Connection connection, String tableName, boolean savemetadata, boolean create, boolean truncate, boolean ifnotexists) {
		super(connection, tableName);		
		this.savemetadata = savemetadata;
		this.create = create;
		this.ifnotexists = ifnotexists;
		this.truncate = truncate;
	}

	public DatabaseSinkAO(DatabaseSinkAO databaseSinkAO) {
		super(databaseSinkAO.getConnection(), databaseSinkAO.getTable());
		this.savemetadata = databaseSinkAO.savemetadata;
		this.create = databaseSinkAO.create;
		this.ifnotexists = databaseSinkAO.ifnotexists;
		this.truncate = databaseSinkAO.truncate;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return super.getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {		
		return new DatabaseSinkAO(this);
	}

	public boolean isSaveMetaData() {		
		return this.savemetadata;
	}

	public boolean isCreate() {
		return create;
	}

	public boolean isIfnotexists() {
		return ifnotexists;
	}

	public boolean isTruncate() {
		return truncate;
	}
	
	

}
