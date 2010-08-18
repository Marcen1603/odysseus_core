package de.uniol.inf.is.odysseus.scheduler.priorityscheduler;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy.StaticPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

public class StaticPriorityPlanSchedulingFactory extends AbstractSchedulerFactory {

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new StaticPriorityPlanScheduling());
	}

}
