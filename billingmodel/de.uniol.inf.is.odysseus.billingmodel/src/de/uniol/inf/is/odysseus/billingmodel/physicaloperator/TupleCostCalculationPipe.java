package de.uniol.inf.is.odysseus.billingmodel.physicaloperator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.billingmodel.BillingHelper;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class TupleCostCalculationPipe<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	
	private Map<Integer, Map<String, Double>> queryToUserToPrice = new HashMap<>();
	private static Connection conn = null;
	private TupleCostCalculationType calculationType;
	private long persistenceInterval = 60 * 1000;
	
	public TupleCostCalculationPipe(TupleCostCalculationType type) {
		if (conn == null) {
			try {
				Class.forName("org.postgresql.Driver");
				String url = "jdbc:postgresql://localhost/Lena";
				conn = DriverManager.getConnection(url, "Lena", "");
			} catch (Exception ex) {
				System.err.println(ex.getMessage());
			}
		}
		calculationType = type;
	}

	public TupleCostCalculationPipe(AbstractPipe<T, T> pipe) {
		super(pipe);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		transfer(object);
		
		// get owner (query) and user of the operator for calculating the tuple price
		for (IOperatorOwner ow : this.getOwner()) {
			IUser user = null;
			int queryID = ow.getID();
			double tuplePrice = 0;
			
			if (ow instanceof ILogicalQuery)
				user = ((ILogicalQuery) ow).getUser().getUser();
			else if (ow instanceof IPhysicalQuery)
				user = ((IPhysicalQuery)ow).getSession().getUser();
			
			if (user != null) {
				String userID = user.getId();
				
				tuplePrice = getTuplePrice(queryID, userID);
				switch (calculationType) {
				case OUTGOING_TUPLES:
					BillingHelper.getBillingManager().addRevenue(userID, queryID, tuplePrice);
					break;
				case INCOMING_TUPLES:
					BillingHelper.getBillingManager().addPayment(userID, queryID, tuplePrice);
					break;
				}
				
//				if (BillingManager.getNumberOfUnsavedRevenues() == 10)
//					BillingManager.persistBillingInformations();
				
				if(System.currentTimeMillis() - BillingHelper.getBillingManager().getLastTimestampOfPersistence() > persistenceInterval)
					BillingHelper.getBillingManager().persistBillingInformations();
			}
		}
	}
	
	private double getTuplePrice(int queryID, String userID) {
		double tuplePrice = 0;
		
		if (queryToUserToPrice.get(queryID) == null) { //.get(userID) == null) {

			try {
				PreparedStatement statement = conn.prepareStatement("SELECT \"Price\" FROM \"TuplePrice\" WHERE \"AccountID\"=? and \"QueryID\"=? and \"CostTypeID\"=?");
				statement.setString(1, userID);
				statement.setInt(2, queryID);
				switch (calculationType) {
				case INCOMING_TUPLES:
					statement.setInt(3, 1);
					break;
				case OUTGOING_TUPLES:
					statement.setInt(3, 4);
					break;
				}
				ResultSet rs = statement.executeQuery();
				while (rs.next())
			      {
			        tuplePrice = rs.getDouble(1);
			      }
			} catch (SQLException ex) {
				System.err.println(ex.getMessage());
			}
			
//			String query = "SELECT price FROM \"Test\" WHERE id=1";
//		    try
//		    {
//		      Statement st = conn.createStatement();
//		      ResultSet rs = st.executeQuery(query);
//		      while (rs.next())
//		      {
//		        tuplePrice = rs.getDouble("price");
//		      }
//		    }
//		    catch (SQLException ex)
//		    {
//		      System.err.println(ex.getMessage());
//		    }
			
//			tuplePrice = 0.01;
			
			Map<String, Double> map = new HashMap<String, Double>();
			map.put(userID, tuplePrice);
			queryToUserToPrice.put(queryID,  map);
		} else {
			tuplePrice = queryToUserToPrice.get(queryID).get(userID);
		}
		
		return tuplePrice;
	}

	@Override
	public AbstractPipe<T, T> clone() {
		return new TupleCostCalculationPipe<T>(this);
	}

	public TupleCostCalculationType getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(TupleCostCalculationType calculationType) {
		this.calculationType = calculationType;
	}

	public long getPersistenceInterval() {
		return persistenceInterval;
	}

	public void setPersistenceInterval(int persistenceInterval) {
		this.persistenceInterval = persistenceInterval;
	}
	
	public enum TupleCostCalculationType {
		INCOMING_TUPLES, OUTGOING_TUPLES
	}
}
