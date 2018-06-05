package windscadaanwendung.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * This class manages the Connection to the Config-Database which contains the
 * data about the windFarms
 * 
 * @author MarkMilster
 * 
 */
public class DBConnectionCA {

	public static Connection conn = null;
	private static Map<String, String> credentials;

	/**
	 * Sets a new Connection to a Database which is specified in
	 * config/CADBConnCredentials.txt in the windSCADA bundle, if there is no
	 * connection at this time. It uses the MySQLConnectionFactory out of
	 * Odysseus.
	 */
	public static void setNewConnection() {
		try {
			credentials = DBConnectionCredentials
					.load("config/CADBConnCredentials.txt");
			credentials.get("server");
			IDatabaseConnection dbconn = (DatabaseConnectionDictionary.getFactory("MYSQL"))
					.createConnection(credentials.get("server"),
							Integer.parseInt(credentials.get("port")),
							credentials.get("database"),
							credentials.get("user"),
							credentials.get("password"));
			conn = dbconn.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads all the windFarms and the WKAs out of the Database and mathes them
	 * together.
	 * 
	 * @return a List of the windFarms
	 */
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
			rs = stmt
					.executeQuery("SELECT * FROM WKA JOIN FARM ON WKA.FARM_ID = FARM.ID ORDER BY WKA.ID");

			WKA actWka;
			while (rs.next()) {
				actWka = new WKA();
				actWka.setID(rs.getInt("WKA.ID"));
				actWka.setHost(rs.getString("HOST"));
				actWka.setPort(rs.getInt("PORT"));
				actWka.setLatitude(rs.getDouble("LATITUDE"));
				actWka.setLongtude(rs.getDouble("LONGTUDE"));
				// search for the windFarm of this WKA
				for (WindFarm farm : farms) {
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