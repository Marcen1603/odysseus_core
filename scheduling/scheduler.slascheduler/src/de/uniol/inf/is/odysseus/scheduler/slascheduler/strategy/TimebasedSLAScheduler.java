package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class TimebasedSLAScheduler extends SimpleSLAScheduler {

	final private Map<IScheduling, ScheduleMeta> schedulingHistory;

	public TimebasedSLAScheduler(TimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta>();
	}

	public TimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta>();
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		super.addPlan(scheduling);
		schedulingHistory.put(scheduling, new ScheduleMeta(-1, 0, 0));
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (queue) {
			if (queue.size() > 0) {
				Map<IQuery, Double> calcedUrg = new HashMap<IQuery, Double>();
				long minTimePeriod = Long.MAX_VALUE;
				// 1. Step: Get all queries and Calc Sum of all MonitoringValues
				Set<IQuery> allQueries = new HashSet<IQuery>();
				// 2. Step: Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					ScheduleMeta scheduleMeta = schedulingHistory.get(is);
					double value = scheduleMeta.inTimeCount
							/ (scheduleMeta.allSchedulingsCount * 1.0);
					// Calc prio for each query
					for (IQuery q : is.getPlan().getQueries()) {
						// A query can be part of more than one scheduling
						// and only needs to be calculated ones
						if (calcedUrg.containsKey(q))
							continue;

						IServiceLevelAgreement sla = TenantManagement
								.getInstance().getSLAForUser(q.getUser());
						if (((ITimeBasedServiceLevelAgreement) sla)
								.getTimeperiod() < minTimePeriod) {
							minTimePeriod = ((ITimeBasedServiceLevelAgreement) sla)
									.getTimeperiod();
						}
						double urge = 0.0;
						try {
							urge = sla.getMaxOcMg(value);
						} catch (NotInitializedException e) {
							throw new RuntimeException(e);
						}
						calcedUrg.put(q, urge);
					}
					// Calc Prio for current Scheduling, based
					// on all queries
					// e.g. with Max
					double prio = 0;
					switch (method) {
					case MAX:
						prio = calcPrioMax(calcedUrg, is);
						break;
					case SUM:
						prio = calcPrioSum(calcedUrg, is);
						break;
					case AVG:
						prio = calcPrioAvg(calcedUrg, is);
						break;
					}
					// Set max as new prio
					is.getPlan().setCurrentPriority(
							(long) Math.round(prio * 100));
				}
				Collections.sort(queue, new CurrentPlanPriorityComperator());
				// Update Scheduling Infos
				IScheduling toSchedule = queue.get(0);
				// In Time if no query is overtimed
				long now = System.currentTimeMillis();
				ScheduleMeta meta = schedulingHistory.get(toSchedule);
				long last = meta.lastSchedule;
				meta.lastSchedule = now;
				if (minTimePeriod < (now - last)) {
					meta.inTimeCount++;
				}
				meta.allSchedulingsCount++;
				return toSchedule;
			} else {
				return null;
			}
		}
	}

	@Override
	public TimebasedSLAScheduler clone() {
		return new TimebasedSLAScheduler(this);
	}

}

class ScheduleMeta {
	long lastSchedule = -1;
	long inTimeCount = 0;
	long allSchedulingsCount = 0;

	public ScheduleMeta(long lastSchedule, long inTimeCount,
			long allSchedulingsCount) {
		super();
		this.lastSchedule = lastSchedule;
		this.inTimeCount = inTimeCount;
		this.allSchedulingsCount = allSchedulingsCount;
	}

}
