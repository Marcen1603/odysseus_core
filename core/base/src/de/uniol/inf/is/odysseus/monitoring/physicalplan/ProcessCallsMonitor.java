package de.uniol.inf.is.odysseus.monitoring.physicalplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;

public class ProcessCallsMonitor extends AbstractMonitoringData<Long>
implements IPOEventListener {
	final Map<IPhysicalOperator, Long> processCallsPerOperator;
	final List<IPhysicalOperator> monitoredOps;
	long overallProcessCallCount = 0;

	public ProcessCallsMonitor(IQuery target, boolean onlyRoots) {
		super(target);
		processCallsPerOperator = new HashMap<IPhysicalOperator, Long>();
		monitoredOps = target.getRoots();
		if (!onlyRoots){
			monitoredOps.addAll(target.getPhysicalChilds());
		}

		for (IPhysicalOperator p : monitoredOps) {
			processCallsPerOperator.put(p, 0l);
			p.subscribe(this, POEventType.ProcessDone);
		}
	}

	public ProcessCallsMonitor(ProcessCallsMonitor processCallsMonitor) {
		super(processCallsMonitor.getTarget());
		overallProcessCallCount = processCallsMonitor.overallProcessCallCount;
		monitoredOps = new ArrayList<IPhysicalOperator>(processCallsMonitor.monitoredOps);
		processCallsPerOperator = new HashMap<IPhysicalOperator, Long>(processCallsMonitor.processCallsPerOperator);
	}

	@Override
	public void eventOccured(IEvent<?,?> event) {
		POEvent poEvent = (POEvent) event;
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

	@Override
	public Long getValue()  {
		return overallProcessCallCount;
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
}
