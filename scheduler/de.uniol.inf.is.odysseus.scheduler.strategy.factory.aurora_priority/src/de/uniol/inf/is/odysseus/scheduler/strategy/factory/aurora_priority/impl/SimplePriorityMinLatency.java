package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority.impl;

import java.util.Iterator;
import java.util.List;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.priority.IPostPriorisationPipe;
import de.uniol.inf.is.odysseus.priority.PostPriorisationPO;
import de.uniol.inf.is.odysseus.priority.PriorityPO;

public class SimplePriorityMinLatency extends AbstractPriorityMinLatency{

	public SimplePriorityMinLatency(IPartialPlan plan, boolean useIter) {
		super(plan, useIter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void executePriorisationActivation(ISource<?> source, List<ISource<?>> opPath) {
		// Muss nichts weiter machen, als copartners zu entfernen, da die Strategie nur Teilplaene bevorzugt,
		// die zu einem Join fuehren, dass priorisierte Datenstromelemente enthalten koennte.
		
		for(ISource<?> op : opPath) {		
			if(op instanceof PriorityPO<?>) {
				PriorityPO<?> prio = (PriorityPO<?>) op;
				if(prio.getCopartners() != null) {
					
					Iterator<IPostPriorisationPipe<?>> it = prio.getCopartners().iterator();
					
					while(it.hasNext()) {
						IPostPriorisationPipe<?> each = it.next();
						if(each instanceof PostPriorisationPO) {
							each.setActive(false);
							it.remove();
						}
					}

				}
			}
			
		}	
		
	}

}
