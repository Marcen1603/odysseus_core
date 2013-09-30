package de.uniol.inf.is.odysseus.billingmodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class DatabaseBillingManager implements IBillingManager{

	private Map<String, Map<Integer, Double>> unsavedPayments = new HashMap<>(); // CostTypeID=1
	private Map<String, Map<Integer, Double>> unsavedPaymentSanctions = new HashMap<>(); // CostTypeID=2
	private Map<String, Map<Integer, Double>> unsavedRevenueSanctions = new HashMap<>(); // CostTypeID=3
	private Map<String, Map<Integer, Double>> unsavedRevenues = new HashMap<>(); // CostTypeID=4

	private int numberOfUnsavedPayments = 0;
	private int numberOfUnsavedPaymentSanctions = 0;
	private int numberOfUnsavedRevenueSanctions = 0;
	private int numberOfUnsavedRevenues = 0;
	
	private long lastPersistence = 0;
	
	private boolean initialised = false;
	private Connection conn = null;
	
	@Override
	public void addPayment(String userID, int queryID, double amount) {
		if (unsavedPayments.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedPayments.put(userID, map);
		} else if (!unsavedPayments.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedPayments.get(userID);
			map.put(queryID, -amount);
		} else {
			Map<Integer, Double> map = unsavedPayments.get(userID);
			double newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedPayments++;
	}
	
	@Override
	public void addPaymentSanction(String userID, int queryID, double amount) {
		if (unsavedPaymentSanctions.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, amount);
			unsavedPaymentSanctions.put(userID, map);
		} else if (!unsavedPaymentSanctions.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedPaymentSanctions.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Double> map = unsavedPaymentSanctions.get(userID);
			double newAmount = map.get(queryID) + amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedPaymentSanctions++;
	}
	
	@Override
	public void addRevenueSanction(String userID, int queryID, double amount) {
		if (unsavedRevenueSanctions.get(userID) == null) {
			Map<Integer, Double> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedRevenueSanctions.put(userID, map);
		} else if (!unsavedRevenueSanctions.get(userID).containsKey(queryID)) {
			Map<Integer, Double> map = unsavedRevenueSanctions.get(userID);
			map.put(queryID, -amount);
		} else {
			Map<Integer, Double> map = unsavedRevenueSanctions.get(userID);
			double newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedRevenueSanctions++;
	}
	
	@Override
	public void addRevenue(String userID, int queryID, double amount) {
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
	
	@Override
	public Map<String, Map<Integer, Double>> getUnsavedPayments() {
		return new HashMap<String, Map<Integer, Double>>(unsavedPayments);
	}
	
	@Override
	public Map<String, Map<Integer, Double>> getUnsavedPaymentSanctions() {
		return new HashMap<String, Map<Integer, Double>>(unsavedPaymentSanctions);
	}
	
	@Override
	public Map<String, Map<Integer, Double>> getUnsavedRevenueSanctions() {
		return new HashMap<String, Map<Integer, Double>>(unsavedRevenueSanctions);
	}

	@Override
	public Map<String, Map<Integer, Double>> getUnsavedRevenues() {
		return new HashMap<String, Map<Integer, Double>>(unsavedRevenues);
	}

	@Override
	public int getNumberOfUnsavedPayments() {
//		int count = 0;
//		for (Map.Entry<String, Map<Integer, Double>> entry : userToQueryToPayments.entrySet()) {
//			count += entry.getValue().size();
//		}
		return numberOfUnsavedPayments;
	}
	
	@Override
	public int getNumberOfUnsavedPaymentSanctions() {
		return numberOfUnsavedPaymentSanctions;
	}
	
	@Override
	public int getNumberOfUnsavedRevenueSanctions() {
		return numberOfUnsavedRevenueSanctions;
	}
	
	@Override
	public int getNumberOfUnsavedRevenues() {
		return numberOfUnsavedRevenues;
	}
	
	@Override
	public long getLastTimestampOfPersistence() {
		return lastPersistence;
	}
	
	private void checkDatabaseConnection() {
		if (!initialised) {
			initDatabaseConnection();
			initialised = true;
		}
	}
	
//	private static void initBillingMaps() {
//		try {
//			PreparedStatement statement = conn.prepareStatement("SELECT * FROM \"CurrentRevenue\"");
//
//			ResultSet rs = statement.executeQuery();
//			while (rs.next()) {
//				
//			}
//		} catch (SQLException ex) {
//			System.err.println(ex.getMessage());
//		}
//		
//	}
	
	private void initDatabaseConnection() {
		if (conn == null) {
			try {
				Class.forName("org.postgresql.Driver");
				String url = "jdbc:postgresql://localhost/Lena";
				conn = DriverManager.getConnection(url, "Lena", "");
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
//		initBillingMaps();
	}
	
	@Override
	public void persistBillingInformations() {
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
		
		Map<String, Map<Integer, Double>> paySanctionsToPersist = new HashMap<String, Map<Integer, Double>>(unsavedPaymentSanctions);
		for (Map.Entry<String, Map<Integer, Double>> entry : paySanctionsToPersist.entrySet()) {
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
		numberOfUnsavedPaymentSanctions = 0;
		
		Map<String, Map<Integer, Double>> revSanctionsToPersist = new HashMap<String, Map<Integer, Double>>(unsavedRevenueSanctions);
		for (Map.Entry<String, Map<Integer, Double>> entry : revSanctionsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Double> sanctionsMap = new HashMap<Integer, Double>(entry.getValue());
			for (Map.Entry<Integer, Double> innerEntry : sanctionsMap.entrySet()) {
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
		numberOfUnsavedRevenueSanctions = 0;
		
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
	
	public void addQueryAndUserToDatabase(IPhysicalQuery query) {
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
	
	private void prepareTuplePriceTable(IPhysicalQuery query) {
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
	
	public Map<Integer, String> getDescriptionOfCostTypes() {
		checkDatabaseConnection();
		Map<Integer, String> costTypeDescription = new HashMap<>();
		try {
			PreparedStatement statement = conn.prepareStatement("SELECT * FROM \"CostType\"");
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				costTypeDescription.put(rs.getInt(1), rs.getString(2));
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return costTypeDescription;
	}
}
