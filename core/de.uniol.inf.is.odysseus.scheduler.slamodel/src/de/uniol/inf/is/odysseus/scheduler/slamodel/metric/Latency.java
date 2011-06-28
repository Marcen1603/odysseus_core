package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

/**
 * This Metric is a measure for the delay of tuples.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Latency extends Metric<Integer, TimeUnit> {

	/**
	 * creates a new metric for latency
	 * @param value the metric's value
	 * @param unit the metric's unit
	 */
	public Latency(Integer value, TimeUnit unit) {
		super(value, unit);
	}

	/**
	 * returns always false because thresholds define maximum values
	 */
	@Override
	public boolean valueIsMin() {
		return false;
	}

}
