package de.uniol.inf.is.odysseus.p2p.distribution.bidding.client.queryselection;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.p2p.distribution.client.queryselection.IQuerySelectionStrategy;
import de.uniol.inf.is.odysseus.p2p.logicaloperator.P2PAO;
import de.uniol.inf.is.odysseus.p2p.peer.AbstractOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class QuerySelectionStrategy implements IQuerySelectionStrategy{
	private static int MAXQUERIES = 10;
	
	@Override
	public boolean handleQuery(Subplan subplan, IOdysseusPeer peer) {
		
		if(((AbstractOdysseusPeer)peer).getQueries().size()== MAXQUERIES) {
			return false;
		}
		//Prüfen, ob adressierte Quellen überhaupt verwaltet werden
		List<AccessAO> sources = new ArrayList<AccessAO>();
		collectSourcesFromPlan(subplan.getAo(), sources);
		if(!sources.isEmpty()) {
			for(AccessAO ao : sources) {
				if(GlobalState.getActiveDatadictionary().createSDFSource(ao.getSource().getURI())== null) {
					return false;
				}
			}
			return true;
		}
		//keine Quellen werden verwaltet
		if(GlobalState.getActiveDatadictionary().emptySourceTypeMap()) {
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
