package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import java.util.Dictionary;


import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.RoundRobinStrategy;


public class RoundRobinFactory extends AbstractSchedulingStrategyFactory {

	protected Logger logger;

	boolean useIter = true;
	
	public RoundRobinFactory() {
		this.logger = LoggerFactory.getLogger(RoundRobinFactory.class);
		//logger.info("Round Robin Factory created");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		// TODO: Warum ist das hier so?
		//Dictionary properties = context.getProperties();
		//useIter = (Boolean) properties.get("execlist.useIter");	
		useIter = true;
	}

	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		return new RoundRobinStrategy(plan, useIter);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+" iter="+useIter;
	}
}
