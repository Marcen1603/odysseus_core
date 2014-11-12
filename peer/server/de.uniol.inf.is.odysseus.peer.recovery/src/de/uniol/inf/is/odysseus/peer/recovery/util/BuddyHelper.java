package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * A helper-class with methods specifically for the buddy-concept
 * @author Tobias Brandt
 *
 */
public class BuddyHelper {

	/**
	 * Determines, if we need a buddy for the part with the given PQL
	 * TODO Redefine with respect to the issue Michael found
	 * @param pql The PQL of the part of the shared query on this peer 
	 * @return true, if you need a buddy, false, if not
	 */
	public static boolean needBuddy(String pql) {
		boolean foundReceiver = false;
		List<ILogicalQuery> logicalQueries = RecoveryHelper.convertToLogicalQueries(pql);
		for (ILogicalQuery query : logicalQueries) {
			for (ILogicalOperator op : LogicalQueryHelper.getAllOperators(query)) {
				if (op instanceof JxtaReceiverAO) {
					foundReceiver = true;
					break;
				}
			}
			if (!foundReceiver) {
				// There is a part without a Receiver -> We need a buddy
				return true;				
			}
			foundReceiver = false;
		}
		return false;
	}
	
}
