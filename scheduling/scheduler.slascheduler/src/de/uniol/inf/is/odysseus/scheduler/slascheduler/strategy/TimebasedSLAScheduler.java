package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.io.FileWriter;
import java.io.IOException;
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

	FileWriter file;

	static private Logger logger = LoggerFactory
			.getLogger(TimebasedSLAScheduler.class);

	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();
	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	final private boolean outputDebug = Boolean.parseBoolean(OdysseusDefaults.get("sla_debug_TimebasedSLAScheduler"));
	final private long sla_history_size = Long.parseLong(OdysseusDefaults.get("sla_history_size"));
	final private long sla_update_Penalties_Frequency = Long.parseLong(OdysseusDefaults.get("sla_update_Penalties_Frequency"));
	
	private long toUpdateCounter = 0;


	public TimebasedSLAScheduler(TimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
		try {
			
			if (outputDebug) {
				file = new FileWriter(OdysseusDefaults.odysseusHome
						+ "TBSLAlog" + System.currentTimeMillis() + ".csv");
				file.write("Timestamp;PartialPlan;Query;Priority;DiffToLastCall;InTimeCalls;AllCalls;Factor\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
		try {
			if (outputDebug) {
				file = new FileWriter(OdysseusDefaults.odysseusHome
						+ "TBSLAlog" + System.currentTimeMillis() + ".csv");
				file.write("Timestamp;PartialPlan;Query;Priority;DiffToLastCall;InTimeCalls;AllCalls;Factor\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
					scheduleMeta
							.drainHistory(sla_history_size);
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
							double rate = scheduleMeta.calcPotentialRate(
									minTimePeriod,
									Long.valueOf((Long) q.getPlanMonitor(
											"Buffer Monitor").getValue()));

							double urge = 0.0;
							urge = sla.getMaxOcMg(rate);
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
				Collections.sort(queue, new CurrentPlanPriorityComperator());
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
		if (toUpdateCounter == sla_update_Penalties_Frequency ) {
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
		if (outputDebug) {
			print(toSchedule);
		}
		meta.scheduleDone(minTime.get(toSchedule));
		return toSchedule;
	}

	private void print(IScheduling s) {
		StringBuffer toPrint = new StringBuffer();
		// TODO: Test Ich weiß, dass es aktuell nur eine Query pro Plan gibt
		toPrint.append(System.currentTimeMillis()).append(";");
		toPrint.append(s.getPlan().getId()).append(";")
				.append(s.getPlan().getQueries().get(0).getID()).append(";")
				.append(s.getPlan().getCurrentPriority()).append(";");
		ScheduleMeta h = s.getPlan().getScheduleMeta();
		h.csvPrint(toPrint);
		// logger.debug(toPrint.toString());
		// System.out.println(toPrint);
		try {
			file.write(toPrint.toString() + "\n");
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public TimebasedSLAScheduler clone() {
		return new TimebasedSLAScheduler(this);
	}

}
