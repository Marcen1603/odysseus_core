package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl.DynamicRandomScheduling;

public class DynamicRandomSchedulingFactory extends AbstractSchedulingFactory {

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}
	
	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		return new DynamicRandomScheduling(plan);
	}

}
