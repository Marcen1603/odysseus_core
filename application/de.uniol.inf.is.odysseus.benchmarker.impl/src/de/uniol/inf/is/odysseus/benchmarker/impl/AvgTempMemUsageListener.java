package de.uniol.inf.is.odysseus.benchmarker.impl;

import java.util.List;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.PointInTime;
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

	private double min = -1;
	private double max = -1;
	private double collected;
	private double called_times;

	private Object value;

	public AvgTempMemUsageListener(Object op) {
		this.value = op;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void poEventOccured(POEvent poEvent) {

		double tmp = 0;

		if (value instanceof JoinTIPO) {
			final ISweepArea[] areas = ((JoinTIPO) value).getAreas();

			for (ISweepArea each : areas) {
				tmp += each.size();
			}
		} else if (value instanceof IBuffer) {
			tmp = ((IBuffer) value).size();
		} else if (value instanceof PunctuationStorage) {
			System.out.println("PUNC!!!!");
			PunctuationStorage punc = (PunctuationStorage) value;
			for (Object eachPort : punc.getStorage()) {
				tmp += ((List<PointInTime>) eachPort).size();
			}
		}

		if (min == -1 || tmp < min) {
			this.min = tmp;
		}

		if (max == -1 || tmp > max) {
			this.max = tmp;
		}

		collected += tmp;
		called_times++;

	}

	public double getAverage() {
		if (called_times == 0) {
			return -1;
		} else {
			return collected / called_times;
		}
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

}
