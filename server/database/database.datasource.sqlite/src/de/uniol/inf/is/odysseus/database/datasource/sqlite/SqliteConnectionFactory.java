/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.database.datasource.sqlite;

import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * @author Cornelius Ludmann
 *
 */
public class SqliteConnectionFactory extends AbstractDatabaseConnectionFactory {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory#createConnection(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public IDatabaseConnection createConnection(String server, int port, String database, String user,
			String password) {
		return new DatabaseConnection("jdbc:sqlite:" + database, new Properties());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.database.connection.IDatabaseConnectionFactory#getDatabase()
	 */
	@Override
	public String getDatabase() {
		return "sqlite";
	}

}
