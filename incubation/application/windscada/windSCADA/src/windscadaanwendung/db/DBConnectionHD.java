package windscadaanwendung.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.hd.HitWindFarmData;
import windscadaanwendung.hd.ae.AEEntry;
import windscadaanwendung.hd.ae.HitAEData;
import de.uniol.inf.is.odysseus.database.connection.AbstractDatabaseConnectionFactory;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnection;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

public class DBConnectionHD extends AbstractDatabaseConnectionFactory {
	
	//TODO: evtl in GUI abfragen
	public static String server = "duemmer.informatik.uni-oldenburg.de";
	public static int port = 1426;
	public static String database = "WindSCADAServer";
	public static String user = "WindSCADAServer";
	public static String password = "Wind0815serv";
	public static Connection conn = null;
	
	//TODO: evtl durch export des packages redundanten code verhindern
	@Override
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
	
	public static void refreshHitWKAData(Date timestamp) {
		long Ltimestamp = timestamp.getTime();
		ResultSet rs = null;
		WKA wka = null;
		
			// load hitWKAData
			
		    try {
		    	CallableStatement cStmt = conn.prepareCall("{call hit_values_procedure(?)}");
		    	// TODO
		    	cStmt.setLong(1, Ltimestamp);
		    
		    	if (cStmt.execute()) {
		    		rs = cStmt.getResultSet();
		    		HitWKAData data = null;
				    while (rs.next()) {
				    	data = new HitWKAData();
				    	data.setAvgWindSpeed(rs.getDouble("avg_wind_speed"));
					    data.setAvgWindDirection(rs.getDouble("avg_wind_direction"));
					    data.setAvgRotationalSpeed(rs.getDouble("avg_rotational_speed"));
					    data.setAvgPerformance(rs.getDouble("avg_corrected_score"));
					    data.setMaxPerformance(rs.getDouble("max_corrected_score"));
					    data.setMaxRotationalSpeed(rs.getDouble("max_rotational_speed"));
					    data.setMaxWindSpeed(rs.getDouble("max_wind_speed"));
					    data.setMinPerformance(rs.getDouble("min_corrected_score"));
					    data.setMinRotationalSpeed(rs.getDouble("min_rotational_speed"));
					    data.setMinWindSpeed(rs.getDouble("min_wind_speed"));
					    
					    wka = FarmList.getWKA(rs.getInt("wka_id"));
					    wka.setHitWKAData(data);
				    } 
		    	}
				     else {
				    	System.out.println("No Hit Data Found for WKA ");
				    }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   

		    try {
		    	if (rs != null) {
		    		rs.close();
		    	} 
	        } catch (SQLException sqlEx) { // ignore 

	        rs = null;
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
	
	public static void refreshHitAEData(Date utilTimestamp) {
		Timestamp timestamp = new Timestamp(utilTimestamp.getTime());
		ResultSet rs = null;
		
			HitAEData.clearEntryList();
			
		    try {
		    	CallableStatement cStmt = conn.prepareCall("{call hit_ae_procedure(?)}");
		    	// TODO
		    	cStmt.setTimestamp(1, timestamp);
		    
		    	if (cStmt.execute()) {
		    		rs = cStmt.getResultSet();
		    		AEEntry aeEntry = null;
		    		System.out.println("RS-Fetch: " + rs.getFetchSize());
				    while (rs.next()) {
				    	aeEntry = new AEEntry();
				    	aeEntry.setWkaId(rs.getInt("wka_id"));
				    	aeEntry.setFarmId(rs.getInt("wp_id"));
				    	aeEntry.setValueType(rs.getString("type"));
				    	aeEntry.setWarning(rs.getBoolean("warning"));
				    	aeEntry.setError(rs.getBoolean("error"));
				    	aeEntry.setConfirm(rs.getBoolean("confirmed"));
				    	aeEntry.setTimestamp(rs.getString("starttime"));
				    	aeEntry.setComment(rs.getString("comment"));
				    	HitAEData.addAEEntry(aeEntry);
				    } 
		    	}
				     else {
				    	System.out.println("No Hit AE Data Found since the given timestamp");
				    }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		   

		    try {
		    	if (rs != null) {
		    		rs.close();
		    	} 
	        } catch (SQLException sqlEx) { // ignore 

	        rs = null;
	        }
	}
	
}