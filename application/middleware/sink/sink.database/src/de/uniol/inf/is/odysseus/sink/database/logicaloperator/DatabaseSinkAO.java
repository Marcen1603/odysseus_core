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

package de.uniol.inf.is.odysseus.sink.database.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen
 * Created at: 20.10.2011
 */
public class DatabaseSinkAO extends AbstractLogicalOperator{
	
	private static final long serialVersionUID = -6513851256783092870L;
	private String name;
	private String databasetype;
	private String host;
	private int port;
	private String tablename;
	private String user;
	private String password;
	private String databasename;
	private boolean truncate;
	private boolean drop;
	

	
	public DatabaseSinkAO(String name, String databasetype, String host, int port, String databasename, String tablename, String user, String pass, boolean drop, boolean truncate) {
		super();
		this.name = name;
		this.databasetype = databasetype;		
		this.host = host;
		this.port = port;
		this.databasename = databasename;				
		this.tablename = tablename;
		this.user = user;
		this.password = pass;
		this.drop = drop;
		this.truncate = truncate;
	}

	public DatabaseSinkAO(DatabaseSinkAO old) {
		super(old);
		this.name = old.name;
		this.databasetype = old.databasetype;
		this.host = old.host;
		this.port = old.port;
		this.tablename = old.tablename;
		this.databasename = old.databasename;		
		this.user = old.user;
		this.password = old.password;
		this.drop = old.drop;
		this.truncate = old.truncate;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new DatabaseSinkAO(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabasetype() {
		return databasetype;
	}

	public void setDatabasetype(String databasetype) {
		this.databasetype = databasetype;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSinkName(){
		return this.name;
	}
	
	public String getDatabasename(){
		return this.databasename;
	}

	public boolean isDrop() {
		return this.drop;
	}
	
	public boolean isTruncate(){
		return this.truncate;
	}

}
