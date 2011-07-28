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

import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="DATABASESINK")
public class DatabaseSinkAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6734727544384678902L;
	
	private Connection connection;
	private String table;
	
	private boolean savemetadata;
	private boolean create;
	private boolean ifnotexists;
	private boolean truncate;
	
	public DatabaseSinkAO(Connection connection, String table, boolean savemetadata, boolean create, boolean truncate, boolean ifnotexists) {	
		super();
		this.table = table;
		this.connection = connection;
		
		this.savemetadata = savemetadata;
		this.create = create;
		this.ifnotexists = ifnotexists;
		this.truncate = truncate;
	}

	public DatabaseSinkAO(DatabaseSinkAO databaseSinkAO) {
		super(databaseSinkAO);
		this.table = databaseSinkAO.getTable();
		this.connection = databaseSinkAO.getConnection();
		
		this.savemetadata = databaseSinkAO.savemetadata;
		this.create = databaseSinkAO.create;
		this.ifnotexists = databaseSinkAO.ifnotexists;
		this.truncate = databaseSinkAO.truncate;
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

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema();
	}

	@Override
	public DatabaseSinkAO clone() {
		return new DatabaseSinkAO(this);
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public boolean isSavemetadata() {
		return savemetadata;
	}

	public void setSavemetadata(boolean savemetadata) {
		this.savemetadata = savemetadata;
	}

	public void setCreate(boolean create) {
		this.create = create;
	}

	public void setIfnotexists(boolean ifnotexists) {
		this.ifnotexists = ifnotexists;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}
	
	
}
