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

package de.uniol.inf.is.odysseus.database.rcp.views;

import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 08.11.2011
 */
public class DatabaseInformationsViewEntry {
		
	private IDatabaseConnection connection;

	public DatabaseInformationsViewEntry(IDatabaseConnection iDatabaseConnection){
		this.connection = iDatabaseConnection;		
	}

	public IDatabaseConnection getConnection() {
		return connection;
	}

	public void setConnection(IDatabaseConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public String toString() {
		return "Information";
	}
	
}
