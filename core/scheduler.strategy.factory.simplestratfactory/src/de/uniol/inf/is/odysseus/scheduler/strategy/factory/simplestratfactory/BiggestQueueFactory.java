package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.BiggestQueueScheduling;

/**
 * @author Jonas Jacobi
 */
public class BiggestQueueFactory extends AbstractSchedulingFactory {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public IScheduling create(IPartialPlan plan, int priority) {
		return new BiggestQueueScheduling(plan);
	}
	
	@Override
	public String toString() {
		return this.getClass().getName();
	}

}
