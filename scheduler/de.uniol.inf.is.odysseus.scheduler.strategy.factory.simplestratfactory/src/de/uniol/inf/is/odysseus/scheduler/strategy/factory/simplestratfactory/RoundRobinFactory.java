package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.RoundRobinStrategy;


public class RoundRobinFactory extends AbstractSchedulingStrategyFactory {

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
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		return new RoundRobinStrategy(plan);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}
}
