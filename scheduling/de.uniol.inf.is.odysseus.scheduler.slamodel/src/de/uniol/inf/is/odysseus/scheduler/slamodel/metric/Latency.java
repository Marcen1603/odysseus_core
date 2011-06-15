package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

public class Latency extends Metric<Integer, TimeUnit> {

	public Latency(Integer value, TimeUnit unit) {
		super(value, unit);
	}

	@Override
	public boolean valueIsMin() {
		return false;
	}

}
