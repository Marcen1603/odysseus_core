package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.RatioUnit;

/**
 * This Metric is a measure for the Quality of Answers (QoA)
 * @author Thomas Vogelgesang
 *
 */
public class Accuracy extends Metric<RatioUnit> {

	/**
	 * Creates a new {@link Accuracy} metric
	 * @param value the value of the metric
	 * @param unit the unit of the metric
	 */
	public Accuracy(double value, RatioUnit unit) {
		super(value, unit);
	}

	/**
	 * returns true because given thresholds minimum values 
	 */
	@Override
	public boolean valueIsMin() {
		return true;
	}

}
