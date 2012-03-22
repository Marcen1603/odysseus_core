package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.scheduler.AbstractSchedulerFactory;
import de.uniol.inf.is.odysseus.core.server.scheduler.IScheduler;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.factory.ISchedulingFactory;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.SingleThreadSchedulerWithStrategy;

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
			this.starvationFreedomFuncName = OdysseusConfiguration.get("sla_starvationFreedomFuncName");
			this.prio = new PriorityFunctionFactory()
					.buildPriorityFunction(OdysseusConfiguration
							.get("sla_prioFuncName"));
			this.decaySF = Float.parseFloat(OdysseusConfiguration.get("sla_starvationFreedomDecay"));
			this.querySharing = Boolean.parseBoolean(OdysseusConfiguration.get("sla_querySharing"));
			this.querySharingCostModelName = OdysseusConfiguration.get("sla_querySharingCostModel");
			this.costFunctionName = OdysseusConfiguration.get("sla_costFunctionName");
			super.setName(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		SLATestLogger.init();
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
