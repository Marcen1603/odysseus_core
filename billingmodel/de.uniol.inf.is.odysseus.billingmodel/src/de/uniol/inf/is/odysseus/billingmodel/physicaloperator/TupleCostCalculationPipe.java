package de.uniol.inf.is.odysseus.billingmodel.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class TupleCostCalculationPipe<T extends IStreamObject<?>> extends AbstractPipe<T, T> {
	
	private Map<Integer, Map<String, Double>> queryToUserToPrice;
	
	public TupleCostCalculationPipe() {
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
				
			}
		}
		
		transfer(object);
	}
	
	private double getTuplePrice(int queryID, String userID) {
		double tuplePrice = 0;
		
		if (queryToUserToPrice.get(queryID).get(userID) == null) {
//			String query = "select...from...where...";
			tuplePrice = 0.01;
			
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

}
