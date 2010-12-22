package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class TimebasedSLAScheduler extends AbstractTimebasedSLAScheduler{


	final CurrentPlanPriorityComperator comperator = new CurrentPlanPriorityComperator();

	public TimebasedSLAScheduler(TimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
	}

	public TimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
	}

	@Override
	public IScheduling nextPlan() {
		IScheduling nextPlan = super.nextPlan();
		if (nextPlan != null) return nextPlan;
		
		synchronized (queue) {
			if (queue.size() > 0) {
				long maxPrio = 0;
				// Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					calcMinTime(is);
					ScheduleMeta scheduleMeta = drainHistory(is);
					long newPrio = calcPrio(is, scheduleMeta);
					is.getPlan().setCurrentPriority(newPrio);
					
					// Determine MaxPrio
					if (newPrio > maxPrio) {
						maxPrio = newPrio;
					}
					
				} // for (IScheduling is : queue)
				return initLastRunAndReturn(maxPrio);
			} else {
				return null;
			}
		}
	}

	private long calcPrio(IScheduling is, ScheduleMeta scheduleMeta) {
		Map<IQuery, Double> queryCalcedUrg = new HashMap<IQuery, Double>();
		for (IQuery q : is.getPlan().getQueries()) {
			try {
				IServiceLevelAgreement sla = TenantManagement
						.getInstance().getSLAForUser(q.getUser());
				double rate = scheduleMeta.getRate();
				double urge = sla.getMaxOcMg(rate);
				queryCalcedUrg.put(q, urge);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		}// Query
		// Calc Prio for current Scheduling, based
		// on all queries
		double prio = calcPrio(queryCalcedUrg, is);
		return Math.round(prio * 1000);
	}


	@Override
	public TimebasedSLAScheduler clone() {
		return new TimebasedSLAScheduler(this);
	}

	
}
