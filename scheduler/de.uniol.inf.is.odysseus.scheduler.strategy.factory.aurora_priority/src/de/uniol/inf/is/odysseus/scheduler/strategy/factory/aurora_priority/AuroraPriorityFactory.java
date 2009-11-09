package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.ISchedulingStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.AbstractSchedulingStrategyFactory;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority.impl.AuroraMinLatencyLoadShedding;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority.impl.PriorityMinLatency;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurora_priority.impl.SimplePriorityMinLatency;


public class AuroraPriorityFactory extends AbstractSchedulingStrategyFactory {

	static public enum AuroraPriorityStrategy{MIN_LATENCY_PUNCTUATION, PO_MIN_LATENCY_PUNCTUATION,MIN_LATENCY_LOAD_SHEDDING}

	private AuroraPriorityStrategy strategy;;
	private boolean useIter;
	
	public AuroraPriorityStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AuroraPriorityStrategy strategy) {
		this.strategy = strategy;
	}

	public boolean isUseIter() {
		return useIter;
	}

	public void setUseIter(boolean useIter) {
		this.useIter = useIter;
	}

	public AuroraPriorityFactory(){
		this.strategy = AuroraPriorityStrategy.MIN_LATENCY_PUNCTUATION;
		this.useIter = true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary properties = context.getProperties();
		this.strategy = AuroraPriorityStrategy.valueOf((String)properties.get("aurora_priority.strategy"));
		this.useIter = (Boolean)properties.get("execlist.useIter");
	}
	
	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		switch(strategy){
		case MIN_LATENCY_PUNCTUATION:
			return new SimplePriorityMinLatency(plan, useIter);
		case PO_MIN_LATENCY_PUNCTUATION:
			return new PriorityMinLatency(plan, useIter);
		case MIN_LATENCY_LOAD_SHEDDING:
			return new AuroraMinLatencyLoadShedding(plan,useIter);
		}
		
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" iter="+useIter+" strategy="+strategy;
	}

}