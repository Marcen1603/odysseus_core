package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.condition.logicaloperator.ValueAreaAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * An operator that finds anomalies when the values are not in the declared area
 * and gives the anomalies a score.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class ValueAreaAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	private String valueAttributeName;
	
	private double minValue;
	private double maxValue;
	private boolean sendAllAnomalies;

	private boolean tooLow;
	private boolean tooHigh;
	private boolean sendTooHigh;
	private double distance;
	private double lastSendDistance;
	private double nextWarningDistance = 1.0;

	/**
	 * Create the physical operator from the logical operator.
	 * 
	 * @param ao
	 */
	public ValueAreaAnomalyDetectionPO(ValueAreaAnomalyDetectionAO ao) {
		this.minValue = ao.getMinValue();
		this.maxValue = ao.getMaxValue();
		this.sendAllAnomalies = ao.sendAllAnomalies();
		tooLow = false;
		tooHigh = false;
		this.valueAttributeName = ao.getNameOfValue();
	}

	/**
	 * Create the physical operator.
	 * 
	 * @param minValue
	 *            The minimal value which is in the accepted area
	 * @param maxValue
	 *            The maximal value which is in the accepted area
	 * @param sendAllAnomalies
	 *            Set to true, if you want to have a tuple for every tuple which
	 *            is an anomaly. If set to false, the operator will send
	 *            anomaly-tuples if the value changed significantly or turned
	 *            from being to small to being to high or the other way around
	 */
	public ValueAreaAnomalyDetectionPO(double minValue, double maxValue, boolean sendAllAnomalies) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.sendAllAnomalies = sendAllAnomalies;
		tooLow = false;
		tooHigh = false;
	}

	@Override
	protected void process_next(T object, int port) {

		int valueIndex = getOutputSchema().findAttributeIndex(valueAttributeName);
		double sensorValue = object.getAttribute(valueIndex);

		// Calc the distance to the normal area. Distance is always positive or
		// 0
		distance = sensorValue > maxValue ? sensorValue - maxValue : sensorValue < minValue ? minValue - sensorValue
				: 0;
		double anomalyScore = calcAnomalyScore(distance);

		// In case that we want to see all the anomalies: just send the anomaly
		// score
		if (distance > 0 && sendAllAnomalies) {
			Tuple newTuple = object.append(anomalyScore);
			transfer(newTuple);
			return;
		}

		// In case that we want an "intelligent" filtering
		// Check, if we are too high or too low
		tooHigh = sensorValue > maxValue;
		tooLow = sensorValue < minValue;

		if (distance > 0) {
			if (tooHigh && !sendTooHigh || tooLow && sendTooHigh
					|| Math.abs(distance - lastSendDistance) > nextWarningDistance * (maxValue - minValue)) {
				// Send a new warning if
				// 1. We are now too high, but last tuple was too low
				// 2. We are now too low, but last tuple was too high
				// 3. The distance got (much) bigger in comparison to the
				// "good area"
				lastSendDistance = distance;
				sendTooHigh = tooHigh;
				Tuple newTuple = object.append(anomalyScore);
				transfer(newTuple);
				return;
			}
		}
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1. (Will actually
	 * never reach 1)
	 * 
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double distance) {

		double div = distance / (maxValue - minValue);

		double addValue = 0.5;
		double anomalyScore = 0;
		while (div > 0) {
			if (div >= 1) {
				anomalyScore += addValue;
				addValue /= 2;
				div -= 1;
			} else {
				anomalyScore += div * addValue;
				div -= 1;
			}
		}

		return anomalyScore;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
