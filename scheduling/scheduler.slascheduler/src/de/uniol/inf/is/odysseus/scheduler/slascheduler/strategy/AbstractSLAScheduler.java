package de.uniol.inf.is.odysseus.scheduler.slascheduler.strategy;

import java.util.ArrayList;
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
import de.uniol.inf.is.odysseus.scheduler.singlethreadscheduler.IPartialPlanScheduling;
import de.uniol.inf.is.odysseus.scheduler.strategy.IScheduling;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.NotInitializedException;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;

public abstract class AbstractSLAScheduler implements IPartialPlanScheduling{

	static private Logger logger = LoggerFactory
	.getLogger(AbstractSLAScheduler.class);
	
	protected final List<IScheduling> queue;

	final private List<IScheduling> lastRun = new LinkedList<IScheduling>();
	
	public enum PrioCalcMethod {
		MAX, SUM, AVG
	}

	final private long updatePenaltyTime = OdysseusDefaults.getLong(
			"sla_updatePenaltyTime", 60000);
	private long lastPenalties = -1;
	
	protected PrioCalcMethod method;

	public AbstractSLAScheduler(PrioCalcMethod method) {
		queue = new ArrayList<IScheduling>();
		this.method = method;
	}

	public AbstractSLAScheduler(AbstractSLAScheduler simpleSLAScheduler) {
		queue = new ArrayList<IScheduling>(simpleSLAScheduler.queue);
		this.method = simpleSLAScheduler.method;
	}

	protected double calcPrio(Map<IQuery, Double> queryCalcedUrg, IScheduling is) {
		double prio = 0;
		// Calculation is only needed if more that one query is involved
		if (queryCalcedUrg.size() == 1) {
			prio = queryCalcedUrg.values().iterator().next();
		} else {
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
		}
		return prio;
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
	public void clear() {
		queue.clear();
	}
	
	@Override
	public IScheduling nextPlan() {
		if ((lastPenalties > 0 && System.currentTimeMillis() - lastPenalties >= updatePenaltyTime)
				|| lastPenalties <= 0) {
			updatePenalites();
		}
		synchronized (lastRun) {
			if (lastRun.size() > 0) {
				return updateMetaAndReturnPlan(lastRun.remove(0));
			}
		}
		return null;
	}
	
	protected IScheduling initLastRun(long maxPrio) {
		// Add all elements with max prio to list
		Iterator<IScheduling> iter = queue.iterator();
		synchronized (lastRun) {
			while (iter.hasNext()) {
				IScheduling s = iter.next();
				if (s.getPlan().getCurrentPriority() == maxPrio) {
					lastRun.add(s);
				}
			}
			return updateMetaAndReturnPlan(lastRun.remove(0));
		}
	}

	
	abstract protected IScheduling updateMetaAndReturnPlan(IScheduling plan);
	
	private void updatePenalites() {
		synchronized (queue) {
			for (IScheduling s : queue) {
				updatePenalites(s.getPlan());
			}
			lastPenalties = System.currentTimeMillis();
		}

	}

	private void updatePenalites(IPartialPlan plan)
			throws NotInitializedException {
		ScheduleMeta meta = plan.getScheduleMeta();
		for (IQuery q : plan.getQueries()) {
			IServiceLevelAgreement sla = TenantManagement.getInstance()
					.getSLAForUser(q.getUser());
			if (meta.getLastDiff() > 0) {
				double penalty = sla.getPercentilConstraint(meta.getRate())
						.getPenalty();
				q.addPenalty(penalty);
			}
		}
	}

	
	
	@Override
	public void addPlan(IScheduling scheduling) {
		synchronized (queue) {
			queue.add(scheduling);
			scheduling.getPlan().setScheduleMeta(
					new ScheduleMeta(System.currentTimeMillis()));
			logger.debug("Adding Plan done. Current Plan count " + queue.size());
		}
	}

	@Override
	public void removePlan(IScheduling plan) {
		queue.remove(plan);
		lastRun.remove(plan);
	}

	@Override
	public int planCount() {
		synchronized (queue) {
			return queue.size();
		}
	}

	
	@Override
	abstract public AbstractSLAScheduler clone();

}
