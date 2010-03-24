package de.uniol.inf.is.odysseus.dbIntegration;



import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;

/**
 * Diese Klasse stellt einige Testmethoden fuer die OSGi-Konsole zur Verfuegung.
 * @author crolfes
 *
 */
public class Console implements CommandProvider {

	
	IConnectionData connectionData;
		
	public void _setConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		String user = "root";
		String password = "";
		String url = "jdbc:mysql://localhost:3306/auction";
		String driver = "com.mysql.jdbc.Driver";
		DBProperties prop = new DBProperties(database, url, driver, user, password);
		
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
		help.append("---Database Integration---").append("setConn - Verbindung zu Standard-MySQL-DB erzeugen").append(
	    "test - Test starten").append("deleteConn - Verbindung wieder entfernen");
		return help.toString();
	}
	
	protected void setConnectionService(IConnectionData connectionData) {
		this.connectionData = connectionData;
	}
	
	protected void unsetConnectionService(IConnectionData connectionData) {
		this.connectionData = null;
	}
	
	
}
