package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.SelectPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;

public class PriorityMinLatency extends AbstractPriorityMinLatency{

	public PriorityMinLatency(IPartialPlan plan, boolean useIter) {
		super(plan, useIter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void executePriorisationActivation(ISource<?> source, List<ISource<?>> opPath) {
		
		List joinPredicate = new ArrayList();
		
		for(ISource<?> op : opPath) {		
			if(op instanceof PostPriorisationPO<?>) {
				PostPriorisationPO<?> prio = (PostPriorisationPO<?>) op;
				if(!prio.isActive()) {
					prio.setActive(true);
					prio.setJoinFragment( joinPredicate);
				}
			}
			
			if(op instanceof SelectPO<?>) {
				joinPredicate.add(((SelectPO<?>) op).getPredicate());
			}
			
			/*if(op instanceof JoinTIPO<?,?>) {
				if(((JoinTIPO<?,?>) op).getJoinPredicate() != null) {
				System.out.println("JOIN: " + ((JoinTIPO<?,?>) op).getJoinPredicate().toString());
				joinPredicate.add(((JoinTIPO<?,?>) op).getJoinPredicate());
				}
			}*/
		}
	}

}
