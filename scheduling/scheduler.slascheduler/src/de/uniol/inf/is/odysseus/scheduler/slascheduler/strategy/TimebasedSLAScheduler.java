package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.OdysseusDefaults;
import de.uniol.inf.is.odysseus.collection.Pair;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class TimebasedSLAScheduler extends SimpleSLAScheduler {

	// TODO: REMOVE AGAIN!!
	FileWriter file;

	static private Logger logger = LoggerFactory
			.getLogger(TimebasedSLAScheduler.class);

	final private Map<IScheduling, ScheduleMeta> schedulingHistory;

	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();
	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	long historySize = 1000;
	long start = -1;

	public TimebasedSLAScheduler(TimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta>();
		for (Entry<IScheduling, ScheduleMeta> h : timebasedSLAScheduler.schedulingHistory
				.entrySet()) {
			schedulingHistory.put(h.getKey(), new ScheduleMeta(h.getValue()));
		}
		try {
			file = new FileWriter(OdysseusDefaults.odysseusHome + "TBSLAlog"
					+ System.currentTimeMillis() + ".csv");
			file.write("Timestamp;PartialPlan;Query;Priority;DiffToLastCall;InTimeCalls;AllCalls;PossSchedulings;Factor\n");
			start = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public TimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta>();
		try {
			file = new FileWriter(OdysseusDefaults.odysseusHome + "TBSLAlog"
					+ System.currentTimeMillis() + ".csv");
			file.write("Timestamp;PartialPlan;Query;Priority;DiffToLastCall;InTimeCalls;AllCalls;PossSchedulings;Factor\n");
			start = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void addPlan(IScheduling scheduling) {
		logger.debug("Adding Plan");
		synchronized (queue) {
			queue.add(scheduling);
			scheduling.addSchedulingEventListener(this);
			schedulingHistory.put(scheduling, new ScheduleMeta(start,
					scheduling.getPlan()));
			logger.debug("Adding Plan done. Current Plan count " + queue.size());
		}
	}

	long getNow() {
		return System.currentTimeMillis() - start;
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (lastRun) {
			if (lastRun.size() > 0) {
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
		synchronized (queue) {
			if (queue.size() > 0) {
				minTime.clear();
				// Current Time
				long now = getNow();
				// Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					Map<IQuery, Double> queryCalcedUrg = new HashMap<IQuery, Double>();
					long minTimePeriod = calcMinTimePeriod(is.getPlan());
					minTime.put(is, minTimePeriod);
					ScheduleMeta scheduleMeta = schedulingHistory.get(is);
					// Drain
					scheduleMeta.drainHistory(now - historySize);
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
									now,
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
					is.getPlan().setSLARate(scheduleMeta.getRate());
					StringBuffer toPrint = new StringBuffer();
					scheduleMeta.csvPrint2(toPrint, now);
					is.getPlan().setSLAInfo(toPrint.toString());
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

	private IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		// In Time if no query is overtimed
		ScheduleMeta meta = schedulingHistory.get(toSchedule);
		long now = getNow();
		print(now, toSchedule);
		meta.transactionDone(now, minTime.get(toSchedule));
		return toSchedule;
	}

	private void print(long now, IScheduling s) {
		StringBuffer toPrint = new StringBuffer();
		// TODO: Test Ich weiß, dass es aktuell nur eine Query pro Plan gibt
		toPrint.append(now).append(";");
		toPrint.append(s.getPlan().getId()).append(";")
				.append(s.getPlan().getQueries().get(0).getID()).append(";")
				.append(s.getPlan().getCurrentPriority()).append(";");
		ScheduleMeta h = schedulingHistory.get(s);
		h.csvPrint(toPrint, now);
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

class ScheduleMeta {
	private long lastSchedule = -1;
	private long inTimeCount = 0;
	private long allSchedulings;
	private long possibleSchedulings;
	final private IPartialPlan partialPlan;

	final private LinkedList<Pair<Long, Boolean>> history;

	public ScheduleMeta(long lastSchedule, IPartialPlan partialPlan) {
		super();
		this.lastSchedule = lastSchedule;
		this.inTimeCount = 0;
		this.allSchedulings = 0;
		this.possibleSchedulings = 0;
		this.partialPlan = partialPlan;
		history = new LinkedList<Pair<Long, Boolean>>();
	}

	public ScheduleMeta(ScheduleMeta value) {
		this.lastSchedule = value.lastSchedule;
		this.inTimeCount = value.inTimeCount;
		this.allSchedulings = value.allSchedulings;
		this.possibleSchedulings = value.possibleSchedulings;
		this.partialPlan = value.partialPlan;
		history = new LinkedList<Pair<Long, Boolean>>(value.history);
	}

	public void csvPrint(StringBuffer toPrint, long now) {
		toPrint.append(now - lastSchedule);
		toPrint.append(";").append(inTimeCount);
		toPrint.append(";").append(allSchedulings);
		toPrint.append(";").append(possibleSchedulings);
		if (allSchedulings > 0) {
			toPrint.append(";").append(
					Math.round((inTimeCount * 1.0 / allSchedulings) * 100));
		} else {
			toPrint.append(";0");
		}
	}

	public void csvPrint2(StringBuffer toPrint, long now) {
		toPrint.append("Last=").append(now - lastSchedule);
		toPrint.append(" InTime=").append(inTimeCount);
		toPrint.append(" All=").append(allSchedulings);
		toPrint.append(" Rate=");
		if (allSchedulings > 0) {
			toPrint.append(Math
					.round((inTimeCount * 1.0 / allSchedulings) * 100));
		} else {
			toPrint.append(";0");
		}
		toPrint.append("H size=").append(history.size());
	}

	public void drainHistory(long before) {
		synchronized (history) {
			Iterator<Pair<Long, Boolean>> iter = history.iterator();
			while (iter.hasNext()) {
				Pair<Long, Boolean> entry = iter.next();
				if (entry.getE1().longValue() < before) {
					iter.remove();
					if (entry.getE2()) {
						inTimeCount--;
					}
					allSchedulings--;
				} else {
					break; // Timestamps are sorted
				}
			}
		}
	}

	public void transactionDone(long timestamp, long minTime) {
		synchronized (history) {
			boolean inTime = minTime > timestamp - lastSchedule;
			if (inTime) {
				inTimeCount++;
			}
			this.lastSchedule = timestamp;
			allSchedulings++;
			history.add(new Pair<Long, Boolean>(timestamp, inTime));
		}
	}

	public double calcPotentialRate(long now, long minTimePeriod,
			long possibleSchedulings) {
		this.possibleSchedulings = possibleSchedulings;
		double potentialInTimeTrans = ((now - lastSchedule) < minTimePeriod ? (inTimeCount + 1)
				: inTimeCount) * 1.0;
		return allSchedulings > 0 ? potentialInTimeTrans * 1.0
				/ (allSchedulings + 1 * 1.0) // increase
												// potential
												// overall
												// count!
		: 0;
	}

	public double getRate() {
		return allSchedulings > 0 ? inTimeCount * 1.0 / (allSchedulings * 1.0)
				: 0;

	}

	@Override
	public String toString() {
		return "last " + lastSchedule + " " + inTimeCount;
	}

}
