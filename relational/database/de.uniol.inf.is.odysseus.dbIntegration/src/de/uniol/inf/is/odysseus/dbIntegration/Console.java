package de.uniol.inf.is.odysseus.dbIntegration;



import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.dataAccess.DataAccess;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;

public class Console implements CommandProvider {

	
	IConnectionData connectionData;
	DataAccess dal;
	Controller controller;
	
	/*
	 * ***********************
			CREATE TABLE  `auction`.`person` (
			  `id` int(11) NOT NULL,
			  `name` varchar(50) NOT NULL,
			  `email` varchar(50) NOT NULL,
			  `creditcard` varchar(50) NOT NULL,
			  `city` varchar(50) NOT NULL,
			  `state` varchar(50) NOT NULL,
			  PRIMARY KEY (`id`)
			) ENGINE=InnoDB DEFAULT CHARSET=latin1
			
			
			public void _test1(CommandInterpreter ci) {
				String db = "SELECT * " +
					"FROM person " +
					"WHERE $auction.seller = person.id AND " +
							"person.id < 400";
				
				String ds;
				ds = "SELECT db.name, db.city, db.state, db.id, auction.seller " +
						"FROM nexmark:auction2 UNBOUNDED AS auction, DATABASE(auction, [[" + db + "]]) AS db ";
				
				
				addQuery(ds);
			}
			
			public void _test2(CommandInterpreter ci) {
				String db = "SELECT * " +
					"FROM person " +
					"WHERE $ds.seller = person.id AND " +
							"person.id < 400";
				
				String ds;
				ds = "SELECT db.id, ds.seller " +
						"FROM nexmark:auction2 UNBOUNDED AS ds, DATABASE(auction, 'ctrl cache', [[" + db + "]]) AS db ";
				
				
				addQuery(ds);
			}
			
			
			
			
			
	 ***********************
	 **/
	
	
	
	public void _setConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		String schema = "auction";
		String user = "root";
		String password = "";
		String url = "jdbc:mysql://localhost:3306/auction";
		String driver = "com.mysql.jdbc.Driver";
		DBProperties prop = new DBProperties(database, url, driver, user, password, schema);
		
		try {
			connectionData.addConnection(prop);
			commandInterpreter.println();
			_testConn(commandInterpreter);
		} catch (BackingStoreException e) {
			commandInterpreter.println("Error");
			e.printStackTrace();
		}
	}
	
	public void _testConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		try {
			DBProperties prop = connectionData.getConnection(database);
			if (prop != null) {
				commandInterpreter.println("database: " + prop.getDatabase());
				commandInterpreter.println("driver: " + prop.getDriverClass());
				commandInterpreter.println("password: " + prop.getPassword());
				commandInterpreter.println("schema: " + prop.getSchema());
				commandInterpreter.println("url: " + prop.getUrl());
				commandInterpreter.println("user: " + prop.getUser());
			} else {
				commandInterpreter.println("Connection " + database + " not exists!");
			}
			
			
		} catch (BackingStoreException e) {
			commandInterpreter.println("Error: Connection not possible!");
			e.printStackTrace();
		}
	}
	
	public void _deleteConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		
		try {
			connectionData.deleteConnection(database);
			commandInterpreter.println("Connection " + database + " deleted!");
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getHelp() {
		StringBuilder help = new StringBuilder();
		help.append("---Database Integration---").append(
		    "test - Test starten");
		return help.toString();
	}
	
	protected void setConnectionService(IConnectionData connectionData) {
		this.connectionData = connectionData;
	}
	
	protected void unsetConnectionService(IConnectionData connectionData) {
		this.connectionData = null;
	}
	
	
}
