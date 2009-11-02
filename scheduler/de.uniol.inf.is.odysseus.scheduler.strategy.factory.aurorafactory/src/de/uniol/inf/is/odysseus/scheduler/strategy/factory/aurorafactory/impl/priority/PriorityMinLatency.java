package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority;

import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;

public class PriorityMinLatency extends AbstractPriorityMinLatency{

	public PriorityMinLatency(IPartialPlan plan, boolean useIter) {
		super(plan, useIter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void executePriorisationActivation(ISource<?> source, List<ISource<?>> opPath) {
		
		IPredicate joinPredicate = null;
		
		for(ISource<?> op : opPath) {		
			if(op instanceof PostPriorisationPO<?>) {
				PostPriorisationPO<?> prio = (PostPriorisationPO<?>) op;
				if(!prio.isActive()) {
					prio.setActive(true);
					prio.setJoinFragment( joinPredicate);
				}
			}
			
			if(op instanceof JoinTIPO<?,?>) {
				joinPredicate = ((JoinTIPO<?,?>) op).getJoinPredicate();
			}
		}
	}

}
