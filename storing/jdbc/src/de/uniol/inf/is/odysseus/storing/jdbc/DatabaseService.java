package de.uniol.inf.is.odysseus.storing.jdbc;

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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import de.uniol.inf.is.odysseus.storing.IDatabaseService;


public class DatabaseService implements IDatabaseService{

	private static final String DRIVER = "org.postgresql.Driver";
	private static final String DRIVERNAME = "postgresql";
	private static final String JDBC_VERSION = "JDBC4";
	private static final String DB_VERSION = "8.2";
	
	@Override
	public String getName(){
		return DRIVERNAME;
	}
	
	@Override
	public String getInfo(){
		return DRIVER + " "+ JDBC_VERSION + " " + DB_VERSION;
	}
	
	@Override
	public String getDriver(){
		return DRIVER;
	}
	
	@Override
	public boolean testDriver() throws ClassNotFoundException{
		 Class.forName(DRIVER);
		 return true;
	}	
	
	public Connection getConnection(String url, String user, String password) throws SQLException{
		return DriverManager.getConnection(url, user, password);
	}
}