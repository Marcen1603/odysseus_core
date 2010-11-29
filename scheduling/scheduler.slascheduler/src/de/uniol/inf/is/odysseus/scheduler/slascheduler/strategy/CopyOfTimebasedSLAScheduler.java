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
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.ITimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class CopyOfTimebasedSLAScheduler extends SimpleSLAScheduler {

	FileWriter file;

	static private Logger logger = LoggerFactory
			.getLogger(CopyOfTimebasedSLAScheduler.class);

	final private Map<IScheduling, ScheduleMeta0> schedulingHistory;

	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();
	Map<IScheduling, Long> minTime = new HashMap<IScheduling, Long>();

	public CopyOfTimebasedSLAScheduler(CopyOfTimebasedSLAScheduler timebasedSLAScheduler) {
		super(timebasedSLAScheduler);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta0>();
		for (Entry<IScheduling, ScheduleMeta0> h : timebasedSLAScheduler.schedulingHistory
				.entrySet()) {
			schedulingHistory.put(h.getKey(), new ScheduleMeta0(h.getValue()));
		}
		try {
			file = new FileWriter(OdysseusDefaults.odysseusHome
					+ "TBSLAlog"+System.currentTimeMillis()+".csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public CopyOfTimebasedSLAScheduler(PrioCalcMethod method) {
		super(method);
		schedulingHistory = new HashMap<IScheduling, ScheduleMeta0>();
		try {
			file = new FileWriter(OdysseusDefaults.odysseusHome
					+ "TBSLAlog"+System.currentTimeMillis()+".csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void addPlan(IScheduling scheduling) {
		logger.debug("Adding Plan");
		synchronized (queue) {
			queue.add(scheduling);
			scheduling.addSchedulingEventListener(this);
			schedulingHistory.put(scheduling, new ScheduleMeta0(-1, 0, 0));
		}
		logger.debug("Adding Plan done");
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
				// Calced Urgency Cache for all queries
				Map<IQuery, Double> queryCalcedUrg = new HashMap<IQuery, Double>();
				minTime.clear();
				// Current Time
				long now = System.currentTimeMillis();
				// Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					long minTimePeriod = Long.MAX_VALUE;
					ScheduleMeta0 ScheduleMeta0 = schedulingHistory.get(is);
					// Calc prio for each query
					IServiceLevelAgreement sla = null;
					// Urgency for all Querys of this plan
					for (IQuery q : is.getPlan().getQueries()) {
						try {
							// A query can be part of more than one scheduling
							// and only needs to be calculated ones
							if (queryCalcedUrg.containsKey(q)) {
								continue;
							}

							sla = TenantManagement.getInstance().getSLAForUser(
									q.getUser());
							if (((ITimeBasedServiceLevelAgreement) sla)
									.getTimeperiod() < minTimePeriod) {
								minTimePeriod = ((ITimeBasedServiceLevelAgreement) sla)
										.getTimeperiod();
							}
							// Calculate the value, if this query is scheduled
							// need to test, if in time or out of time
							// if in time increase no of in time scheduling
							double potentialInTimeTrans = ((now - ScheduleMeta0.lastSchedule) < minTimePeriod ? (ScheduleMeta0.inTimeCount + 1)
									: ScheduleMeta0.inTimeCount) * 1.0;
							double value = ScheduleMeta0.allSchedulings > 0 ? potentialInTimeTrans
									* 1.0
									/ (ScheduleMeta0.allSchedulings + 1 * 1.0) // increase
																				// potential
																				// overall
																				// count!
									: 0;

							double urge = 0.0;
							urge = sla.getMaxOcMg(value);
							queryCalcedUrg.put(q, urge);
						} catch (Exception e) {
							e.printStackTrace();
							throw new RuntimeException(e);
						}

					}// Query
					minTime.put(is, minTimePeriod);
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
			} else {
				return null;
			}
		}
	}

	private IScheduling updateMetaAndReturnPlan(IScheduling toSchedule) {
		// In Time if no query is overtimed
		ScheduleMeta0 meta = schedulingHistory.get(toSchedule);
		long last = meta.lastSchedule;
		long now = System.currentTimeMillis();
		print(now, toSchedule);
		meta.lastSchedule = now;
		if (minTime.get(toSchedule) > (meta.lastSchedule - last)) {
			meta.inTimeCount++;
		}
		meta.allSchedulings++;
		return toSchedule;
	}

	private void print(long now, IScheduling s) {
		StringBuffer toPrint = new StringBuffer();
		// TODO: Test Ich weiﬂ, dass es aktuell nur eine Query pro Plan gibt
		toPrint.append(now).append(";");
		toPrint.append(s.getPlan().getId()).append(";")
				.append(s.getPlan().getQueries().get(0).getID()).append(";")
				.append(s.getPlan().getCurrentPriority());
		ScheduleMeta0 h = schedulingHistory.get(s);
		toPrint.append(";").append(now - h.lastSchedule);
		toPrint.append(";").append(h.inTimeCount);
		toPrint.append(";").append(h.allSchedulings);
		toPrint.append(";").append(h.inTimeCount * 1.0 / h.allSchedulings)
				.append(";");
		// logger.debug(toPrint.toString());
		// System.out.println(toPrint);
		try {
			file.write(toPrint.toString() + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public CopyOfTimebasedSLAScheduler clone() {
		return new CopyOfTimebasedSLAScheduler(this);
	}

}

class ScheduleMeta0 {
	long lastSchedule = -1;
	long inTimeCount = 0;
	long allSchedulings;

	public ScheduleMeta0(long lastSchedule, long inTimeCount, long outOfTimeCount) {
		super();
		this.lastSchedule = lastSchedule;
		this.inTimeCount = inTimeCount;
		this.allSchedulings = outOfTimeCount;
	}

	public ScheduleMeta0(ScheduleMeta0 value) {
		this.lastSchedule = value.lastSchedule;
		this.inTimeCount = value.inTimeCount;
		this.allSchedulings = value.allSchedulings;
	}

	@Override
	public String toString() {
		return "last " + lastSchedule + " " + inTimeCount;
	}

}
