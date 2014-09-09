package windscadaanwendung.db;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;

public class DBConnection extends AbstractDatabaseConnectionFactory {
	
	//TODO: evtl in GUI abfragen
	public static String server = "duemmer.informatik.uni-oldenburg.de";
	public static int port = 1426;
	public static String database = "WindSCADAClient";
	public static String user = "WindSCADAClient";
	public static String password = "Wind0815clit";
	public static Connection conn = null;
	
	//TODO: evtl durch export des packages redundanten code verhindern
	public IDatabaseConnection createConnection(String server, int port, String database, String user, String password) {
		Properties connectionProps = getCredentials(user, password);
		if(port==-1){
			port = 3306;
		}
		if(server==null || server.isEmpty()){
			server = "localhost";
		}			
		String connString = "jdbc:mysql://" + server + ":" + port + "/" + database;		
		return new DatabaseConnection(connString, connectionProps);
	}
	
	public DBConnection() {
	}
	
	public static void setNewConnection() {
		if (conn == null) {
			DatabaseConnection dbconn = (DatabaseConnection) (new DBConnection()).createConnection(server, port, database, user, password);
			try {
				conn = dbconn.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<WindFarm> getFarmList() {
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList<WindFarm> farms = null;

		try {
			// load WindFarms
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM FARM");
		    
		    farms = new ArrayList<WindFarm>();
		    WindFarm actFarm = null;
		    while (rs.next()) {
		    	actFarm = new WindFarm();
		    	actFarm.setID(rs.getInt("ID"));
		    	farms.add(actFarm);
		    }
		    
		    try {
	            rs.close();
	        } catch (SQLException sqlEx) { // ignore 

	        rs = null;
	        }
		    
		    try {
	            stmt.close();
	        } catch (SQLException sqlEx) { // ignore 

	        stmt = null;
	        }
		    
		    // load WKAs
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("SELECT * FROM WKA JOIN FARM ON WKA.FARM_ID = FARM.ID ORDER BY WKA.ID");

		    WKA actWka;
		    while (rs.next()) {
		    	actWka = new WKA();
		    	actWka.setID(rs.getInt("WKA.ID"));
		    	actWka.setLatitude(rs.getDouble("LATITUDE"));
		    	actWka.setLongtude(rs.getDouble("LONGTUDE"));
		    	//TODO: Scripte einlesen (auch für windfarmen)
		    	// search for the windFarm of this WKA
		    	for (WindFarm farm: farms) {
		    		if (farm.getID() == rs.getInt("FARM.ID")) {
		    			actFarm = farm;
		    			break;
		    		}
		    	}
		    	actWka.setFarm(actFarm);
		    	actFarm.addWKA(actWka);
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { // ignore 

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { // ignore 

		        stmt = null;
		    }
		}
		
		
	}
}
		return farms;
}
}