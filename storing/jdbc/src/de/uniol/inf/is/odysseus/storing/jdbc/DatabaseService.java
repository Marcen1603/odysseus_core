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

	private static final String DRIVER = "JDBC";
	
	@Override
	public String getName(){
		return DRIVER;
	}
	
	@Override
	public String getInfo(){
		return "The JDBC DatabaseService is able to handle every database which use JDBC and the current JDBC host is registered as a fragment-Bundle in the Odysseus JDBC Bundle";
	}
	
	@Override
	public Object testDriver(String URL) throws ClassNotFoundException{
		 try {
			return Class.forName(URL).newInstance();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public Connection getConnection(String url, String user, String password) throws SQLException{
		return DriverManager.getConnection(url, user, password);
	}

	@Override
	public String getDefaultURL() {
		return null;
	}

	@Override
	public String getDefaultUser() {
		return null;
	}

	@Override
	public String getDefaultPassword() {
		return null;
	}

}