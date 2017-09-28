package de.uniol.inf.is.odysseus.server.monitoring.query;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class QueryEventListener implements IPOEventListener {
	private Measurement measurement;
	ThreadCalculateLatency thread;

	public void addOperator(IPhysicalOperator o) {
		o.subscribe(this, POEventType.PushInit);
		o.subscribe(this, POEventType.ProcessInit);
		o.subscribe(this, POEventType.ProcessDone);
	}

	public QueryEventListener(Measurement m, ThreadCalculateLatency t) {
		// TODO Auto-generated constructor stub	
		this.thread = t;
		this.measurement = m;
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		this.thread.addEvent(this.measurement, event, nanoTimestamp);
	}
	
	public void removeOperator(IPhysicalOperator o) {
		o.unSubscribeFromAll(this);
	}
}
