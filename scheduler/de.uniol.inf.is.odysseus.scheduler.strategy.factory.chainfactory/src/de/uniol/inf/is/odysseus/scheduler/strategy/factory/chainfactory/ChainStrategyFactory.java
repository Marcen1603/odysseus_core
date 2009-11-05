package de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.chainfactory.impl.ChainStrategy;

public class ChainStrategyFactory extends AbstractSchedulingStrategyFactory {
	
	private boolean useIter = true;

	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary properties = context.getProperties();
		this.useIter = (Boolean)properties.get("execlist.useIter");
	}

	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		return new ChainStrategy(plan, useIter);
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" iter="+useIter;
	}
	
}
