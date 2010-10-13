package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;

import de.uniol.inf.is.odysseus.derby.IDatabaseService;


public class DatabaseServiceLoader {

	private static IDatabaseService service = null;	
	
	public static Connection getConnection(){
		return DatabaseServiceLoader.service.getDatabaseConnection();
	}
	
	public void bindDatabaseService(IDatabaseService service){
		DatabaseServiceLoader.service = service;	
	//	DatabaseFill.fillDB();
	}
	
	public void unbindDatabaseService(IDatabaseService service){
		DatabaseServiceLoader.service = null;
	}
	
}
