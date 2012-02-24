package de.uniol.inf.is.odysseus.core.server.sla.metric;

import de.uniol.inf.is.odysseus.core.server.sla.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.core.sla.Metric;

/**
 * This metric is a measure for throughput
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class Throughput extends Metric<ThroughputUnit> {

	/**
	 * creates a new throughput metric
	 * 
	 * @param value
	 *            the metric's value
	 * @param unit
	 *            the metric's unit
	 */
	public Throughput(double value, ThroughputUnit unit) {
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
