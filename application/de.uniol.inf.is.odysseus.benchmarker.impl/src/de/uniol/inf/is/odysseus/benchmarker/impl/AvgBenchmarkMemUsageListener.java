package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;

/**
 * Called after initializing the plan and stopping the execution.
 * AvgBenchmarkMemUsageListener produces memory usage statistics based uppon
 * collected data by AvgTempMemUsageListener during the plan execution.
 * 
 * @author Jan Steinke
 * 
 */
public class AvgBenchmarkMemUsageListener implements IPlanExecutionListener,
		IPOEventListener {

	private static final int nsDivisor = 100000;
	long lastTime = 0;

	@Override
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs) {
		if (eventArgs instanceof PlanExecutionEvent) {

			if (((PlanExecutionEvent) eventArgs).getID().equals(
					PlanExecutionEvent.EXECUTION_STARTED)) {
				System.out
						.println("Plan execution prepared...collecting memory usage data!");
				for (IPartialPlan each : eventArgs.getSender()
						.getExecutionPlan().getPartialPlans()) {
					for (IPhysicalOperator op : each.getRoots()) {
						addMemListeners((ISink<?>) op);
					}
				}
			}

			if (((PlanExecutionEvent) eventArgs).getID().equals(
					PlanExecutionEvent.EXECUTION_STOPPED)) {
				System.out
						.println("Plan execution finished...create benchmark results!");
				hash.clear();
			}
		}

	}

	private Collection<IPhysicalOperator> ops = new ArrayList<IPhysicalOperator>();
	private Map<Integer, IPhysicalOperator> hash = new HashMap<Integer, IPhysicalOperator>();
	private DescriptiveStatistics stats = new DescriptiveStatistics();
	private long tmpAgg = 0;

	@SuppressWarnings("unchecked")
	private void addMemListeners(IPhysicalOperator op) {
		if ((op instanceof IBuffer || op instanceof JoinTIPO)
				&& hash.get(op.hashCode()) == null) {
			System.out.println("Monitoring temp memory usage for: "
					+ op.getName() + " with hash " + op.hashCode());
			op.subscribe(this, POEventType.PushDone);
			ops.add(op);
			hash.put(op.hashCode(), op);
		}
		if (op.isSink()) {
			for (PhysicalSubscription<?> sub : ((ISink<?>) op)
					.getSubscribedToSource()) {
				addMemListeners((ISource<?>) sub.getTarget());
			}
		}

	}

	public List<DescriptiveStatistics> calcMemUsageBenchmarkResult(
			List<AvgTempMemUsageListener> list) {
		List<DescriptiveStatistics> stats = new ArrayList<DescriptiveStatistics>();
		for (AvgTempMemUsageListener each : list) {
			stats.add(each.getStats());
		}
		return stats;
	}

	public DescriptiveStatistics getMemUsageStatistics() {
		return this.stats;
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		long curTime = System.nanoTime() / nsDivisor;
		if (lastTime == 0) {
			lastTime = curTime;
			return;
		}
		long aggSize = 0;
		for (IPhysicalOperator op : ops) {
			if (op instanceof IBuffer<?>) {
				aggSize += ((IBuffer<?>) op).size();
			} else {
				JoinTIPO<?, ?> join = (JoinTIPO<?, ?>) op;
				for (ISweepArea<?> sa : join.getAreas()) {
					aggSize += sa.size();
				}
			}
		}
		long timeDiff = curTime - lastTime;
		this.tmpAgg = Math.max(tmpAgg, aggSize);
		if (timeDiff != 0) {
			lastTime = curTime;
			for (int i = 0; i < timeDiff; ++i) {
				this.stats.addValue(tmpAgg);
			}
			this.tmpAgg = 0;
		}
	}

}
