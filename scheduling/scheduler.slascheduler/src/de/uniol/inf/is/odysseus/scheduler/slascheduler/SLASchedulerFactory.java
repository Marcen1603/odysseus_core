package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadTrainSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.strategy.factory.ISchedulingFactory;

/**
 * Factory for builing SLA schedulers
 * 
 * @author tommy
 * 
 */
public class SLASchedulerFactory extends AbstractSchedulerFactory {
	/**
	 * the name of the starvation freedom function that should be used by the
	 * scheduler
	 */
	private String starvationFreedomFuncName;
	/**
	 * a priority function that should be used for priorizing the schedulable
	 * objects
	 */
	private IPriorityFunction prio;
	/**
	 * decay of starvation freedom function
	 */
	private double decaySF;
	/**
	 * true iff query sharing should be considered in scheduling
	 */
	private boolean querySharing;
	/**
	 * name of the cost model that should be used, if query sharing should be 
	 * considered in scheduling
	 */
	private String querySharingCostModelName;

	/**
	 * initializes and configures the schedulerfactory. will be called by osgi.
	 */
	@Override
	protected void activate(ComponentContext context) {
		super.activate(context);
		@SuppressWarnings("rawtypes")
		Dictionary properties = context.getProperties();
		// get settings from properties
		this.starvationFreedomFuncName = (String) properties
				.get("sla_starvationFreedomFuncName");
		this.prio = new PriorityFunctionFactory()
				.buildPriorityFunction((String) properties
						.get("sla_prioFuncName"));
		this.decaySF = Float.parseFloat((String)properties.get("sla_stavationFreedomDecay"));
		this.querySharing = Boolean.parseBoolean((String)properties.get("sla_querySharing"));
		this.querySharingCostModelName = (String)properties.get("sla_querySharingCostModel");
		super.setName(properties);
		System.err.println("SLA Scheduler Factory activated!!!");
	}

	/**
	 * builds a new sla scheduler
	 */
	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		return new SingleThreadTrainSchedulerWithStrategy(schedulingFactoring,
				new SLAPartialPlanScheduling(starvationFreedomFuncName, prio,
						decaySF, querySharing, querySharingCostModelName));
	}

}
