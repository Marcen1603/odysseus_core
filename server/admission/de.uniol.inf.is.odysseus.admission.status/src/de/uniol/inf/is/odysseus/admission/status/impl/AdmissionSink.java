package de.uniol.inf.is.odysseus.admission.status.impl;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LatencyAdmissionMonitor;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class AdmissionSink<R extends IStreamObject<?>> extends AbstractSink<R> {
	
	private final int MEASURE_FREQUENCY = 20;
	private int measurement = 0;
	private LatencyAdmissionMonitor monitor;
	
	@Override
	protected void process_next(R object, int port) {
		if(measurement < MEASURE_FREQUENCY) {
			
			long latency = System.nanoTime() - (long) object.getMetadata().getValue(0, 0);
			IPhysicalQuery owner = (IPhysicalQuery) this.getOwner().get(0);
			monitor.updateMeasurement(owner, latency);
		}
		measurement = (measurement + MEASURE_FREQUENCY) % 100;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {		
	}

	@Override
	public AbstractSink<R> clone() {
		return null;
	}
	
	public void setLatencyAdmissionMonitor(LatencyAdmissionMonitor monitor) {
		this.monitor = monitor;
	}

}
