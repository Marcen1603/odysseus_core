package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.SimpleSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.SimpleSLAScheduler.PrioCalcMethod;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.TimebasedSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;


public class SLAFactory extends AbstractSchedulerFactory{

	private PrioCalcMethod method;
	String kind = "";
	
	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
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
		return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new SimpleSLAScheduler(method));
	}


}
