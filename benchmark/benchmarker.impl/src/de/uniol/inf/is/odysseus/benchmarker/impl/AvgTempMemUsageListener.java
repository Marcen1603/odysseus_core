package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;

/**
 * Collects memory usage for a given operator using SweepAreas.
 * 
 * @author Jan Steinke
 * 
 */
public class AvgTempMemUsageListener implements IPOEventListener {

	private DescriptiveStatistics stats = new DescriptiveStatistics();

	public DescriptiveStatistics getStats() {
		return stats;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void eventOccured(IEvent<?,?> event) {
		POEvent poEvent = (POEvent) event;
		long tmp = 0;

		IPhysicalOperator sourceOp = poEvent.getSender();
		if (sourceOp instanceof JoinTIPO) {
			final ISweepArea[] areas = ((JoinTIPO) sourceOp).getAreas();

			for (ISweepArea each : areas) {
				tmp += each.size();
			}
		} else if (sourceOp instanceof IBuffer) {
			tmp = ((IBuffer) sourceOp).size();
		}
		try {
			stats.addValue(tmp);
		} catch (Exception e) {
		}
	}

}
