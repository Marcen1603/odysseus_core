package de.uniol.inf.is.odysseus.billingmodel.physicaloperator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.billingmodel.BillingHelper;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;

public class TupleCostCalculationPipe<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	
	private Map<Integer, Map<String, Double>> queryToUserToPrice = new HashMap<>();
	private static Connection conn = null;
	private TupleCostCalculationType calculationType;
	
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
		BillingHelper.setPersistenceInterval(30 * 1000);
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
		Set<IOperatorOwner> ownerSet = new HashSet<IOperatorOwner>(this.getOwner());
		for (IOperatorOwner ow : ownerSet) {
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
					BillingHelper.getBillingManager().addRevenue(userID, queryID, (long)(tuplePrice * 10000));
					break;
				case INCOMING_TUPLES:
					BillingHelper.getBillingManager().addPayment(userID, queryID, (long)(tuplePrice * 10000));
					break;
				}
				
//				if (BillingManager.getNumberOfUnsavedRevenues() == 50)
//					BillingManager.persistBillingInformations();
				
				if(System.currentTimeMillis() - BillingHelper.getBillingManager().getLastTimestampOfPersistence() > BillingHelper.getPersistenceInterval())
					BillingHelper.getBillingManager().persistBillingInformations();
			}
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	/**
	 * gets the tuple price of the database (if its not in the cache yet)
	 * @param queryID
	 * @param userID
	 * @return the tuple price
	 */
	private double getTuplePrice(int queryID, String userID) {
		double tuplePrice = 0;
		
		if (queryToUserToPrice.get(queryID) == null) {

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
			
			Map<String, Double> map = new HashMap<String, Double>();
			map.put(userID, tuplePrice);
			queryToUserToPrice.put(queryID,  map);
		} else {
			tuplePrice = queryToUserToPrice.get(queryID).get(userID);
		}
		
		return tuplePrice;
	}

	/**
	 * 
	 * @return the calculationType
	 */
	public TupleCostCalculationType getCalculationType() {
		return calculationType;
	}

	/**
	 * 
	 * @param the calculationType to set
	 */
	public void setCalculationType(TupleCostCalculationType calculationType) {
		this.calculationType = calculationType;
	}
	
	/**
	 * tuple cost calculation types
	 * @author Lena
	 *
	 */
	public enum TupleCostCalculationType {
		INCOMING_TUPLES, OUTGOING_TUPLES
	}
}
