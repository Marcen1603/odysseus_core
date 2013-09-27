package de.uniol.inf.is.odysseus.billingmodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class BillingManager {

	private static Map<String, Map<Integer, Double>> unsavedPayments = new HashMap<>(); // CostTypeID=1
	private static Map<String, Map<Integer, Double>> unsavedSanctions = new HashMap<>(); // CostTypeID=2
	private static Map<String, Map<Integer, Double>> unsavedOperatingCosts = new HashMap<>(); // CostTypeID=3
	private static Map<String, Map<Integer, Double>> unsavedRevenues = new HashMap<>(); // CostTypeID=4

	private static int numberOfUnsavedPayments = 0;
	private static int numberOfUnsavedSanctions = 0;
	private static int numberOfUnsavedOperatingCosts = 0;
	private static int numberOfUnsavedRevenues = 0;
	
	private static long lastPersistence = 0;
	
	private static boolean initialised = false;
	private static Connection conn = null;
	
	public static void addPayment(String userID, int queryID, double amount) {
		if (unsavedPayments.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedPayments.put(userID, map);
		} else if (!unsavedPayments.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedPayments.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Double> map = unsavedPayments.get(userID);
			double newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedPayments++;
	}
	
	public static void addSanction(String userID, int queryID, double amount) {
		if (unsavedSanctions.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedSanctions.put(userID, map);
		} else if (!unsavedSanctions.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedSanctions.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Double> map = unsavedSanctions.get(userID);
			double newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedSanctions++;
	}
	
	public static void addOperatingCost(String userID, int queryID, double amount) {
		if (unsavedOperatingCosts.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedOperatingCosts.put(userID, map);
		} else if (!unsavedOperatingCosts.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedOperatingCosts.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Double> map = unsavedOperatingCosts.get(userID);
			double newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedOperatingCosts++;
	}
	
	public static void addRevenue(String userID, int queryID, double amount) {
		if (unsavedRevenues.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, amount);
			unsavedRevenues.put(userID, map);
		} else if (!unsavedRevenues.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedRevenues.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Double> map = unsavedRevenues.get(userID);
			double newAmount = map.get(queryID) + amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedRevenues++;
	}
	
	public static Map<String, Map<Integer, Double>> getUnsavedPayments() {
		return new HashMap<String, Map<Integer, Double>>(unsavedPayments);
	}
	
	public static Map<String, Map<Integer, Double>> getUnsavedSanctions() {
		return new HashMap<String, Map<Integer, Double>>(unsavedSanctions);
	}

	public static Map<String, Map<Integer, Double>> getUnsavedOperatingCosts() {
		return new HashMap<String, Map<Integer, Double>>(unsavedOperatingCosts);
	}

	public static Map<String, Map<Integer, Double>> getUnsavedRevenues() {
		return new HashMap<String, Map<Integer, Double>>(unsavedRevenues);
	}

	public static int getNumberOfUnsavedPayments() {
//		int count = 0;
//		for (Map.Entry<String, Map<Integer, Double>> entry : userToQueryToPayments.entrySet()) {
//			count += entry.getValue().size();
//		}
		return numberOfUnsavedPayments;
	}
	
	public static int getNumberOfUnsavedSanctions() {
		return numberOfUnsavedSanctions;
	}
	
	public static int getNumberOfUnsavedOperatingCosts() {
		return numberOfUnsavedOperatingCosts;
	}
	
	public static int getNumberOfUnsavedRevenues() {
		return numberOfUnsavedRevenues;
	}
	
	public static long getLastTimestampOfPersistence() {
		return lastPersistence;
	}
	
	private static void checkDatabaseConnection() {
		if (!initialised) {
			initDatabaseConnection();
			initialised = true;
		}
	}
	
	private static void initDatabaseConnection() {
		if (conn == null) {
			try {
				Class.forName("org.postgresql.Driver");
				String url = "jdbc:postgresql://localhost/Lena";
				conn = DriverManager.getConnection(url, "Lena", "");
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	public static void persistBillingInformations() {
		checkDatabaseConnection();

		// update existing database entry or insert a new row
		Map<String, Map<Integer, Double>> paymentsToPersist = new HashMap<String, Map<Integer, Double>>(unsavedPayments);
		for (Map.Entry<String, Map<Integer, Double>> entry : paymentsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Double> paymentsMap = new HashMap<Integer, Double>(entry.getValue());
			for (Map.Entry<Integer, Double> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue();

				// String query = "UPDATE CurrentRevenue SET Amount=" + amount + " WHERE AccountID='" + userID + "' and QueryID=" + queryID + " and CostTypeID=1";
				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE \"CurrentRevenue\" SET \"Amount\"=? WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=1");
					statement.setDouble(1, amount);
					statement.setString(2, userID);
					statement.setInt(3, queryID);
					int numberAffected = statement.executeUpdate();
					
					if (numberAffected == 0) {
						// query = "INSERT INTO CurrentRevenue (AccountID, QueryID, CostTypeID, Amount) VALUES ('" + userID + "', " + queryID + ", 1, " + amount + ")";
						statement = conn.prepareStatement("INSERT INTO \"CurrentRevenue\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Amount\") VALUES (?, ?, 1, ?)");
						statement.setString(1, userID);
						statement.setInt(2, queryID);
						statement.setDouble(3, amount);
						statement.executeUpdate();
					}
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
				}
			}

		}
		numberOfUnsavedPayments = 0;
		
		Map<String, Map<Integer, Double>> sanctionsToPersist = new HashMap<String, Map<Integer, Double>>(unsavedSanctions);
		for (Map.Entry<String, Map<Integer, Double>> entry : sanctionsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Double> sanctionsMap = new HashMap<Integer, Double>(entry.getValue());
			for (Map.Entry<Integer, Double> innerEntry : sanctionsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue();

				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE \"CurrentRevenue\" SET \"Amount\"=? WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=2");
					statement.setDouble(1, amount);
					statement.setString(2, userID);
					statement.setInt(3, queryID);
					int numberAffected = statement.executeUpdate();
					
					if (numberAffected == 0) {
						statement = conn.prepareStatement("INSERT INTO \"CurrentRevenue\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Amount\") VALUES (?, ?, 2, ?)");
						statement.setString(1, userID);
						statement.setInt(2, queryID);
						statement.setDouble(3, amount);
						statement.executeUpdate();
					}
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
				}
			}
			
		}
		numberOfUnsavedSanctions = 0;
		
		Map<String, Map<Integer, Double>> operatingCostsToPersist = new HashMap<String, Map<Integer, Double>>(unsavedOperatingCosts);
		for (Map.Entry<String, Map<Integer, Double>> entry : operatingCostsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Double> operatingCostsMap = new HashMap<Integer, Double>(entry.getValue());
			for (Map.Entry<Integer, Double> innerEntry : operatingCostsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue();
				
				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE \"CurrentRevenue\" SET \"Amount\"=? WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=3");
					statement.setDouble(1, amount);
					statement.setString(2, userID);
					statement.setInt(3, queryID);
					int numberAffected = statement.executeUpdate();
					
					if (numberAffected == 0) {
						statement = conn.prepareStatement("INSERT INTO \"CurrentRevenue\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Amount\") VALUES (?, ?, 3, ?)");
						statement.setString(1, userID);
						statement.setInt(2, queryID);
						statement.setDouble(3, amount);
						statement.executeUpdate();
					}
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
				}
			}
			
		}
		numberOfUnsavedOperatingCosts = 0;
		
		Map<String, Map<Integer, Double>> revenuesToPersist = new HashMap<String, Map<Integer, Double>>(unsavedRevenues);
		for (Map.Entry<String, Map<Integer, Double>> entry : revenuesToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Double> revenuesMap = new HashMap<Integer, Double>(entry.getValue());
			for (Map.Entry<Integer, Double> innerEntry : revenuesMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue();

				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE \"CurrentRevenue\" SET \"Amount\"=? WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=4");
					statement.setDouble(1, amount);
					statement.setString(2, userID);
					statement.setInt(3, queryID);
					int numberAffected = statement.executeUpdate();
					
					if (numberAffected == 0) {
						statement = conn.prepareStatement("INSERT INTO \"CurrentRevenue\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Amount\") VALUES (?, ?, 4, ?)");
						statement.setString(1, userID);
						statement.setInt(2, queryID);
						statement.setDouble(3, amount);
						statement.executeUpdate();
					}
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
				}
			}
			
		}
		numberOfUnsavedRevenues = 0;
		
		lastPersistence = System.currentTimeMillis();
	}
	
	public static void addQueryAndUserToDatabase(IPhysicalQuery query) {
		checkDatabaseConnection();
		if (query.getID() == 0)
			System.out.println("Query0 found");
		
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE \"Account\" SET \"Name\"=? WHERE \"ID\"=?");
			statement.setString(1, query.getSession().getUser().getName());
			statement.setString(2, query.getSession().getUser().getId());
			int numberAffected = statement.executeUpdate();
			
			if (numberAffected == 0) {
				statement = conn.prepareStatement("INSERT INTO \"Account\" (\"ID\", \"Name\") VALUES (?, ?)");
				statement.setString(1, query.getSession().getUser().getId());
				statement.setString(2, query.getSession().getUser().getName());
				statement.executeUpdate();
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE \"Query\" SET \"AccountID\"=? WHERE \"ID\"=?");
			statement.setString(1, query.getSession().getUser().getId());
			statement.setInt(2, query.getID());
			int numberAffected = statement.executeUpdate();
			
			if (numberAffected == 0) {
				statement = conn.prepareStatement("INSERT INTO \"Query\" (\"ID\", \"AccountID\") VALUES (?, ?)");
				statement.setInt(1, query.getID());
				statement.setString(2, query.getSession().getUser().getId());
				statement.executeUpdate();
			}
