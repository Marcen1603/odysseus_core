package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.planmanagement.optimization.plan.ScheduleMeta;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class TimebasedSLAScheduler extends SimpleSLAScheduler {

	static private Logger logger = LoggerFactory
			.getLogger(TimebasedSLAScheduler.class);

	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();
	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	final CurrentPlanPriorityComperator comperator = new CurrentPlanPriorityComperator();
	
	final private long sla_history_size = Long.parseLong(OdysseusDefaults
			.get("sla_history_size"));
	final private long sla_update_Penalties_Frequency = Long
			.parseLong(OdysseusDefaults.get("sla_update_Penalties_Frequency"));

	private long toUpdateCounter = 0;

	public TimebasedSLAScheduler(TimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
	}

	public TimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			scheduling.addSchedulingEventListener(this);
			scheduling.getPlan().setScheduleMeta(
					new ScheduleMeta(System.currentTimeMillis()));
			logger.debug("Adding Plan done. Current Plan count " + queue.size());
		}
	}

	@Override
	public IScheduling nextPlan() {
		updatePenalties();
		synchronized (lastRun) {
			if (lastRun.size() > 0) {
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
		synchronized (queue) {
			if (queue.size() > 0) {
				minTime.clear();
				// Current Time
				// Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					Map<IQuery, Double> queryCalcedUrg = new HashMap<IQuery, Double>();
					long minTimePeriod = calcMinTimePeriod(is.getPlan());
					minTime.put(is, minTimePeriod);
					ScheduleMeta scheduleMeta = is.getPlan().getScheduleMeta();
					// Drain
					scheduleMeta.drainHistory(sla_history_size);
					// Calc prio for each query
					// Urgency for all Querys of this plan
					for (IQuery q : is.getPlan().getQueries()) {
						try {
							IServiceLevelAgreement sla = TenantManagement
									.getInstance().getSLAForUser(q.getUser());

							// Calculate the value, if this query would be
							// scheduled
							// need to test, if in time or out of time
							// if in time increase no of in time scheduling
							// Ist das wirklich schlau? Man will ja wissen
							// wie dringend es aktuell ist und nicht, wie
							// es wäre, wenn er gescheduled wird!
							// double rate = scheduleMeta.calcPotentialRate(
							// minTimePeriod);
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
						// e.g. with Max
					double prio = 0;
					switch (method) {
					case MAX:
						prio = calcPrioMax(queryCalcedUrg, is);
						break;
					case SUM:
						prio = calcPrioSum(queryCalcedUrg, is);
						break;
					case AVG:
						prio = calcPrioAvg(queryCalcedUrg, is);
						break;
					}
					long newPrio = (long) Math.round(prio * 1000);
					is.getPlan().setCurrentPriority(newPrio);
				}
				Collections.sort(queue, comperator);
				// Update Scheduling Infos
				// Add all elements with same prio to list
				Iterator<IScheduling> iter = queue.iterator();
				synchronized (lastRun) {
					lastRun.add(iter.next());
					long prio = queue.get(0).getPlan().getCurrentPriority();
					while (iter.hasNext()) {
						IScheduling s = iter.next();
						if (s.getPlan().getCurrentPriority() == prio) {
							lastRun.add(s);
						} else {
							break;
						}
					}
					return updateMetaAndReturnPlan(lastRun.remove(0));
				}
				// return updateMetaAndReturnPlan(queue.get(0));
			} else {
				return null;
			}
		}
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

	void updatePenalties() {
		if (toUpdateCounter == sla_update_Penalties_Frequency) {
			toUpdateCounter = 0;
			synchronized (queue) {
				for (IScheduling q : queue) {
					updatePenalites(q.getPlan());
				}
			}
		}
		toUpdateCounter++;
	}

	private void updatePenalites(IPartialPlan plan)
			throws NotInitializedException {
		ScheduleMeta meta = plan.getScheduleMeta();
		for (IQuery q : plan.getQueries()) {
			IServiceLevelAgreement sla = TenantManagement.getInstance()
					.getSLAForUser(q.getUser());
			double penalty = sla.getPercentilConstraint(meta.getRate())
					.getPenalty();
			q.addPenalty(penalty);
		}
	}

	private IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		ScheduleMeta meta = toSchedule.getPlan().getScheduleMeta();
		meta.scheduleDone(minTime.get(toSchedule));
		return toSchedule;
	}

	@Override
	public TimebasedSLAScheduler clone() {
		return new TimebasedSLAScheduler(this);
	}

}
