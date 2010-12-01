package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEventType;

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

			if (((PlanExecutionEvent) eventArgs).getEventType().equals(
					PlanExecutionEventType.EXECUTION_STARTED)) {
				System.out
						.println("Plan execution prepared...collecting memory usage data!");
				for (IPhysicalOperator op : eventArgs.getSender()
						.getExecutionPlan().getRoots()) {
					addMemListeners((ISink<?>) op);
				}
			}

			if (((PlanExecutionEvent) eventArgs).getEventType().equals(
					PlanExecutionEventType.EXECUTION_STOPPED)) {
				System.out
						.println("Plan execution finished...create benchmark results!");
				hash.clear();
			}
		}

	}

	private Collection<IPhysicalOperator> ops = new ArrayList<IPhysicalOperator>();
	private Map<Integer, IPhysicalOperator> hash = new HashMap<Integer, IPhysicalOperator>();
	private DescriptiveStatistics stats = new DescriptiveStatistics();
//	private long tmpAgg = 0;

	@SuppressWarnings("unchecked")
	private void addMemListeners(IPhysicalOperator op) {
		if ((op instanceof IBuffer || op instanceof JoinTIPO)
				&& hash.get(op.hashCode()) == null) {
			System.out.println("Monitoring temp memory usage for: "
					+ op.getName() + " with hash " + op.hashCode());
//			op.subscribe(this, POEventType.PushDone);
			op.subscribeToAll(this);
			ops.add(op);
			hash.put(op.hashCode(), op);
		}
		if (op.isSink()) {
			for (PhysicalSubscription<?> sub : ((ISink<?>) op)
					.getSubscribedToSource()) {
				addMemListeners((IPhysicalOperator) sub.getTarget());
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
	public void eventOccured(IEvent poEvent) {
		if (poEvent.getEventType()!= POEventType.ProcessInit && poEvent.getEventType() != POEventType.PushDone && poEvent.getEventType() != POEventType.ProcessPunctuationDone ){
			return;
		}
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
				aggSize += join.getTransferFunction().size();
				for (ISweepArea<?> sa : join.getAreas()) {
					aggSize += sa.size();
				}
			}
		}
		long timeDiff = Math.max(1L, curTime - lastTime);
//		this.tmpAgg = Math.max(tmpAgg, aggSize);
		if (timeDiff != 0) {
			lastTime = curTime;
			for (int i = 0; i < timeDiff; ++i) {
				this.stats.addValue(aggSize);
			}
//			this.tmpAgg = 0;
		}
	}

}
