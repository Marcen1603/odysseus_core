package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class TimebasedSLAScheduler extends AbstractSLAScheduler {

	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	final CurrentPlanPriorityComperator comperator = new CurrentPlanPriorityComperator();

	final private long sla_history_size = OdysseusDefaults.getLong(
			"sla_history_size", 1000);


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
					if (minTime.get(is) == null) {
						long minTimeP = calcMinTimePeriod(is.getPlan());
						minTime.put(is, minTimeP);
						//logger.debug("MinTime for "+is+" set to "+minTimeP);
					}
					ScheduleMeta scheduleMeta = is.getPlan().getScheduleMeta();
					// Drain
					scheduleMeta.drainHistory(sla_history_size);
					long newPrio = calcPrio(is, scheduleMeta);
					is.getPlan().setCurrentPriority(newPrio);
					
					// Determine MaxPrio
					if (newPrio > maxPrio) {
						maxPrio = newPrio;
					}
					
				} // for (IScheduling is : queue)
				return initLastRun(maxPrio);
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

	private long calcMinTimePeriod(IPartialPlan plan) {
		long minTimePeriod = Long.MAX_VALUE;
		for (IQuery q : plan.getQueries()) {
			IServiceLevelAgreement sla = TenantManagement.getInstance()
					.getSLAForUser(q.getUser());
			if (((ITimeBasedServiceLevelAgreement) sla).getTimeperiod() < minTimePeriod) {
				minTimePeriod = ((ITimeBasedServiceLevelAgreement) sla)
						.getTimeperiod();
			}
		}// Query
		return minTimePeriod;
	}


	protected IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		ScheduleMeta meta = toSchedule.getPlan().getScheduleMeta();
		meta.scheduleDone(minTime.get(toSchedule));
		return toSchedule;
	}

	@Override
	public TimebasedSLAScheduler clone() {
		return new TimebasedSLAScheduler(this);
	}

	@Override
	public void removePlan(IScheduling plan) {
		super.removePlan(plan);
		minTime.remove(plan);
	}
	
}
