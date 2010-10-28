package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.RoundRobinScheduling;


public class RoundRobinFactory extends AbstractSchedulingFactory {

	protected Logger logger;
	
	public RoundRobinFactory() {
		this.logger = LoggerFactory.getLogger(RoundRobinFactory.class);
		//logger.info("Round Robin Factory created");
	}
	
	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		return new RoundRobinScheduling(plan);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
