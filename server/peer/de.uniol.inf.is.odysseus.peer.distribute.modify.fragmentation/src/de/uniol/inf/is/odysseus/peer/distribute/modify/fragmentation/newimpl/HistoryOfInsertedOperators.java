package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

/**
 * A class holding pairs of inserted operators and broken subscriptions due to the insertion.
 * @author Michael Brand
 */
public class HistoryOfInsertedOperators extends ArrayList<IPair<ILogicalOperator, LogicalSubscription>> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1L;

}