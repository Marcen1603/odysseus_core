package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationSequenceAnalysisAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

@SuppressWarnings("rawtypes")
public class DeviationSequenceAnalysisPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private static final int DATA_PORT = 0;
	private static final int LEARN_PORT_SINGLE_TUPLE = 1;
	// private static final int LEARN_PORT_CURVE = 2;

	// For analysis
	private Map<Long, DeviationInformation> deviationInfo;
	private double interval;
	private long lastCounter;
	private double totalSum;
	private double meanSum;

	// Learn attributes
	private String meanAttributeName;
	private String standardDeviationAttributeName;

	// Data attributes
	private String valueAttributeName;

	protected IGroupProcessor<T, T> groupProcessor;

	public DeviationSequenceAnalysisPO(DeviationSequenceAnalysisAO ao, IGroupProcessor<T, T> groupProcessor) {
		deviationInfo = new HashMap<Long, DeviationInformation>();

		this.interval = ao.getInterval();

		this.meanAttributeName = ao.getMeanAttributeName();
		this.standardDeviationAttributeName = ao.getStandardDeviationAttributeName();

		this.valueAttributeName = ao.getValueAttributeName();

		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {

		if (port == DATA_PORT) {
			// Process data
			long sequenceCounter = this.groupProcessor.getGroupID(tuple);
			DeviationInformation info = deviationInfo.get(sequenceCounter);
			if (info == null) {
				// We don't have information about this yet, hence, we can't
				// say anything. (Probably we did not get any deviation
				// information yet)
				return;
			}

			if (lastCounter > sequenceCounter) {
				// A new sequence starts, the last one is finished
				// We can now send the info about the last sequence
				double totalDifference = Math.abs(totalSum - meanSum);

				Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(6, false);
				output.setMetadata(tuple.getMetadata());
				output.setAttribute(0, totalSum);
				output.setAttribute(1, meanSum);
				output.setAttribute(2, totalDifference);
				output.setAttribute(3, totalDifference / meanSum);
				output.setAttribute(4, lastCounter);
				output.setAttribute(5, totalSum / lastCounter);

				transfer(output, 1);

				totalDifference = 0;
				totalSum = 0;
				meanSum = 0;
			}

			lastCounter = sequenceCounter;

			totalSum += getValue(tuple);
			meanSum += info.mean;

			if (isAnomaly(getValue(tuple), info.standardDeviation, info.mean)) {
				// We have an anomaly for this tuple
				double anomalyScore = calcAnomalyScore(getValue(tuple), info.mean, info.standardDeviation,
						this.interval);
				Tuple newTuple = tuple.append(anomalyScore).append(info.mean).append(info.standardDeviation);
				transfer(newTuple);
			}
		} else if (port == LEARN_PORT_SINGLE_TUPLE) {
			// Update deviation information
			updateDeviationInfo(tuple);
		}
	}

	private boolean isAnomaly(double sensorValue, double standardDeviation, double mean) {
		if (sensorValue < mean - (interval * standardDeviation) || sensorValue > mean + (interval * standardDeviation)) {
			return true;
		}
		return false;
	}

	/**
	 * Helper function to easily get the value in the attribute specified in the
	 * valueAttributeName variable via the AO
	 * 
	 * @param tuple
	 *            The tuple where this attribute is in
	 * @return The double value in the tuple
	 */
	protected double getValue(T tuple) {
		int valueIndex = getInputSchema(DATA_PORT).findAttributeIndex(valueAttributeName);
		if (valueIndex >= 0) {
			double sensorValue = tuple.getAttribute(valueIndex);
			return sensorValue;
		}
		return 0;
	}

	/**
	 * Updates the deviation info that belongs to the given tuple
	 * 
	 * @param tuple
	 *            The tuple with which the deviation info should be updated
	 * @return The updated deviation info
	 */
	private DeviationInformation updateDeviationInfo(T tuple) {
		long gId = this.groupProcessor.getGroupID(tuple);
		DeviationInformation info = this.deviationInfo.get(gId);
		if (info == null) {
			info = new DeviationInformation();
			deviationInfo.put(gId, info);
		}

		int meanIndex = getInputSchema(LEARN_PORT_SINGLE_TUPLE).findAttributeIndex(meanAttributeName);
		int stdDevIndex = getInputSchema(LEARN_PORT_SINGLE_TUPLE).findAttributeIndex(standardDeviationAttributeName);
		if (meanIndex >= 0 && stdDevIndex >= 0) {
			double mean = tuple.getAttribute(meanIndex);
			double stdDev = tuple.getAttribute(stdDevIndex);
			info.mean = mean;
			info.standardDeviation = stdDev;

		}
		deviationInfo.put(gId, info);
		return info;
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1.
	 * 
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double sensorValue, double mean, double standardDeviation, double interval) {

		double minValue = mean - (interval * standardDeviation);
		double maxValue = mean + (interval * standardDeviation);

		double distance = sensorValue > maxValue ? sensorValue - maxValue : sensorValue < minValue ? minValue
				- sensorValue : 0;

		double div = distance / (maxValue - minValue);

		double addValue = 0.5;
		double anomalyScore = 0;
		while (div > 0 && !Double.isInfinite(div)) {
			if (div >= 1) {
				anomalyScore += addValue;
				addValue /= 2;
				div -= 1;
			} else {
				anomalyScore += div * addValue;
				div -= 1;
			}
		}

		if (Double.isInfinite(div)) {
			anomalyScore = 1;
		}

		return anomalyScore;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
