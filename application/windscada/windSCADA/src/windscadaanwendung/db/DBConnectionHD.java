package windscadaanwendung.db;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.hd.HitWindFarmData;

public class DBConnectionHD extends AbstractDatabaseConnectionFactory {
	
	//TODO: evtl in GUI abfragen
	public static String server = "duemmer.informatik.uni-oldenburg.de";
	public static int port = 1426;
	public static String database = "WindSCADAServer";
	public static String user = "WindSCADAServer";
	public static String password = "Wind0815serv";
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
	
	public DBConnectionHD() {
	}
	
	public static void setNewConnection() {
		if (conn == null) {
			DatabaseConnection dbconn = (DatabaseConnection) (new DBConnectionHD()).createConnection(server, port, database, user, password);
			
			try {
				conn = dbconn.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void setHitWKAData(WKA wka) {

		Statement stmt = null;
		ResultSet rs = null;

		
			// load hitWKAData
			
		    try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM hit_values WHERE wka_id = " + wka.getID());

				    HitWKAData data = new HitWKAData();
				    // there is only one tuple
				    if (rs.next()) {
				    	data.setAvgWindSpeed(rs.getDouble("avg_wind_speed"));
					    data.setAvgWindDirection(rs.getDouble("avg_wind_direction"));
					    data.setAvgRotationalSpeed(rs.getDouble("avg_rotational_speed"));
					    data.setAvgPerformance(rs.getDouble("avg_corrected_score"));
					    wka.setHitWKAData(data);
				    } else {
				    	System.out.println("No Hit Data Found for WKA " + wka.getID());
				    }
			} catch (SQLException e) {
				e.printStackTrace();
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
	
	}
	
	public static void setHitFarmData(WindFarm farm) {

		Statement stmt = null;
		ResultSet rs = null;

		
			// load hitWKAData
			
		    try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM hit_values_park WHERE wp_id = " + farm.getID());

				    HitWindFarmData data = new HitWindFarmData();
				    // there is only one tuple
				    if (rs.next()) {
				    	data.setAvgPervormance(rs.getDouble("avg_corrected_score"));
					    farm.setHitWindFarmData(data);
				    } else {
				    	System.out.println("No Hit Data Found for WindFarm " + farm.getID());
				    }
			} catch (SQLException e) {
				e.printStackTrace();
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
	
	}
	
}