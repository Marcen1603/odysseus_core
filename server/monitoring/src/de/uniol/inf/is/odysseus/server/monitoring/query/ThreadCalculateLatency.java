package de.uniol.inf.is.odysseus.server.monitoring.query;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;

public class ThreadCalculateLatency extends Thread {

	Measurements measurement;
	private IEvent<?, ?> event;
	private long nanoTimeStamp;

	public ThreadCalculateLatency(Measurements m, IEvent<?, ?> e, long nanoTimestamp) {
		this.measurement = m;
		this.event = e;
		this.nanoTimeStamp = nanoTimestamp;
		this.setName("Calculating_Latencys");
	}

	@Override
	public void run() {
		System.out.println("Event fired: " + this.event.toString() + " Zeit: " + this.nanoTimeStamp);
		IPhysicalOperator o = (IPhysicalOperator) this.event.getSender();
		if (event.getEventType().equals(POEventType.ProcessInit)) {
			this.measurement.createNewTempLatency(o.toString(), this.nanoTimeStamp);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			this.measurement.calcTemporaryOperatorLatency(o.toString(), this.nanoTimeStamp);
		}
	}
}
