package de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;

public class SingleThreadSchedulerFactory extends AbstractSchedulerFactory {

	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	@Override
	public IScheduler createScheduler(ISchedulingStrategyFactory schedulingStrategy) {
		return new SingleThreadScheduler(schedulingStrategy);
	}

}
