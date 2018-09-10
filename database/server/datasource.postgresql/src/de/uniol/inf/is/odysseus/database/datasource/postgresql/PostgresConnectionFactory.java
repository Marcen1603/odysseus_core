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

package de.uniol.inf.is.odysseus.database.datasource.postgresql;

import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 16.03.2012
 */
public class PostgresConnectionFactory extends AbstractDatabaseConnectionFactory {

	@Override
	public IDatabaseConnection createConnection(String server, int port, String database, String user, String password){
		Properties connectionProps = getCredentials(user, password);
        if ((server == null) || ("".equals(server))) {
            server = "localhost";
        }   
		String connString = "jdbc:postgresql://" + server + ":" + port + "/" + database;		
		return new DatabaseConnection(connString, connectionProps);		
	}
	
	@Override
	public String getDatabase() {
		return "postgresql";
	}

}
