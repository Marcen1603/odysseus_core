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

package de.uniol.inf.is.odysseus.database.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 28.10.2011
 */
public interface IDatabaseConnection {

	public void createTable(String tablename, SDFSchema schema) throws SQLException;
	public void truncateTable(String tablename) throws SQLException;
	public void dropTable(String tablename) throws SQLException;
	public boolean tableExists(String tablename);
	public boolean equalSchemas(String tablename, SDFSchema schema) throws SQLException;	
	public Connection getConnection() throws SQLException;	
	public Map<String, String> getInformation() throws SQLException;	
	public List<String> getTables() throws SQLException;
	public List<String> getSchemas() throws SQLException;
	public SDFSchema getSchema(String tablename) throws SQLException;
	public boolean testConnection() throws SQLException;	
	public void checkProperties() throws SQLException;
}
