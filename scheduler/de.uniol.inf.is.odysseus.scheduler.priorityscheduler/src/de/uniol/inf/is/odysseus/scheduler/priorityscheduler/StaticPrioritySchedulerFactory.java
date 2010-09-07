package de.uniol.inf.is.odysseus.scheduler.priorityscheduler;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.ISchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.priorityscheduler.prioritystrategy.StaticPriorityPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

public class StaticPrioritySchedulerFactory extends AbstractSchedulerFactory implements ISchedulerFactory {

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingStrategy) {
		return new SingleThreadSchedulerWithStrategy(schedulingStrategy, new StaticPriorityPlanScheduling());
	}

}
