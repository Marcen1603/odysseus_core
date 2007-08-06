package mg.dynaquest.history;

import java.sql.*;

/**
 * @author  Marco Grawunder
 */
public class BehaviourManager {

	/**
	 * @uml.property  name="user"
	 */
	static String user = "sdm_mg";

	/**
	 * @uml.property  name="password"
	 */
	static String password = "melkor";

	static String jdbcString = "jdbc:oracle:thin:@power2.offis.uni-oldenburg.de:1521:power2";

	static String driverClass = "oracle.jdbc.driver.OracleDriver";

	static Connection dbConnection = null;

	static void initDB() throws ClassNotFoundException, SQLException {
		// Connection zur DB aufbauen
		Class.forName(driverClass);
		dbConnection = DriverManager.getConnection(jdbcString, user, password);
	}

	public static DataDeliveryBehaviour getBehaviour(String quellenURI,
			int tag, int monat, int wochentag, int stunde, int minute, int kw) {

		DataDeliveryBehaviour behaviour = null;

		try {
			if (dbConnection == null)
				initDB();
			Statement stmt = dbConnection.createStatement();
			StringBuffer sql = new StringBuffer(
					"select avg(datenrate),max(DATENRATENTYP) from DYNQ_DATARATE where QUELLE ='"
							+ quellenURI + "'");
			StringBuffer wherePart = new StringBuffer();
			if (tag > 0) {
				wherePart.append(" and tag=" + tag);
			}
			if (monat > 0) {
				wherePart.append(" and monat=" + monat);
			}
			if (wochentag > 0) {
				wherePart.append(" and wochentag=" + wochentag);
			}
			if (stunde > 0) {
				wherePart.append(" and stunde=" + stunde);
			}
			if (minute > 0) {
				wherePart.append(" and minute=" + minute);
			}
			if (kw > 0) {
				wherePart.append(" and wochentag=" + kw);
			}

			//System.out.println(sql.append(wherePart).toString());

			ResultSet rs = stmt.executeQuery(sql.toString());
			if (rs.next()) {

				float avg = rs.getFloat(1);
				int typ = rs.getInt(2);
				//System.out.println(avg+" "+typ);
				behaviour = new DataDeliveryBehaviour(quellenURI, tag, monat,
						wochentag, stunde, minute, kw, typ, avg);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return behaviour;
	}

	/**
	 * @uml.property  name="user"
	 */
	public void setUser(String user) {
		BehaviourManager.user = user;
	}

	/**
	 * @param password  the password to set
	 * @uml.property  name="password"
	 */
	public void setPassword(String password) {
		BehaviourManager.password = password;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		DataDeliveryBehaviour bev = BehaviourManager.getBehaviour("Quelle_1",
				-1, 2, -1, -1, -1, -1);
		long point1 = System.currentTimeMillis();
		System.out.println(bev);
		bev = BehaviourManager.getBehaviour("Quelle_4", -1, 2, -1, -1, -1, -1);
		long point2 = System.currentTimeMillis();

		System.out.println((point1 - start) + " " + (point2 - point1));
	}

}