/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.benchmarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.monitoring.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.IBuffer;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.IPlanExecutionListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planexecution.event.PlanExecutionEventType;

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
					addMemListeners(op);
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
	public void eventOccured(IEvent<?,?> poEvent, long eventNanoTime) {
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
