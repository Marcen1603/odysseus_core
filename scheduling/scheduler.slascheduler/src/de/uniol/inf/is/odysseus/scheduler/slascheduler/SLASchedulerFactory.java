package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadTrainSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

public class SLASchedulerFactory extends AbstractSchedulerFactory {
	
	private String starvationFreedomFuncName;
	private IPriorityFunction prio;
	
	@Override
	protected void activate(ComponentContext context){
		super.activate(context);
		@SuppressWarnings("rawtypes")
		Dictionary properties = context.getProperties();
		// get settings from properties 
		this.starvationFreedomFuncName = (String) properties.get(
				"sla_starvationFreedomFuncName");
		this.prio = new PriorityFunctionFactory().buildPriorityFunction(
				(String) properties.get("sla_prioFuncName"));
		super.setName(properties);
	}

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		return new SingleThreadTrainSchedulerWithStrategy(schedulingFactoring, 
				new SLAPartialPlanScheduling(starvationFreedomFuncName, prio));
	}

}
