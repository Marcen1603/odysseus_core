package windscadaanwendung.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import windscadaanwendung.ca.FarmList;
import windscadaanwendung.ca.WKA;
import windscadaanwendung.ca.WindFarm;
import windscadaanwendung.hd.HitWKAData;
import windscadaanwendung.hd.HitWindFarmData;
import windscadaanwendung.hd.ae.AEEntry;
import windscadaanwendung.hd.ae.HitAEData;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;

/**
 * This class manages the Connection to the Database which contains the
 * historical data of all windFarms, WKAs and the Warnings and Errors.
 * 
 * @author MarkMilster
 * 
 */
public class DBConnectionHD {

	public static Connection conn = null;
	private static Map<String, String> credentials;

	/**
	 * Sets a new Connection to a Database which is specified in
	 * config/HDDBConnCredentials.txt in the windSCADA bundle, if there is no
	 * connection at this time. It uses the MySQLConnectionFactory out of
	 * Odysseus.
	 */
	public static void setNewConnection() {
		try {
			credentials = DBConnectionCredentials
					.load("config/HDDBConnCredentials.txt");
			credentials.get("server");
			IDatabaseConnection dbconn = (DatabaseConnectionDictionary.getFactory("MYSQL"))
					.createConnection(credentials.get("server"),
							Integer.parseInt(credentials.get("port")),
							credentials.get("database"),
							credentials.get("user"),
							credentials.get("password"));
			conn = dbconn.getConnection();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Refreshes the historical WKAData for every WKA of every windFarm
	 * currently loaded in the windFarms of the FarmList.
	 * 
	 * @param timestamp
	 *            The timestamp since when the data should be loaded. The oldest
	 *            historical Data which will be loaded out of the Database has
	 *            this timestamp.
	 */
	public static void refreshHitWKAData(Date timestamp) {
		long Ltimestamp = timestamp.getTime();
		ResultSet rs = null;
		WKA wka = null;

		try {
			CallableStatement cStmt = conn
					.prepareCall("{call hit_values_procedure(?)}");
			cStmt.setLong(1, Ltimestamp);

			if (cStmt.execute()) {
				rs = cStmt.getResultSet();
				HitWKAData data = null;
				while (rs.next()) {
					data = new HitWKAData();
					data.setAvgWindSpeed(rs.getDouble("avg_wind_speed"));
					data.setAvgWindDirection(rs.getDouble("avg_wind_direction"));
					data.setAvgRotationalSpeed(rs
							.getDouble("avg_rotational_speed"));
					data.setAvgPerformance(rs.getDouble("avg_corrected_score"));
					data.setMaxPerformance(rs.getDouble("max_corrected_score"));
					data.setMaxRotationalSpeed(rs
							.getDouble("max_rotational_speed"));
					data.setMaxWindSpeed(rs.getDouble("max_wind_speed"));
					data.setMinPerformance(rs.getDouble("min_corrected_score"));
					data.setMinRotationalSpeed(rs
							.getDouble("min_rotational_speed"));
					data.setMinWindSpeed(rs.getDouble("min_wind_speed"));

					wka = FarmList.getWKA(rs.getInt("wka_id"));
					wka.setHitWKAData(data);
				}
			} else {
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

	/**
	 * Refreshes the historical windFarmData for every windFarm currently loaded
	 * in the FarmList.
	 * 
	 * @param timestamp
	 *            The timestamp since when the data should be loaded. The oldest
	 *            historical Data which will be loaded out of the Database has
	 *            this timestamp.
	 */
	public static void refreshHitFarmData(Date timestamp) {
		long Ltimestamp = timestamp.getTime();
		ResultSet rs = null;
		WindFarm windFarm = null;

		try {
			CallableStatement cStmt = conn
					.prepareCall("{call hit_values_farm_procedure(?)}");
			cStmt.setLong(1, Ltimestamp);

			if (cStmt.execute()) {
				rs = cStmt.getResultSet();
				HitWindFarmData data = null;
				while (rs.next()) {
					data = new HitWindFarmData();
					data.setAvgPervormance(rs.getDouble("avg_corrected_score"));

					windFarm = FarmList.getFarm(rs.getInt("wp_id"));
					windFarm.setHitWindFarmData(data);
				}
			} else {
				System.out.println("No Hit WindFarm Data Found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			rs.close();
		} catch (SQLException sqlEx) { // ignore

			rs = null;
		}

	}

	/**
	 * 
	 * Refreshes the historical data of the stored Warnings and Errors.
	 * 
	 * First every entry in HitAEData will be disposed, then this new ones will
	 * be added.
	 * 
	 * @param sinceDate
	 *            The timestamp since when the data should be loaded. The oldest
	 *            historical Warning or Error which will be loaded out of the
	 *            Database has this timestamp.
	 * @param untilDate
	 *            The timestamp until when the data should be loaded. The newest
	 *            historical Warning or Error which will be loaded out of the
	 *            Database has this timestamp.
	 * @param showConfirmedBool
	 *            true if you want to load the confirmed and not confirmed
	 *            warnings and errors, false if you just want to load the not
	 *            confirmed ones
	 */
	public static void refreshHitAEData(Date sinceDate, Date untilDate,
			boolean showConfirmedBool) {
		Timestamp sinceTimestamp = new Timestamp(sinceDate.getTime());
		Timestamp untilTimestamp = new Timestamp(untilDate.getTime());
		int showConfirmed = showConfirmedBool ? 1 : 0;
		ResultSet rs = null;

		HitAEData.clearEntryList();

		try {
			CallableStatement cStmt = conn
					.prepareCall("{call hit_ae_procedure(?, ?, ?)}");
			cStmt.setTimestamp(1, sinceTimestamp);
			cStmt.setTimestamp(2, untilTimestamp);
			cStmt.setInt(3, showConfirmed);

			if (cStmt.execute()) {
				rs = cStmt.getResultSet();
				AEEntry aeEntry = null;
				while (rs.next()) {
					aeEntry = new AEEntry();
					aeEntry.setId(rs.getInt("id"));
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
			} else {
				System.out
						.println("No Hit AE Data Found since the given timestamp");
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

	/**
	 * Writes all the values from the specified aeEntry into the Database. The
	 * aeEntry with the id of the specified aeEntry will be updated.
	 * 
	 * @param aeEntry
	 *            The aeEntry which will be updated. It is necessary that this
	 *            aeEntry has a id which is stored in the database
	 */
	public static void updateAEEntry(AEEntry aeEntry) {
		PreparedStatement stmt = null;

		try {
			stmt = conn
					.prepareStatement("update AEarchive set confirmed = ? , comment = ? where id = ?");
			stmt.setInt(1, aeEntry.isConfirm() ? 1 : 0);
			stmt.setString(2, aeEntry.getComment());
			stmt.setInt(3, aeEntry.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			stmt.close();
		} catch (SQLException sqlEx) { // ignore
			stmt = null;
		}
	}

}