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
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

/**
 * 
 * @author Dennis Geesen
 * Created at: 28.10.2011
 */
public interface IDatabaseConnection {

	public void createTable(String tablename, SDFSchema schema);
	public void truncateTable(String tablename);
	public void dropTable(String tablename);
	public boolean tableExists(String tablename);
	public boolean equalSchemas(String tablename, SDFSchema schema);	
	public Connection getConnection();	
	public Map<String, String> getInformation();	
	public List<String> getTables();
	public List<String> getSchemas();
	public SDFSchema getSchema(String tablename);
}
