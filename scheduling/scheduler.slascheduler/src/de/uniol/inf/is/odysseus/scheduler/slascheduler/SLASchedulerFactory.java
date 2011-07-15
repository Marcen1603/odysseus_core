package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.test.SLATestLogger;
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
	 * name of cost function
	 */
	private String costFunctionName;

	/**
	 * initializes and configures the schedulerfactory. will be called by osgi.
	 */
	@Override
	protected void activate(ComponentContext context) {
		try {
			super.activate(context);
			@SuppressWarnings("rawtypes")
			Dictionary properties = context.getProperties();
			// get settings
			this.starvationFreedomFuncName = OdysseusDefaults.get("sla_starvationFreedomFuncName");
			this.prio = new PriorityFunctionFactory()
					.buildPriorityFunction(OdysseusDefaults
							.get("sla_prioFuncName"));
			this.decaySF = Float.parseFloat(OdysseusDefaults.get("sla_starvationFreedomDecay"));
			this.querySharing = Boolean.parseBoolean(OdysseusDefaults.get("sla_querySharing"));
			this.querySharingCostModelName = OdysseusDefaults.get("sla_querySharingCostModel");
			this.costFunctionName = OdysseusDefaults.get("sla_costFunctionName");
			super.setName(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SLATestLogger.init();
	}

	/**
	 * builds a new sla scheduler
	 */
	@Override
	public IScheduler createScheduler(ISchedulingFactory schedulingFactoring) {
		return new SingleThreadSchedulerWithStrategy(schedulingFactoring,
				new SLAPartialPlanScheduling(starvationFreedomFuncName, prio,
						decaySF, querySharing, querySharingCostModelName, 
						costFunctionName));
	}

}
