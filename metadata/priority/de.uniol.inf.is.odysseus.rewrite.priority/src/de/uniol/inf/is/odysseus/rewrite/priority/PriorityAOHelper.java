package de.uniol.inf.is.odysseus.rewrite.priority;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.logicaloperator.base.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.priority.PostPriorisationAO;
import de.uniol.inf.is.odysseus.priority.PriorityAO;
import de.uniol.inf.is.odysseus.rewrite.drools.RestructHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PriorityAOHelper {

	@SuppressWarnings("unchecked")
	public static void searchForJoinsAndPlacePriorityAOs(AbstractLogicalOperator current, PriorityAO base) {
		Collection<LogicalSubscription> childs = current.getSubscriptions();

		
		if(current instanceof PriorityAO) {
			base = (PriorityAO) current;
		}
		
		for(LogicalSubscription sub : childs) {
			if(sub.getTarget() instanceof JoinAO) {
				handleJoin((AbstractLogicalOperator) sub.getTarget(), base);
			} else {
				searchForJoinsAndPlacePriorityAOs((AbstractLogicalOperator)sub.getTarget(), base);
			}
		} 
		
	}


	@SuppressWarnings("unchecked")
	public static  void handleJoin(AbstractLogicalOperator join, PriorityAO base) {
		
		Collection<LogicalSubscription> fathers = join.getSubscribedTo();
		IPredicate predicate = join.getPredicate();

		for(LogicalSubscription sub : fathers) {
			searchForPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, base);
		}

	}

	@SuppressWarnings("unchecked")
	public static  void searchForPriorityAOPosistion(AbstractLogicalOperator current, IPredicate predicate, PriorityAO base) {
		if(current instanceof PriorityAO) {
			return;
		}

		if(current instanceof JoinAO) {
			handleJoin(current, base);
		}

		if(isCriticalAO(current) || 
			current.getSubscribedTo() == null || 
				current.getSubscribedTo().size() == 0) {
			PostPriorisationAO prioAO = new PostPriorisationAO();

			// Nur die Default-Prioritaet setzen. Den Rest macht dann die jeweilige Strategie
			prioAO.setDefaultPriority(base.getDefaultPriority());
			// Operator erst einmal deaktivieren, damit er nicht die normale Berechnung stört
			prioAO.setActive(false);
			insertPriorityAO(current, prioAO);
			base.getCopartners().add(prioAO);

		} else {
			Collection<LogicalSubscription> fathers = current.getSubscribedTo();
			for(LogicalSubscription sub : fathers) {
				searchForPriorityAOPosistion((AbstractLogicalOperator) sub.getTarget(), predicate, base);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	public static  void insertPriorityAO(AbstractLogicalOperator current, PostPriorisationAO prioAO) {	
		for(LogicalSubscription sub :  current.getSubscriptions()) {
			sub.getTarget().unsubscribeSubscriptionTo(current, sub.getSinkPort(),sub.getSourcePort());
			prioAO.subscribe(sub.getTarget(), sub.getSinkPort(),sub.getSourcePort(), sub.getInputSchema());
		}

		current.subscribe(prioAO, 0, 0, current.getOutputSchema());	
	}

	public static boolean isCriticalAO(AbstractLogicalOperator current) {
		if(current instanceof TopAO || current instanceof ProjectAO || current instanceof BinaryLogicalOp) {
			return true;
		}
		
		return false;
	}	
	
}
