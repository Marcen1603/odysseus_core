package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.BiggestQueueStrategy;

/**
 * @author Jonas Jacobi
 */
public class BiggestQueueFactory extends AbstractSchedulingStrategyFactory {

	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		return new BiggestQueueStrategy(plan);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}

}
