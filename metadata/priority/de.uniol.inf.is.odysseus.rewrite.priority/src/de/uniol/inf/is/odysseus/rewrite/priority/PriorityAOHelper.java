package de.uniol.inf.is.odysseus.rewrite.priority;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.priority.PriorityAO;

public class PriorityAOHelper {

	@SuppressWarnings("unchecked")
	public static void searchForJoinsAndPlacePriorityAOs(AbstractLogicalOperator current, Map priorities) {
		Collection<LogicalSubscription> childs = current.getSubscriptions();

		for(LogicalSubscription sub : childs) {
			if(sub.getTarget() instanceof JoinAO) {
				handleJoin((AbstractLogicalOperator) sub.getTarget(), priorities);
			} else {
				searchForJoinsAndPlacePriorityAOs((AbstractLogicalOperator)sub.getTarget(), priorities);
			}
		} 
		
	}


	@SuppressWarnings("unchecked")
	public static  void handleJoin(AbstractLogicalOperator join, Map priorities) {
		
		Collection<LogicalSubscription> fathers = join.getSubscribedTo();
		IPredicate predicate = join.getPredicate();

		for(LogicalSubscription sub : fathers) {
			searchForPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, priorities);
		}

	}

	@SuppressWarnings("unchecked")
	public static  void searchForPriorityAOPosistion(AbstractLogicalOperator current, IPredicate predicate, Map priorities) {
		if(current instanceof PriorityAO) {
			return;
		}

		if(current instanceof JoinAO) {
			handleJoin(current, priorities);
		}

		if(isCriticalAO(current) || 
			current.getSubscribedTo() == null || 
				current.getSubscribedTo().size() == 0) {
			PriorityAO prioAO = new PriorityAO();

			System.out.println("Praedikat: " + predicate);
			prioAO.setPriorities(priorities);
			
			insertPriorityAO(current, prioAO);

		} else {
			Collection<LogicalSubscription> fathers = current.getSubscribedTo();
			for(LogicalSubscription sub : fathers) {
				searchForPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, priorities);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public static  void insertPriorityAO(AbstractLogicalOperator current, PriorityAO prioAO) {
		for(LogicalSubscription sub :  current.getSubscriptions()) {
			sub.getTarget().unsubscribeSubscriptionTo(current, sub.getSinkPort(),sub.getSourcePort());
			prioAO.subscribe(sub.getTarget(), sub.getSinkPort(),sub.getSourcePort());
		}
		current.subscribe(prioAO, 0, 0);	
	}

	public static  boolean isCriticalAO(AbstractLogicalOperator current) {
		if(current instanceof TopAO) {
			return true;
		}
		
		return false;
	}	
	
}
