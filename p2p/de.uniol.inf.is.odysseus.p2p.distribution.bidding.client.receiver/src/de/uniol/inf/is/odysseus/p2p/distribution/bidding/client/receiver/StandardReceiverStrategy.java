package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.receiver;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.distribution.client.receiver.IReceiverStrategy;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;

public class StandardReceiverStrategy implements IReceiverStrategy{
	private static int MAXQUERIES = 10;
	
	@Override
	public boolean handleQuery(Query query, AbstractPeer peer) {
		
		if(peer.getQueries().size()== MAXQUERIES) {
			return false;
		}
		//Prüfen, ob adressierte Quellen überhaupt verwaltet werden
		List<AccessAO> sources = new ArrayList<AccessAO>();
		for(Subplan subplan :  query.getSubPlans().values()) {
			
			collectSourcesFromPlan(subplan.getAo(), sources);
		}
		if(!sources.isEmpty()) {
			for(AccessAO ao : sources) {
				if(DataDictionary.getInstance().getSource(ao.getSourceType())== null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	

	private void collectSourcesFromPlan(ILogicalOperator op, List<AccessAO> sources) {
		if((op instanceof AccessAO) && !(op instanceof P2PAO)) {
			sources.add((AccessAO) op);
		}
		else {
			for(LogicalSubscription subscription : op.getSubscribedTo()) {
				collectSourcesFromPlan(subscription.getTarget(), sources);
			}
		}
		
	}

}
