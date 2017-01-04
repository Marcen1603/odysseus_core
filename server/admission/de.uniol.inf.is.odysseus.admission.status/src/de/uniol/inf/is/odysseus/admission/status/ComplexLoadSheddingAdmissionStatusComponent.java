package de.uniol.inf.is.odysseus.admission.status;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;

public class ComplexLoadSheddingAdmissionStatusComponent implements ILoadSheddingAdmissionStatusComponent {

	private final QueueLengthsAdmissionMonitor QUEUE_LENGTHS_MONITOR = new QueueLengthsAdmissionMonitor();
	private final LatencyAdmissionMonitor LATENCY_MONITOR = new LatencyAdmissionMonitor();
	
	@Override
	public void addQuery(IPhysicalQuery query) {
		QUEUE_LENGTHS_MONITOR.addQuery(query);
		LATENCY_MONITOR.addQuery(query);
	}

	@Override
	public void removeQuery(IPhysicalQuery query) {
		QUEUE_LENGTHS_MONITOR.removeQuery(query);
		LATENCY_MONITOR.removeQuery(query);
	}

	@Override
	public void runLoadShedding() {
		QUEUE_LENGTHS_MONITOR.getQuerysWithIncreasingTendency();
		LATENCY_MONITOR.getQuerysWithIncreasingTendency();
	}

	@Override
	public void rollBackLoadShedding() {
		

	}

	@Override
	public void measureStatus() {
		QUEUE_LENGTHS_MONITOR.updateMeasurements();
		LATENCY_MONITOR.updateMeasurements();
	}

}
