package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.SimpleSLAScheduler;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy.SimpleSLAScheduler.PrioCalcMethod;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;


public class SLAFactory extends AbstractSchedulerFactory{

	private PrioCalcMethod method;
	
	@Override
	@SuppressWarnings("unchecked")
	protected void activate(ComponentContext context){
		super.activate(context);
		Dictionary properties = context.getProperties();
		this.method = PrioCalcMethod.valueOf((String)properties.get("calcMethod"));
		super.setName(properties);
	}

	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		return new SingleThreadSchedulerWithStrategy(schedulingFactoring, new SimpleSLAScheduler(method));
	}


}
