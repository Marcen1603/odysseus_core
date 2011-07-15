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
package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author KPancratz
 * 
 */
public class DatabaseService {

	/*
	 * Changed the 0..n databaseServices to 1..1. 
	 * 
	 */
	private static IDatabaseService databaseService;

	private static String defaultURL;
	private static String defaultUser;
	private static String defaultPassword;

	volatile protected static Logger LOGGER = LoggerFactory.getLogger(IDatabaseService.class);;

	/**
	 * Register a default connection for User without Database URL, User and Password. 
	 * 
	 * @TODO (Christian and Cai)
	 * Set default Connection only by User which is authorized by the User-Management 
	 * 
	 * @param defaultURL The default URL.
	 * @param defaultUser The default User.
	 * @param defaultPassword The default Password.
	 */
	public static void registerDefault(String defaultURL, String defaultUser, String defaultPassword){
		LOGGER.info(String.format("Set new default connection: %s with user %s",defaultURL,defaultUser));
		DatabaseService.defaultURL = defaultURL;
		DatabaseService.defaultUser = defaultUser;
		DatabaseService.defaultPassword = defaultPassword;
	}
	
	public static Connection getConnection(String url,String  user,String  password) throws SQLException{
		LOGGER.info(String.format("Return connection: %s with user %s",defaultURL,defaultUser));
		return DatabaseService.databaseService.getConnection(url, user, password);
	}
	
	public static Connection getDefaultConnection() throws SQLException{
		LOGGER.info(String.format("Return current default connection: %s with user %s",defaultURL,defaultUser));
		return DatabaseService.databaseService.getConnection(defaultURL, defaultUser, defaultPassword);
	}
	
	public static void bindDatabaseService(IDatabaseService service){
		DatabaseService.databaseService = service;
		if((DatabaseService.databaseService.getDefaultUser() != null) &&
		   (DatabaseService.databaseService.getDefaultPassword() != null) &&
		   (DatabaseService.databaseService.getDefaultURL() != null)){
			DatabaseService.defaultURL = service.getDefaultURL();
			DatabaseService.defaultUser = service.getDefaultUser();
			DatabaseService.defaultPassword = service.getDefaultPassword();
			LOGGER.info(String.format("Registered Database Service: %s with default configuration(User/URL): %s %s.", service.getName(),DatabaseService.defaultUser, DatabaseService.defaultURL));
		}
		else{
			LOGGER.info(String.format("Registered Database Service: %s without default configuration.", service.getName()));
		}		
		LOGGER.info(String.format("DatabaseService Info: %s", service.getInfo()));	
	}
	
	public static void unbindDatabaseService(IDatabaseService service){
		LOGGER.info(String.format("Unregistered Database Service: %s " ,service.getName()));
		DatabaseService.databaseService = null;
	}

}

