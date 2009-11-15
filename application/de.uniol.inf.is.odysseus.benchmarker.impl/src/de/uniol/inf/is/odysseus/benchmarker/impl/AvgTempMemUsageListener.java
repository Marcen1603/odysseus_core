package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.benchmarker.DescriptiveStatistics;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.PunctuationStorage;
import de.uniol.inf.is.odysseus.physicaloperator.base.IBuffer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;

/**
 * Collects memory usage for a given operator using SweepAreas.
 * 
 * @author Jan Steinke
 * 
 */
public class AvgTempMemUsageListener implements POEventListener {

	private Object value;
	
	private DescriptiveStatistics stats = new DescriptiveStatistics();

	public DescriptiveStatistics getStats() {
		return stats;
	}

	public AvgTempMemUsageListener(Object op) {
		this.value = op;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void poEventOccured(POEvent poEvent) {
		long tmp = 0;

		if (value instanceof JoinTIPO) {
			final ISweepArea[] areas = ((JoinTIPO) value).getAreas();

			for (ISweepArea each : areas) {
				tmp += each.size();
			}
		} else if (value instanceof IBuffer) {
			tmp = ((IBuffer) value).size();
		} else if (value instanceof PunctuationStorage) {
			PunctuationStorage punc = (PunctuationStorage) value;
			tmp = punc.size();
		}
		try {
			stats.addValue(tmp);
		} catch(Exception e) {
		}
	}



}
