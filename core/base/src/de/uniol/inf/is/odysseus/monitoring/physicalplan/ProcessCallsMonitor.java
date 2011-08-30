/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class ProcessCallsMonitor extends AbstractPlanMonitor<Long> implements
		IPOEventListener {
	final Map<IPhysicalOperator, Long> processCallsPerOperator;
	long overallProcessCallCount = 0;
	private boolean relativeCallCount;

	public ProcessCallsMonitor(IQuery target, boolean onlyRoots,
			boolean onlyBuffer, String type, boolean relativeCallCount) {
		super(target, onlyRoots, onlyBuffer, type);
		processCallsPerOperator = new HashMap<IPhysicalOperator, Long>();
		for (IPhysicalOperator p : monitoredOps) {
			processCallsPerOperator.put(p, 0l);
			p.subscribe(this, POEventType.ProcessDone);
		}
		this.relativeCallCount = relativeCallCount;
	}

	public ProcessCallsMonitor(ProcessCallsMonitor processCallsMonitor) {
		super(processCallsMonitor);
		overallProcessCallCount = processCallsMonitor.overallProcessCallCount;
		processCallsPerOperator = new HashMap<IPhysicalOperator, Long>(
				processCallsMonitor.processCallsPerOperator);
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long eventNanoTime) {
		POEvent poEvent = (POEvent) event;
		IPhysicalOperator source = poEvent.getSource();
		synchronized (processCallsPerOperator) {
			long c = processCallsPerOperator.get(source);
			processCallsPerOperator.put(source, c + 1);
			overallProcessCallCount++;
		}
	}

	public long getProcessCallsForOperator(IPhysicalOperator op) {
		return processCallsPerOperator.get(op);
	}

	public long getOverallProcessCallCount() {
		return overallProcessCallCount;
	}

	@Override
	public String toString() {
		StringBuffer b = new StringBuffer(this.getType());
		b.append(" sum=").append(overallProcessCallCount);
		if (processCallsPerOperator.size() > 1) {
			 b.append(" /#ops="
					+ overallProcessCallCount / processCallsPerOperator.size()
					+ "\n");
		}
		// for (Entry<IPhysicalOperator, Long> p :
		// processCallsPerOperator.entrySet()) {
		// b.append("--> " + p.getKey() + " = " + p.getValue() + " "
		// + (overallProcessCallCount > 0 ? (p.getValue() /
		// overallProcessCallCount) : 0)
		// + "\n");
		// }
		return b.toString();
	}

	@Override
	public Long getValue() {
		return relativeCallCount ? (overallProcessCallCount / processCallsPerOperator
				.size()) : overallProcessCallCount;
	}

	public double getDoubleValue() {
		return relativeCallCount ? (overallProcessCallCount * 1.0 / processCallsPerOperator
				.size()) : overallProcessCallCount * 1.0;
	}

	@Override
	public void reset() {
		overallProcessCallCount = 0;
		for (IPhysicalOperator p : monitoredOps) {
			processCallsPerOperator.put(p, 0l);
		}

	}

	@Override
	public AbstractMonitoringData<Long> clone() {
		return new ProcessCallsMonitor(this);
	}

	@Override
	public Long getValue(IPhysicalOperator operator) {
		return processCallsPerOperator.get(operator);
	}
}
