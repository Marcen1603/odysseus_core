package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import javax.management.RuntimeErrorException;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.AbstractSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.AbstractSLAScheduler.PrioCalcMethod;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.PrioSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.TimebasedSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;


public class SLAFactory extends AbstractSchedulerFactory{

	private PrioCalcMethod method;
	String kind = "";
	
	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
		@SuppressWarnings("rawtypes")
		Dictionary properties = context.getProperties();
		this.method = PrioCalcMethod.valueOf((String)properties.get("calcMethod"));
		this.kind = (String)properties.get("kind");
		super.setName(properties);
	}

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		if ("time".equals(kind)){
			return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new TimebasedSLAScheduler(method));
		}
		if ("prio".equals(kind)){
			return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new PrioSLAScheduler(method));			
		}
		throw new RuntimeException("Scheduler "+kind+" "+method+" could not be created ");
	}


}
