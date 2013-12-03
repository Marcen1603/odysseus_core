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

	private Map<String, Map<Integer, Long>> unsavedPayments = new HashMap<>(); // CostTypeID=1
	private Map<String, Map<Integer, Long>> unsavedPaymentSanctions = new HashMap<>(); // CostTypeID=2
	private Map<String, Map<Integer, Long>> unsavedRevenueSanctions = new HashMap<>(); // CostTypeID=3
	private Map<String, Map<Integer, Long>> unsavedRevenues = new HashMap<>(); // CostTypeID=4

	private int numberOfUnsavedPayments = 0;
	private int numberOfUnsavedPaymentSanctions = 0;
	private int numberOfUnsavedRevenueSanctions = 0;
	private int numberOfUnsavedRevenues = 0;
	
	private long lastPersistence = 0;
	
	private Connection conn = null;
	
	@Override
	public void addPayment(String userID, int queryID, long amount) {
		if (unsavedPayments.get(userID) == null) {
			Map<Integer, Long> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedPayments.put(userID, map);
		} else if (!unsavedPayments.get(userID).containsKey(queryID)) {
			Map<Integer, Long> map = unsavedPayments.get(userID);
			map.put(queryID, -amount);
		} else {
			Map<Integer, Long> map = unsavedPayments.get(userID);
			long newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedPayments++;
	}
	
	@Override
	public void addPaymentSanction(String userID, int queryID, long amount) {
		if (unsavedPaymentSanctions.get(userID) == null) {
			Map<Integer, Long> map = new HashMap<>();
			map.put(queryID, amount);
			unsavedPaymentSanctions.put(userID, map);
		} else if (!unsavedPaymentSanctions.get(userID).containsKey(queryID)) {
			Map<Integer, Long> map = unsavedPaymentSanctions.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Long> map = unsavedPaymentSanctions.get(userID);
			long newAmount = map.get(queryID) + amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedPaymentSanctions++;
	}
	
	@Override
	public void addRevenueSanction(String userID, int queryID, long amount) {
		if (unsavedRevenueSanctions.get(userID) == null) {
			Map<Integer, Long> map = new HashMap<>();
			map.put(queryID, -amount);
			unsavedRevenueSanctions.put(userID, map);
		} else if (!unsavedRevenueSanctions.get(userID).containsKey(queryID)) {
			Map<Integer, Long> map = unsavedRevenueSanctions.get(userID);
			map.put(queryID, -amount);
		} else {
			Map<Integer, Long> map = unsavedRevenueSanctions.get(userID);
			long newAmount = map.get(queryID) - amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedRevenueSanctions++;
	}
	
	@Override
	public void addRevenue(String userID, int queryID, long amount) {
		if (unsavedRevenues.get(userID) == null) {
			Map<Integer, Long> map = new HashMap<>();
			map.put(queryID, amount);
			unsavedRevenues.put(userID, map);
		} else if (!unsavedRevenues.get(userID).containsKey(queryID)) {
			Map<Integer, Long> map = unsavedRevenues.get(userID);
			map.put(queryID, amount);
		} else {
			Map<Integer, Long> map = unsavedRevenues.get(userID);
			long newAmount = map.get(queryID) + amount;
			map.put(queryID, newAmount);
		}
		numberOfUnsavedRevenues++;
	}
	
	@Override
	public Map<String, Map<Integer, Long>> getUnsavedPayments() {
		return new HashMap<String, Map<Integer, Long>>(unsavedPayments);
	}
	
	@Override
	public Map<String, Map<Integer, Long>> getUnsavedPaymentSanctions() {
		return new HashMap<String, Map<Integer, Long>>(unsavedPaymentSanctions);
	}
	
	@Override
	public Map<String, Map<Integer, Long>> getUnsavedRevenueSanctions() {
		return new HashMap<String, Map<Integer, Long>>(unsavedRevenueSanctions);
	}

	@Override
	public Map<String, Map<Integer, Long>> getUnsavedRevenues() {
		return new HashMap<String, Map<Integer, Long>>(unsavedRevenues);
	}

	@Override
	public int getNumberOfUnsavedPayments() {
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
	
	/**
	 * checks the database connection
	 */
	private void checkDatabaseConnection() {
		if (conn == null) {
			initDatabaseConnection();
		}
	}
	
//	/**
//	 * read database entries and initialise billing maps
//	 */
//	private void initBillingMaps() {
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
	
	/**
	 * creates a database connection (if no one exists)
	 */
	private void initDatabaseConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			String url = "jdbc:postgresql://localhost/Lena";
			conn = DriverManager.getConnection(url, "Lena", "");
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
//		initBillingMaps();
	}
	
	@Override
	public void persistBillingInformations() {
		checkDatabaseConnection();

		// update existing database entry or insert a new row
		Map<String, Map<Integer, Long>> paymentsToPersist = new HashMap<String, Map<Integer, Long>>(unsavedPayments);
		for (Map.Entry<String, Map<Integer, Long>> entry : paymentsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> paymentsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : paymentsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000.0;
				
				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE \"CurrentRevenue\" SET \"Amount\"=? WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=1");
					statement.setDouble(1, amount);
					statement.setString(2, userID);
					statement.setInt(3, queryID);
					int numberAffected = statement.executeUpdate();
					
					if (numberAffected == 0) {
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
		
		Map<String, Map<Integer, Long>> paySanctionsToPersist = new HashMap<String, Map<Integer, Long>>(unsavedPaymentSanctions);
		for (Map.Entry<String, Map<Integer, Long>> entry : paySanctionsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> sanctionsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : sanctionsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000.0;

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
		
		Map<String, Map<Integer, Long>> revSanctionsToPersist = new HashMap<String, Map<Integer, Long>>(unsavedRevenueSanctions);
		for (Map.Entry<String, Map<Integer, Long>> entry : revSanctionsToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> sanctionsMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : sanctionsMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000.0;

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
		
		Map<String, Map<Integer, Long>> revenuesToPersist = new HashMap<String, Map<Integer, Long>>(unsavedRevenues);
		for (Map.Entry<String, Map<Integer, Long>> entry : revenuesToPersist.entrySet()) {
			String userID = entry.getKey();
			int queryID;
			double amount;
			Map<Integer, Long> revenuesMap = new HashMap<Integer, Long>(entry.getValue());
			for (Map.Entry<Integer, Long> innerEntry : revenuesMap.entrySet()) {
				queryID = innerEntry.getKey();
				amount = innerEntry.getValue() / 10000.0;
				
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
	
	/**
	 * add query and user to database
	 * @param query
	 */
	public void addQueryAndUserToDatabase(IPhysicalQuery query) {
		checkDatabaseConnection();
		
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
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		// for test purposes (otherwise the individual tupleprice has to be added manually to the database according to the specific sla)
		prepareTuplePriceTable(query);
	}
	
	/**
	 * for test purposes: fill database with tuple prices
	 * @param query
	 */
	private void prepareTuplePriceTable(IPhysicalQuery query) {
		// incoming tuples / payments
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO \"TuplePrice\" (\"AccountID\", \"QueryID\", \"CostTypeID\", \"Price\") VALUES (?, ?, ?, ?)");
			statement.setString(1, query.getSession().getUser().getId());
			statement.setInt(2, query.getID());
			statement.setInt(3, 1);
			statement.setDouble(4, 0.001);
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
			statement.setDouble(4, 0.0052);
			statement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

	}
	
	/**
	 * 
	 * @return the descriptions of all existing cost types
	 */
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
