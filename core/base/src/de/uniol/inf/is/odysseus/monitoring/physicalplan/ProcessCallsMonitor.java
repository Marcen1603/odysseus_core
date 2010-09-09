package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

public class ProcessCallsMonitor implements IPOEventListener {
	final Map<IPhysicalOperator, Long> processCallsPerOperator;
	long overallProcessCallCount = 0;

	public ProcessCallsMonitor(List<? extends IPhysicalOperator> toMonitor) {
		processCallsPerOperator = new HashMap<IPhysicalOperator, Long>();
		for (IPhysicalOperator p : toMonitor) {
			processCallsPerOperator.put(p, 0l);
			p.subscribe(this, POEventType.ProcessDone);
		}
	}

	@Override
	public void poEventOccured(POEvent poEvent) {
		IPhysicalOperator source = poEvent.getSource();
		synchronized (processCallsPerOperator) {
			long c = processCallsPerOperator.get(source);
			processCallsPerOperator.put(source, c + 1);
			overallProcessCallCount++;
		}
	}

	public Long getProcessCallsForOperator(IPhysicalOperator op) {
		return processCallsPerOperator.get(op);
	}
	
	public long getOverallProcessCallCount() {
		return overallProcessCallCount;
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer(this.getClass().getSimpleName());
		b.append("OverallCount " + overallProcessCallCount + "\n");
		for (Entry<IPhysicalOperator, Long> p : processCallsPerOperator.entrySet()) {
			b.append("--> " + p.getKey() + " = " + p.getValue() + " "
					+ (overallProcessCallCount > 0 ? (p.getValue() / overallProcessCallCount) : 0)
					+ "\n");
		}
		return b.toString();
	}
}
