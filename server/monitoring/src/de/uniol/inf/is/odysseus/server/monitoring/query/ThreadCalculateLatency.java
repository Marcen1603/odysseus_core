package de.uniol.inf.is.odysseus.server.monitoring.query;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;

public class ThreadCalculateLatency extends Thread {

	Measurement measurement;
	private IEvent<?, ?> event;
	private long nanoTimeStamp;

	public ThreadCalculateLatency(Measurement m, IEvent<?, ?> e, long nanoTimestamp) {
		this.measurement = m;
		this.event = e;
		this.nanoTimeStamp = nanoTimestamp;
		this.setName("Calculating_Latencys");
	}

	@Override
	public void run() {
		printEvent();
		IPhysicalOperator o = (IPhysicalOperator) this.event.getSender();
		if (event.getEventType().equals(POEventType.ProcessInit)) {
			this.measurement.createNewTempLatency(o.toString(), this.nanoTimeStamp);
		}
		if (event.getEventType().equals(POEventType.PushInit)) {
			this.measurement.calcTemporaryOperatorLatency(o.toString(), this.nanoTimeStamp);
		}
	}
	
	private synchronized void printEvent(){

		System.out.println(nanoTimeStamp + event.toString());
	}
}
