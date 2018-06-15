package de.uniol.inf.is.odysseus.server.monitoring.query;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;

public class QueryEventListener implements IPOEventListener {
	private Measurement measurement;
	private ThreadCalculateLatency thread;

	/**
	 * Adds required Events (PushInit, ProcessInit and ProcessDone) for an Operator
	 * @param o Operator which will be subscribed to the Events.
	 */
	public void addOperator(IPhysicalOperator o) {
		o.subscribe(this, POEventType.PushInit);
		o.subscribe(this, POEventType.ProcessInit);
		o.subscribe(this, POEventType.ProcessDone);
	}

	public QueryEventListener(Measurement m, ThreadCalculateLatency t) {
		this.thread = t;
		this.measurement = m;
	}

	@Override
	public void eventOccured(IEvent<?, ?> event, long nanoTimestamp) {
		this.thread.addEvent(this.measurement, event, nanoTimestamp);
	}
	
	/**
	 * Removes all Eventsubsciptions for the given Operator
	 * @param o IPhysicalOperator
	 */
	public void removeOperator(IPhysicalOperator o) {
		o.unsubscribe(this, POEventType.PushInit);
		o.unsubscribe(this, POEventType.ProcessInit);
		o.unsubscribe(this, POEventType.ProcessDone);
	}
}
