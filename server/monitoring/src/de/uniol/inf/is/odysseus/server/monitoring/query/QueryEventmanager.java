package de.uniol.inf.is.odysseus.server.monitoring.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryEventmanager implements IPOEventListener {

	private long currentLatency;
	private IPhysicalQuery query;
	private List<IPhysicalOperator> roots;
	IPhysicalQuery subscribedTarget;
	private NavigableMap<String, OperatorLatency> latencys ;
	private NavigableMap<Integer, NavigableMap<String, OperatorLatency>> temporaryLatencys;
	
	public QueryEventmanager(IPhysicalQuery q) {
		temporaryLatencys = new TreeMap<Integer, NavigableMap<String, OperatorLatency>>();
		latencys =  new TreeMap<String, OperatorLatency>();
		query = q;
		for (IPhysicalOperator o : query.getPhysicalChilds()) {
			o.subscribe(this, POEventType.ProcessInit);
			o.subscribe(this, POEventType.PushInit);
			o.subscribe(this, POEventType.ProcessDone);
		}
		roots = query.getRoots();
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		System.out.println("Event fired: " + event.toString() + "Zeit: " + nanoTimestamp);
		IPhysicalOperator o = (IPhysicalOperator) event.getSender();
		OperatorLatency l = null;
		if (event.getEventType().equals(POEventType.ProcessInit)) {
			createNewTempLatency(o.toString(), nanoTimestamp);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			calcTemporaryOperatorLatency(o.toString(), nanoTimestamp);
		}
		if (event.getEventType().equals(POEventType.ProcessDone)) {
			commitTempElement(o.toString());
		}
	}

	private void calcTemporaryOperatorLatency(String operatorName, long nanoTimestamp) {
		for (int i = 0; i < temporaryLatencys.size(); i++) {
			if (temporaryLatencys.get(i).containsKey(operatorName)) {
				OperatorLatency l = temporaryLatencys.get(i).get(operatorName);
				// Berechne Latenz eines einzelnen Operators
				l.calcLatency(nanoTimestamp);
				temporaryLatencys.get(i).put(operatorName, l);
				//End loop
				i=temporaryLatencys.size();
			}
		}
	}

	private void createNewTempLatency(String operatorName, long nanoTimestamp) {
		if (temporaryLatencys.size()!=0){
			for (int i = 0; i < temporaryLatencys.size(); i++) {
				if (temporaryLatencys.get(i).containsKey(operatorName)) {
					continue;
				} else {
					OperatorLatency l = new OperatorLatency();
					l.setStartTime(nanoTimestamp);
					NavigableMap<String, OperatorLatency> temp = new TreeMap<String, OperatorLatency>();
					temp.put(operatorName, l);
					temporaryLatencys.put(i, temp);
				}
			}
		}else{
			OperatorLatency l = new OperatorLatency();
			l.setStartTime(nanoTimestamp);
			NavigableMap<String, OperatorLatency> temp = new TreeMap<String, OperatorLatency>();
			temp.put(operatorName, l);
			temporaryLatencys.put(1, temp);
		}
	}

	private void commitTempElement(String o) {
		// Durchlaufe alle Operatoren, für die ein ProcessInit Event erzeugt
		// wurde
		for (int i = 0; i < temporaryLatencys.size(); i++) {
			if (temporaryLatencys.get(i).containsKey(o)) {
				if (i == 0) {
					// copy HashMap to Latencys
					latencys.putAll(temporaryLatencys.get(i));

					// delete Item from temporaryLatencys
					temporaryLatencys.remove(i);
					// reorganize HashMap

					// Recalculate Latency
					calcQueryLatency();
					System.out.println("Total Latency: "+currentLatency);
					// break forloop
					i = temporaryLatencys.size();
				} else {
					// delete Item from temporaryLatencys
					temporaryLatencys.remove(i);
					// reorganize

					// break forloop
					i = temporaryLatencys.size();
				}
			}
		}
	}

	private void calcQueryLatency() {
		long latency = 0;
		for (String o : latencys.keySet()) {
			latency += latencys.get(o).getLatency();
		}
		setCurrentLatency(latency);
	}

	public long getCurrentLatency() {
		return currentLatency;
	}

	private void setCurrentLatency(long currentLatency) {
		this.currentLatency = currentLatency;
	}
}
