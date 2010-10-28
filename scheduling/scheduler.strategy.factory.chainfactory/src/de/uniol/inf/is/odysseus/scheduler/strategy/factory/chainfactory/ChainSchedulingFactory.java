package de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory.impl.ChainScheduling;

public class ChainSchedulingFactory extends AbstractSchedulingFactory {
	

	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
	}

	@Override
	public IScheduling create(IPartialPlan plan, long priority) {
		return new ChainScheduling(plan);
	}

	@Override
	public String toString() {
		return this.getClass().getName();
	}
	
}
