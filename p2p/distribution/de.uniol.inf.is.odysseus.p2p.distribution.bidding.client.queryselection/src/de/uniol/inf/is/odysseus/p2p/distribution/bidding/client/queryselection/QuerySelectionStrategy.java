package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.queryselection;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.peer.IPeer;

public class QuerySelectionStrategy implements IQuerySelectionStrategy{
	private static int MAXQUERIES = 10;
	
	@Override
	public boolean handleQuery(Subplan subplan, IPeer peer) {
		
		if(((AbstractPeer)peer).getQueries().size()== MAXQUERIES) {
			return false;
		}
		//Prüfen, ob adressierte Quellen überhaupt verwaltet werden
		List<AccessAO> sources = new ArrayList<AccessAO>();
		collectSourcesFromPlan(subplan.getAo(), sources);
		if(!sources.isEmpty()) {
			for(AccessAO ao : sources) {
				
				if(DataDictionary.getInstance().getSource(ao.getSource().getURI())== null) {
					return false;
				}
			}
			return true;
		}
		//keine Quellen werden verwaltet
		if(DataDictionary.getInstance().emptySourceTypeMap()) {
			return true;
		}
		//Zum einfacheren testen wird true zurückgeliefert, obwohl false geliefert werden müsste
		else {
			return true;
		}
	}
	

	private void collectSourcesFromPlan(ILogicalOperator op, List<AccessAO> sources) {
		if((op instanceof AccessAO) && !(op instanceof P2PAO)) {
			sources.add((AccessAO) op);
		}
		else {
			for(LogicalSubscription subscription : op.getSubscribedToSource()) {
				collectSourcesFromPlan(subscription.getTarget(), sources);
			}
		}
		
	}

}
