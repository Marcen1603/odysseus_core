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

package de.uniol.inf.is.odysseus.database.drivers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public class MySQLConnectionFactory extends AbstractDatabaseConnectionFactory {

	@Override
	public IDatabaseConnection createConnection(String server, int port, String database, String user, String password) throws SQLException {
		Properties connectionProps = getCredentials(user, password);
		String connString = "jdbc:mysql://" + server + ":" + port + "/" + database;
		Connection con = DriverManager.getConnection(connString, connectionProps);
		return new DatabaseConnection(con);
	}

}
