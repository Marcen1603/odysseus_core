package mg.dynaquest.evaluation.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import mg.dynaquest.history.BehaviourManager;
import mg.dynaquest.history.DataDeliveryBehaviour;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;

/**
 * @author  Marco Grawunder
 */
public class DataProvider extends UnicastRemoteObject implements
		RemoteDataProvider, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7479623970578682836L;

	//private static final long serialVersionUID = 1L;
	static String user = "grawunder";

	static String password = "melkor";

	static String jdbcString = "jdbc:oracle:thin:@power2.offis.uni-oldenburg.de:1521:power2";

	static String driverClass = "oracle.jdbc.driver.OracleDriver";

	static Connection dbConnection = null;

	/**
	 * @uml.property  name="t"
	 */
	private Thread t = null;

	/**
	 * @uml.property  name="queryResults"
	 * @uml.associationEnd  qualifier="caller:java.lang.Object java.sql.ResultSet"
	 */
	public HashMap<Object, ResultSet> queryResults = new HashMap<Object, ResultSet>();

	/**
	 * @uml.property  name="elementsDelivered"
	 * @uml.associationEnd  qualifier="caller:java.lang.Object java.lang.Long"
	 */
	public HashMap<Object, Long> elementsDelivered = new HashMap<Object, Long>();

	/**
	 * @uml.property  name="lastDeliveryTime"
	 * @uml.associationEnd  qualifier="caller:java.lang.Object java.lang.Long"
	 */
	public HashMap<Object, Long> lastDeliveryTime = new HashMap<Object, Long>();

	/**
	 * @uml.property  name="initialDelay"
	 */
	private int initialDelay = 0;

	/**
	 * @uml.property  name="timeBetweenObjects"
	 */
	private int timeBetweenObjects = -1;

	/**
	 * @uml.property  name="burstBlockSize"
	 */
	private int burstBlockSize = 10;

	/**
	 * @uml.property  name="timeBetweenBurstBlocks"
	 */
	private int timeBetweenBurstBlocks = -1;

	/**
	 * @uml.property  name="sourceName"
	 */
	private String sourceName = null;

	/**
	 * @uml.property  name="useBehaviourDB"
	 */
	private boolean useBehaviourDB = true;

	static void initDB() throws ClassNotFoundException, SQLException {
		// Connection zur DB aufbauen
		Class.forName(driverClass);
		dbConnection = DriverManager.getConnection(jdbcString, user, password);
	}

	public DataProvider() throws RemoteException {

	}

	public DataProvider(String sourceName, boolean useBehaviourDB)
			throws RemoteException {
		super();
		System.out.println("Servererzeugung");
		this.sourceName = sourceName;
		this.useBehaviourDB = useBehaviourDB;
		try {
			initDB();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Erzeugt");
	}

	public boolean executeQuery(String query, Object caller, SDFConstantList values)
			throws RemoteException {
		System.out.println("Execute Query " + query + " von " + caller);
		try {
			if (dbConnection == null)
				initDB();
			if (values == null) { // kein
				Statement stmt = dbConnection.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				queryResults.put(caller, rs);
			} else {
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				for (int i = 1; i < values.size() + 1; i++) {
					SDFConstant elem = values.getConstant(i - 1);
					if (elem instanceof SDFStringConstant) {
						stmt.setString(i, elem.getString());
					} else if (elem instanceof SDFNumberConstant) {
						stmt.setDouble(i, elem.getDouble());
					} else {
						stmt.setObject(i, elem);
					}
				}
				ResultSet rs = stmt.executeQuery();
				queryResults.put(caller, rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		Calendar today = Calendar.getInstance();
		int tag = today.get(Calendar.DAY_OF_MONTH);
		//int monat = today.get(Calendar.MONTH);
		int stunde = today.get(Calendar.HOUR);
		this.elementsDelivered.put(caller, new Long(0));
		this.lastDeliveryTime.put(caller, new Long(System
				.currentTimeMillis()));

		if (this.useBehaviourDB) {
			DataDeliveryBehaviour dlb = BehaviourManager.getBehaviour(
					this.sourceName, tag, -1, -1, stunde, -1, -1);
			// Datenrate liefert Anzahl Objekte pro Sekunde und der Kehrwert ist
			// die Zeit zwischen zwei Objekten (das ganze in Millisekunden)
			this.timeBetweenObjects = Math.round(1 / dlb.getDatarate() * 1000);
			System.out.println("timeBetweenObjects " + timeBetweenObjects);
			if (dlb.getDeliveryType() == DataDeliveryBehaviour.BURSTY_DELIVERY) {
				// Zum Testen einfache einen fixen Wert angeben?
				this.timeBetweenBurstBlocks = 3 * timeBetweenObjects;
				System.out.println("timeBetweenBurstBlocks "
						+ timeBetweenBurstBlocks);
			}
		}
		System.out.println("Execute Query " + query + " von " + caller
				+ " durch");
		return true;
	}

	// Busy Wait
	public void waitForMilliseconds(int delay) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < delay) {
		}
	}

	public void waitUntil(long time) {
		while (System.currentTimeMillis() < time) {
		}
		;
	}

	public RelationalTuple nextObject(Object caller) throws RemoteException {

		ResultSet rs = (ResultSet) queryResults.get(caller);
		RelationalTuple attList = null;
		long noObjsDelivered = ((Long) this.elementsDelivered.get(caller))
				.longValue();
		long lastDelTime = ((Long) this.lastDeliveryTime.get(caller))
				.longValue();
		try {
			if (rs != null && rs.next()) {
				int colCount = rs.getMetaData().getColumnCount();
				attList = new RelationalTuple(colCount);
				for (int i = 0; i < colCount; i++) {
					attList.setAttribute(i, rs.getString(i + 1));
				}
				if (noObjsDelivered == 0) {
					System.out.println("INITIAL WAIT for " + caller);
					waitForMilliseconds(this.initialDelay);
				} else {
					if (this.timeBetweenBurstBlocks > 0) {
						// Burst-Block durch?
						if (noObjsDelivered % this.burstBlockSize == 0) {
							System.out.println("BURST WAIT (" + noObjsDelivered
									+ ") for " + caller);
							this
									.waitForMilliseconds(this.timeBetweenBurstBlocks);
						}
					}
					waitUntil(lastDelTime + this.timeBetweenObjects);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.elementsDelivered.put(caller, new Long(noObjsDelivered + 1));
		this.lastDeliveryTime.put(caller,  new Long(System.currentTimeMillis()));
		System.out.println("Liefere nächstes Objekt für " + caller);
		return attList;
	}

	/**
	 * @param initialDelay  the initialDelay to set
	 * @uml.property  name="initialDelay"
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * @return  the initialDelay
	 * @uml.property  name="initialDelay"
	 */
	public int getInitialDelay() {
		return initialDelay;
	}

	/**
	 * @param burstBlockSize  the burstBlockSize to set
	 * @uml.property  name="burstBlockSize"
	 */
	public void setBurstBlockSize(int burstBlockSize) {
		this.burstBlockSize = burstBlockSize;
	}

	/**
	 * @return  the burstBlockSize
	 * @uml.property  name="burstBlockSize"
	 */
	public int getBurstBlockSize() {
		return burstBlockSize;
	}

	/**
	 * @param timeBetweenObjects  the timeBetweenObjects to set
	 * @uml.property  name="timeBetweenObjects"
	 */
	public void setTimeBetweenObjects(int timeBetweenObjects) {
		this.timeBetweenObjects = timeBetweenObjects;
	}

	/**
	 * @param timeBetweenBurstBlocks  the timeBetweenBurstBlocks to set
	 * @uml.property  name="timeBetweenBurstBlocks"
	 */
	public void setTimeBetweenBurstBlocks(int timeBetweenBurstBlocks) {
		this.timeBetweenBurstBlocks = timeBetweenBurstBlocks;
	}

	public static void main(String[] args) {
		if (args.length != 5) {
			System.out
					.println("Aufruf mit QuellenName IntialDelay BurstBlockSize TimeBetweenBurtsBlocks TimeBetweenObjects");
			System.exit(0);
		}
		try {
			String sourceName = args[0];
			//RMISecurityManager secMan = new RMISecurityManager();
			//System.setSecurityManager(secMan);
			DataProvider dataProvider = new DataProvider(sourceName, false);
			Naming.rebind("DataProvider_" + sourceName, dataProvider);
			//dataProvider.start();
			dataProvider.setInitialDelay(Integer.parseInt(args[1]));
			dataProvider.setBurstBlockSize(Integer.parseInt(args[2]));
			dataProvider.setTimeBetweenBurstBlocks(Integer.parseInt(args[3]));
			dataProvider.setTimeBetweenObjects(Integer.parseInt(args[4]));
			System.out.println("Server wartet auf Anfragen...");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (t != null) {
			synchronized (this) {
				try {
					wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}