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
	
	public AuroraPriorityStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(AuroraPriorityStrategy strategy) {
		this.strategy = strategy;
	}


	public AuroraPriorityFactory(){
		this.strategy = AuroraPriorityStrategy.MIN_LATENCY_PUNCTUATION;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary properties = context.getProperties();
		this.strategy = AuroraPriorityStrategy.valueOf((String)properties.get("aurora_priority.strategy"));
	}
	
	@Override
	public ISchedulingStrategy createStrategy(IPartialPlan plan, int priority) {
		switch(strategy){
		case MIN_LATENCY_PUNCTUATION:
			return new SimplePriorityMinLatency(plan);
		case PO_MIN_LATENCY_PUNCTUATION:
			return new PriorityMinLatency(plan);
		case MIN_LATENCY_LOAD_SHEDDING:
			return new AuroraMinLatencyLoadShedding(plan);
		}
		
		return null;
	}

	@Override
	public String toString() {
		return this.getClass().getName()+" strategy="+strategy;
	}

}