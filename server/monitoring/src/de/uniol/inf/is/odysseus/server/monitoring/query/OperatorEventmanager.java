package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.server.monitoring.physicaloperator.Monitor;

public class OperatorEventmanager implements IPOEventListener {

	private Map<IPhysicalOperator, Long> operatorCPUUsage;
	private Map<IPhysicalOperator, Monitor> activeMonitor;
	// private Map<IPhysicalOperator, Long> startTimestamps;

	public OperatorEventmanager(IPhysicalQuery query) {
		operatorCPUUsage = new HashMap<IPhysicalOperator, Long>();
		activeMonitor = new HashMap<IPhysicalOperator, Monitor>();
		// startTimestamps = new HashMap<IPhysicalOperator, Long>();
		for (IPhysicalOperator o : query.getPhysicalChilds()) {
			o.subscribe(this, POEventType.ProcessInit);
			o.subscribe(this, POEventType.ProcessDone);
		}
	}

	@Override
		public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
			System.out.println("Event fired: " + event.toString());
			IPhysicalOperator o = (IPhysicalOperator) event.getSender();
			Thread t=null;
			Monitor mon =null;
			if(!activeMonitor.containsKey(o)){
				 mon = new Monitor();
				t = new Thread(mon);
			}else {
				mon=activeMonitor.get(o);
			}
			if (event.getEventType().equals(POEventType.ProcessInit)) {
				if(t!=null){
					activeMonitor.replace(o, mon);
					t.start();
				}
			}
			if (event.getEventType().equals(POEventType.ProcessDone)) {
				if (mon!=null){
					System.out.println("CPU: "+mon.getCpuUsage());
					mon.shutdown();
				}
			}
		}
}
