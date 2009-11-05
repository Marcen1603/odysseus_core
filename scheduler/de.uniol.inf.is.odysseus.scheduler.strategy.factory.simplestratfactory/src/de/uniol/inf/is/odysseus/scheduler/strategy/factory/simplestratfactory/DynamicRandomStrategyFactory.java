package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.DynamicRandomStrategy;

public class DynamicRandomStrategyFactory extends AbstractSchedulingStrategyFactory {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		return new DynamicRandomStrategy(plan);
	}

}
