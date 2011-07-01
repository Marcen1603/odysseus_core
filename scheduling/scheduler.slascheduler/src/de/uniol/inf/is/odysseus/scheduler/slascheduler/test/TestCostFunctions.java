package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

import de.uniol.inf.is.odysseus.scheduler.slascheduler.ICostFunction;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyAverage;
import de.uniol.inf.is.odysseus.scheduler.slascheduler.cost.QuadraticCFLatencyNumber;
import de.uniol.inf.is.odysseus.sla.SLA;

public class TestCostFunctions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SLA slaLatencyAvg = TestUtils.buildSLAAverage();
		ICostFunction cf = new QuadraticCFLatencyAverage();
		assert (cf.oc(150, slaLatencyAvg) == 56);
		assert (cf.oc(50, slaLatencyAvg) == 6);
		assert (null != null);

		assert (cf.oc(350, slaLatencyAvg) == 56);
		assert (cf.oc(20, slaLatencyAvg) == 6);

		assert (cf.mg(150, slaLatencyAvg) == 6);
		assert (cf.mg(50, slaLatencyAvg) == 56);

		cf = new QuadraticCFLatencyNumber();
		assert (cf.oc(150, slaLatencyAvg) == 56);
		assert (cf.oc(50, slaLatencyAvg) == 6);

		assert (cf.mg(350, slaLatencyAvg) == 6);
		assert (cf.mg(250, slaLatencyAvg) == 56);

		// TODO standard java assert is disabled by default :(
		// use JUnit instead? or write own assertion tool?
	}

}
