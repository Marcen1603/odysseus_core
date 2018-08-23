package de.uniol.inf.is.odysseus.anomalydetection.physicaloperator;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.ValueAreaAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;

/**
 * An operator that finds anomalies when the values are not in the declared area
 * and gives the anomalies a score.
 *
 * @author Tobias Brandt
 *
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public class ValueAreaAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> implements IHasPredicate {

	private String valueAttributeName;

	private double minValue;
	private double maxValue;
	private boolean sendAllAnomalies;

	private boolean tooLow = false;;
	private boolean tooHigh = false;;
	private boolean sendTooHigh = false;;
	private boolean wasNormalTuple = false;;
	private double distance;
	private double lastSendDistance;
	private double nextWarningDistance = 1.0;

	// Additional predicate which needs to be true to send anomalies (if not
	// sendAllAnomalies)
	private IPredicate<? super T> predicate;

	/**
	 * Create the physical operator from the logical operator.
	 *
	 * @param ao
	 */
	@SuppressWarnings("unchecked")
	public ValueAreaAnomalyDetectionPO(ValueAreaAnomalyDetectionAO ao) {
		this.minValue = ao.getMinValue();
		this.maxValue = ao.getMaxValue();
		this.sendAllAnomalies = ao.sendAllAnomalies();
		tooLow = false;
		tooHigh = false;
		this.valueAttributeName = ao.getNameOfValue();
		if (ao.getPredicate() != null)
			this.predicate = (IPredicate<? super T>) ao.getPredicate().clone();
	}

	@Override
	protected void process_next(T object, int port) {

		int valueIndex = getOutputSchema().findAttributeIndex(valueAttributeName);
		double sensorValue = object.getAttribute(valueIndex);

		// Calculate the distance to the normal area. Distance is always
		// positive or 0
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
			if ((predicate == null && (wasNormalTuple || tooHigh && !sendTooHigh || tooLow && sendTooHigh || Math
					.abs(distance - lastSendDistance) > nextWarningDistance * (maxValue - minValue)))
					|| (predicate != null && predicate.evaluate(object))) {
				// Send a new warning if we don't have a predicate and
				// 0. The last tuple was normal (no anomaly) OR
				// 1. We are now too high, but last tuple was too low OR
				// 2. We are now too low, but last tuple was too high OR
				// 3. The distance got (much) bigger in comparison to the
				// "good area"
				// OR if we use a predicate
				lastSendDistance = distance;
				sendTooHigh = tooHigh;
				wasNormalTuple = false;
				Tuple newTuple = object.append(anomalyScore);
				transfer(newTuple);
				return;
			}
		}

		if (distance <= 0) {
			wasNormalTuple = true;
		}
		
		// Send all the other things on port 1
		Tuple newTuple = object.append(anomalyScore);
		transfer(newTuple, 1);
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
	public IPredicate<?> getPredicate() {
		return predicate;
	}

	@Override
	public void process_open() throws OpenFailedException {
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		IPunctuation outPunctuation = predicate.processPunctuation(punctuation);
		sendPunctuation(outPunctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void setPredicate(IPredicate<?> predicate) {
		// TODO 
		
	}

}