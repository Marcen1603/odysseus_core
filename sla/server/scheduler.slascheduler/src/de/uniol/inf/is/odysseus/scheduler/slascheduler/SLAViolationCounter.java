package de.uniol.inf.is.odysseus.scheduler.slascheduler;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.sla.metric.UpdateRateSink;
import de.uniol.inf.is.odysseus.core.server.sla.metric.UpdateRateSource;

public class SLAViolationCounter implements ISLAViolationEventListener {

	private static Map<Integer, Integer> numberOfViolationsUpRaSo = new HashMap<>();
	private static Map<Integer, Integer> numberOfViolationsUpRaSi = new HashMap<>();
	private static boolean hasChangedUpRaSo = false;
	private static boolean hasChangedUpRaSi = false;
	
	@Override
	public void slaViolated(SLAViolationEvent event) {
		if (event.getServiceLevel() != 0) { // if ServiceLevel = 0 no violation occurred
			if (event.getMetric() instanceof UpdateRateSource) {
				int queryID = event.getQuery().getID();
				if (!numberOfViolationsUpRaSo.containsKey(queryID)) {
					numberOfViolationsUpRaSo.put(queryID, 1);
				} else {
					int newNumber = numberOfViolationsUpRaSo.get(queryID) + 1;
					numberOfViolationsUpRaSo.put(queryID, newNumber);
				}
				hasChangedUpRaSo = true;
			} else if (event.getMetric() instanceof UpdateRateSink) {
				int queryID = event.getQuery().getID();
				if (!numberOfViolationsUpRaSi.containsKey(queryID)) {
					numberOfViolationsUpRaSi.put(queryID, 1);
				} else {
					int newNumber = numberOfViolationsUpRaSi.get(queryID) + 1;
					numberOfViolationsUpRaSi.put(queryID, newNumber);
				}
				hasChangedUpRaSi = true;
			}
		}
	}

	/**
	 * @return the numberOfViolationsUpRaSo
	 */
	public static Map<Integer, Integer> getNumberOfViolationsUpRaSo() {
		return new HashMap<Integer, Integer>(numberOfViolationsUpRaSo);
	}

	/**
	 * @return the numberOfViolationsUpRaSi
	 */
	public static Map<Integer, Integer> getNumberOfViolationsUpRaSi() {
		return new HashMap<Integer, Integer>(numberOfViolationsUpRaSi);
	}

	/**
	 * @return the hasChangedUpRaSo
	 */
	public static boolean hasChangedUpRaSo() {
		return hasChangedUpRaSo;
	}

	/**
	 * @return the hasChangedUpRaSi
	 */
	public static boolean hasChangedUpRaSi() {
		return hasChangedUpRaSi;
	}

}
