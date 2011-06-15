package de.uniol.inf.is.odysseus.scheduler.slamodel.metric;

import de.uniol.inf.is.odysseus.scheduler.slamodel.Metric;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.ThroughputUnit;

public class Throughput extends Metric<Integer, ThroughputUnit> {

	public Throughput(Integer value, ThroughputUnit unit) {
		super(value, unit);
	}

	@Override
	public boolean valueIsMin() {
		return true;
	}

}
