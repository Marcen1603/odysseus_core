package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.monitoring.physicalplan.IPlanMonitor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.scheduler.ISchedulingEventListener;
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.CurrentPlanPriorityComperator;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public class SimpleSLAScheduler implements IPartialPlanScheduling,
		ISchedulingEventListener {

	protected final List<IScheduling> queue;

	public enum PrioCalcMethod {
		MAX, SUM, AVG
	}

	protected PrioCalcMethod method;

	public SimpleSLAScheduler(PrioCalcMethod method) {
		queue = new ArrayList<IScheduling>();
		this.method = method;
	}

	public SimpleSLAScheduler(SimpleSLAScheduler simpleSLAScheduler) {
		queue = new ArrayList<IScheduling>(simpleSLAScheduler.queue);
		this.method = simpleSLAScheduler.method;
	}

	@Override
	public IScheduling nextPlan() {
		synchronized (queue) {
			if (queue.size() > 0) {
				Map<IQuery, Double> calcedUrg = new HashMap<IQuery, Double>();
				// 1. Step: Get all queries and Calc Sum of all MonitoringValues 
				Set<IQuery> allQueries = new HashSet<IQuery>();
				double sum = 0;
				for (IScheduling is : queue){
					for(IQuery q:is.getPlan().getQueries()) {
						allQueries.add(q);
						@SuppressWarnings("unchecked")
						IPlanMonitor<Double> monitor = q.getPlanMonitor("SLA Monitor");
						sum += monitor.getDoubleValue(); 
					}
				}
				// 2. Step: Calc Prio for each PartialPlan
				for (IScheduling is : queue) {
					// Calc prio for each query
					for (IQuery q: is.getPlan().getQueries()){
						// A query can be part of more than one scheduling
						// and only needs to be calculated ones
						if (calcedUrg.containsKey(q))
							continue;

						@SuppressWarnings("unchecked")
						IPlanMonitor<Double> monitor = q
								.getPlanMonitor("SLA Monitor");
						IServiceLevelAgreement sla = TenantManagement
								.getInstance().getSLAForUser(q.getUser());
						// Im Prinzip müsste man jetzt testen, ob es eine SLA
						// für diesen
						// User gibt ... auf der anderen Seiten macht es keinen
						// SInn, diesen
						// Scheduler zu waehlen, wenn man kein SLA hat ...
						double urge = 0.0;
						try {
							urge = sla.getMaxOcMg(monitor.getDoubleValue()/sum);
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
				return queue.get(0);
			} else {
				return null;
			}
		}
	}

	// Calc Prio with maximum
	protected double calcPrioMax(Map<IQuery, Double> calcedUrg, IScheduling is) {
		double prio = 0;
		for (IQuery q : is.getPlan().getQueries()) {
			prio = Math.max(prio, calcedUrg.get(q));
		}
		return prio;
	}

	// Calc Prio as sum
	protected double calcPrioSum(Map<IQuery, Double> calcedUrg, IScheduling is) {
		double prio = 0;
		for (IQuery q : is.getPlan().getQueries()) {
			prio = +calcedUrg.get(q);
		}
		return prio;
	}

	// Calc Prio as avg
	protected double calcPrioAvg(Map<IQuery, Double> calcedUrg, IScheduling is) {
		double prio = 0;
		for (IQuery q : is.getPlan().getQueries()) {
			prio = +calcedUrg.get(q);
		}
		return prio / calcedUrg.size();
	}

	@Override
	public SimpleSLAScheduler clone() {
		return new SimpleSLAScheduler(this);
	}

	@Override
	public void clear() {
		for (IScheduling e : queue){
			e.removeSchedulingEventListener(this);
		}
		queue.clear();
	}

	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			// Wird nicht benötigt, da eh in jedem Durchlauf sortiert werden
			// muss
			// Collections.sort(queue, new CurrentPlanPriorityComperator());
			scheduling.addSchedulingEventListener(this);
		}
	}

	@Override
	public int planCount() {
		synchronized (queue) {
			return queue.size();
		}
	}

	@Override
	public void removePlan(IScheduling plan) {
		synchronized (queue) {
			queue.remove(plan);
			plan.removeSchedulingEventListener(this);
		}
	}

	@Override
	public void nothingToSchedule(IScheduling sched) {
		synchronized (queue) {
			// keine gute Idee -->
			// queue.remove(sched);
		}
	}

	@Override
	public void scheddulingPossible(IScheduling sched) {
		synchronized (queue) {
			queue.add(sched);
			Collections.sort(queue, new CurrentPlanPriorityComperator());
		}
	}

}
