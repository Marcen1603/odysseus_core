package de.uniol.inf.is.odysseus.rewrite.priority;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.priority.PostPriorisationAO;
import de.uniol.inf.is.odysseus.priority.PriorityAO;

public class PriorityAOHelper {

	@SuppressWarnings("unchecked")
	public synchronized static void searchForJoinsAndPlacePostPriorityAOs(AbstractLogicalOperator current, PriorityAO base) {

		if(current instanceof PriorityAO) {
			base = (PriorityAO) current;
		}
		
		Iterator<LogicalSubscription> it = current.getSubscriptions().iterator();
		
		while(it.hasNext()) {
			LogicalSubscription sub = it.next();
			
			if(sub.getTarget() instanceof JoinAO) {
				handleJoin((AbstractLogicalOperator) sub.getTarget(), base);
			} else {
				searchForJoinsAndPlacePostPriorityAOs((AbstractLogicalOperator)sub.getTarget(), base);
			}
		} 
		
	}


	@SuppressWarnings("unchecked")
	public synchronized static  void handleJoin(AbstractLogicalOperator join, PriorityAO base) {
		
		IPredicate predicate = join.getPredicate();

		Iterator<LogicalSubscription> it = join.getSubscribedTo().iterator();
		
		while(it.hasNext()) {
			LogicalSubscription sub = it.next();
			searchForPostPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, base, new ArrayList<IPredicate>());
		}

	}

	@SuppressWarnings("unchecked")
	public synchronized static  void searchForPostPriorityAOPosistion(AbstractLogicalOperator current, IPredicate predicate, PriorityAO base,
			List<IPredicate> fragments) {
		if(current instanceof PriorityAO) {
			return;
		}

		if(current instanceof JoinAO) {
			handleJoin(current, base);
		}
		
		if(current instanceof SelectAO) {
			fragments.add(current.getPredicate());
		}

		if(isCriticalAO(current) || 
			current.getSubscribedTo() == null || 
				current.getSubscribedTo().size() == 0) {
			PostPriorisationAO prioAO = new PostPriorisationAO();

			// Nur die Default-Prioritaet setzen. Den Rest macht dann die jeweilige Strategie
			prioAO.setDefaultPriority(base.getDefaultPriority());
			// Operator erst einmal deaktivieren, damit er nicht die normale Berechnung stört
			prioAO.setActive(false);
			prioAO.setPredicates(fragments);
			insertPostPriorityAO(current, prioAO);
			base.getCopartners().add(prioAO);

		} else {
			Iterator<LogicalSubscription> it = current.getSubscribedTo().iterator();
			
			while(it.hasNext()) {
				LogicalSubscription sub = it.next();
				searchForPostPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, base, fragments);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public synchronized static  void insertPostPriorityAO(AbstractLogicalOperator current, PostPriorisationAO prioAO) {	
		Iterator<LogicalSubscription> it = current.getSubscriptions().iterator();
		
		while(it.hasNext()) {
			LogicalSubscription sub = it.next();
			//sub.getTarget().unsubscribeSubscriptionTo(current, sub.getSinkPort(),sub.getSourcePort());
			prioAO.subscribe(sub.getTarget(), sub.getSinkPort(),sub.getSourcePort(), sub.getInputSchema());
		}

		current.subscribe(prioAO, 0, 0, current.getOutputSchema());	
	}

	public synchronized static boolean isCriticalAO(AbstractLogicalOperator current) {
		if(current instanceof TopAO || current instanceof ProjectAO || current instanceof BinaryLogicalOp) {
			return true;
		}
		
		return false;
	}	
	
}