//			PreparedStatement statement = conn.prepareStatement("INSERT INTO \"Query\" (\"ID\", \"Content\", \"AccountID\") VALUES (?, ?, ?)");
//			statement.setInt(1, query.getID());
//			statement.setString(2, );
//			statement.setString(3, query.getSession().getUser().getId());
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		prepareTuplePriceTable(query);
	}
	
	private static void prepareTuplePriceTable(IPhysicalQuery query) {
//		boolean tupleIn = false;
//		boolean tupleOut = false;
//		for (IPhysicalOperator op : query.getAllOperators()) {
//			if (op instanceof TupleCostCalculationPipe) {
//				if (((TupleCostCalculationPipe) op).getCalculationType() == TupleCostCalculationType.INCOMING_TUPLES)
//					tupleIn = true;
//				else if (((TupleCostCalculationPipe) op).getCalculationType() == TupleCostCalculationType.OUTGOING_TUPLES)
//					tupleOut = true;
//			}
//		}

		// incoming tuples / payments
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO \"TuplePrice\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Price\") VALUES (?, ?, ?, ?)");
			statement.setString(1, query.getSession().getUser().getId());
			statement.setInt(2, query.getID());
			statement.setInt(3, 1);
			statement.setDouble(4, 0.1);
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

		// outgoing tuples / revenues
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO \"TuplePrice\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Price\") VALUES (?, ?, ?, ?)");
			statement.setString(1, query.getSession().getUser().getId());
			statement.setInt(2, query.getID());
			statement.setInt(3, 4);
			statement.setDouble(4, 0.2);
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

	}
}
