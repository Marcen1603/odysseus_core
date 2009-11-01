package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.AuroraMinCost;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.AuroraMinLatency;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority.PriorityMinLatency;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl.priority.SimplePriorityMinLatency;


public class AuroraFactory extends AbstractSchedulingStrategyFactory {

	static public enum AuroraStrategy{MIN_LATENCY, MIN_COST, MIN_MEM, MIN_LATENCY_PUNCTUATION, PO_MIN_LATENCY_PUNCTUATION}

	private AuroraStrategy strategy;;
	private boolean useIter;
	
	public AuroraStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AuroraStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isUseIter() {
		return useIter;
	}

	public void setUseIter(boolean useIter) {
		this.useIter = useIter;
	}

	public AuroraFactory(){
		this.strategy = AuroraStrategy.MIN_LATENCY;
		this.useIter = true;
	}
	
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary properties = context.getProperties();
		this.strategy = AuroraStrategy.valueOf((String)properties.get("aurora.strategy"));
		this.useIter = (Boolean)properties.get("execlist.useIter");
	}
	
	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		switch(strategy){
		case MIN_LATENCY:
			return new AuroraMinLatency(plan, useIter);
		case MIN_COST:
			return new AuroraMinCost(plan, useIter);
		case MIN_LATENCY_PUNCTUATION:
			return new SimplePriorityMinLatency(plan, useIter);
		case PO_MIN_LATENCY_PUNCTUATION:
			return new PriorityMinLatency(plan, useIter);
		case MIN_MEM:
			//return new AuroraMinMemory(plan);
			throw new RuntimeException("Not implemented");
		}
		
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" iter="+useIter+" strategy="+strategy;
	}

}
