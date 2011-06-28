package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.ThroughputUnit;

/**
 * This metric is a measure for throughput
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Throughput extends Metric<Integer, ThroughputUnit> {

	/**
	 * creates a new throughput metric
	 * 
	 * @param value
	 *            the metric's value
	 * @param unit
	 *            the metric's unit
	 */
	public Throughput(Integer value, ThroughputUnit unit) {
		super(value, unit);
	}

	/**
	 * returns always true, because thresholds ar defined as minimium values
	 */
	@Override
	public boolean valueIsMin() {
		return true;
	}

}
