package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.DynamicRandomScheduling;

public class DynamicRandomSchedulingFactory extends AbstractSchedulingFactory {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	@Override
	public IScheduling createStrategy(IPartialPlan plan, int priority) {
		return new DynamicRandomScheduling(plan);
	}

}
