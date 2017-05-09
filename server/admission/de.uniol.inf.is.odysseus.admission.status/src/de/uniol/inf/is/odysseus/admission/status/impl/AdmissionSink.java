package de.uniol.inf.is.odysseus.admission.status.impl;

import de.uniol.inf.is.odysseus.admission.status.loadshedding.LatencyAdmissionMonitor;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class AdmissionSink<R extends IStreamObject<? extends ILatency>> extends AbstractSink<R> {
	
	private LatencyAdmissionMonitor monitor;
	
	public AdmissionSink() {
		setInputPortCount(1);
	}
	
	@Override
	protected void process_next(R object, int port) {
			IPhysicalQuery owner = (IPhysicalQuery) this.getOwner().get(0);
			object.getMetadata().setLatencyEnd(System.nanoTime());
			long latency = object.getMetadata().getLatency();
			monitor.updateMeasurement(owner, latency);
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
