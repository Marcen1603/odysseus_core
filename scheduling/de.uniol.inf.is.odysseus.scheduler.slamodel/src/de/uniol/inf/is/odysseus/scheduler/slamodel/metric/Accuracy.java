package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.RatioUnit;

public class Accuracy extends Metric<Float, RatioUnit> {

	public Accuracy(Float value, RatioUnit unit) {
		super(value, unit);
	}

	@Override
	public boolean valueIsMin() {
		return true;
	}

}
