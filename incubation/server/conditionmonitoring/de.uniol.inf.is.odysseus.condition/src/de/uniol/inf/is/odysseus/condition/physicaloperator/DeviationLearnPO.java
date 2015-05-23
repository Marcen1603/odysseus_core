package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationLearnAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.NoGroupProcessor;

@SuppressWarnings("rawtypes")
public class DeviationLearnPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private String valueAttributeName;
	private double manualStandardDeviation;
	private double manualMean;
	private long tuplesToLearn;
	private IGroupProcessor<T, T> groupProcessor;
	private Map<Long, DeviationInformation> deviationInfo;
	private Map<Long, List<T>> tupleMap;
	private boolean exactCalculation;
	private TrainingMode trainingMode;

	public DeviationLearnPO(TrainingMode trainingMode, String attributeName) {
		this.groupProcessor = (IGroupProcessor<T, T>) new NoGroupProcessor<T, T>();
		this.deviationInfo = new HashMap<Long, DeviationInformation>();
		this.valueAttributeName = attributeName;
		this.trainingMode = trainingMode;
	}

	public DeviationLearnPO(DeviationLearnAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.groupProcessor = groupProcessor;
		this.trainingMode = ao.getTrainingMode();
		this.deviationInfo = new HashMap<Long, DeviationInformation>();
		this.valueAttributeName = ao.getNameOfValue();
		this.tuplesToLearn = ao.getTuplesToLearn();
		this.exactCalculation = ao.isExactCalculation();

		if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			this.manualMean = ao.getManualMean();
			this.manualStandardDeviation = ao.getManualStandardDeviation();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {

		// Get the group for this tuple (e.g., the incoming values have
		// different contexts)
		Long gId = groupProcessor.getGroupID(tuple);
		DeviationInformation info = this.deviationInfo.get(gId);

		if (info == null) {
			info = new DeviationInformation();
			this.deviationInfo.put(gId, info);
		}

		double sensorValue = getValue(tuple);

		if (this.trainingMode.equals(TrainingMode.ONLINE)) {
			// Calculate new value for standard deviation
			info = calcStandardDeviationOnline(sensorValue, info);
		} else if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Don't change the values - they are set by the user
			info.standardDeviation = this.manualStandardDeviation;
			info.mean = this.manualMean;
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			// Only change the values the first tuplesToLearn tuples (per group)
			if (info.n <= this.tuplesToLearn) {
				info = calcStandardDeviationOnline(sensorValue, info);
			}
		} else if (this.trainingMode.equals(TrainingMode.WINDOW)) {
			// Use the window which is before this operator
			processTupleWithWindow(gId, tuple, info, this.exactCalculation);
		}

		Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(3, false);
		output.setMetadata(tuple.getMetadata());
		output.setAttribute(0, gId);
		output.setAttribute(1, info.mean);
		output.setAttribute(2, info.standardDeviation);
		transfer(output);
	}

	/**
	 * Processes a tuple if the operator is used in window mode. You have the
	 * choice to use the exact calculation or the (faster) approximate
	 * calculation.
	 * 
	 * @param gId
	 *            The id of the group for this tuple
	 * @param tuple
	 *            The new tuple
	 * @param info
	 *            The info object for this group
	 * @param exactCalculation
	 *            Your choice if you want to use exact calculation
	 */
	private void processTupleWithWindow(Long gId, T tuple, DeviationInformation info, boolean exactCalculation) {
		List<T> tuples = tupleMap.get(gId);
		if (tuples == null) {
			tuples = new ArrayList<T>();
			tupleMap.put(gId, tuples);
		}
		tuples.add(tuple);
		removeOldValues(tuples, tuple.getMetadata().getStart(), info, exactCalculation);
		if (exactCalculation) {
			addValue(getValue(tuple), info);
			info.standardDeviation = calcStandardDeviationApprox(info);
			info.mean = getMeanValue(info);
		} else {
			info.standardDeviation = calcStandardDeviationOffline(tuples, info);
		}
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param tuples
	 *            The list of tuples from which the old ones have to be removed
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<T> tuples, PointInTime start, DeviationInformation info, boolean exactCalculation) {
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next.getMetadata().getEnd().beforeOrEquals(start)) {
				if (!exactCalculation)
					removeValue(getValue(next), info);
				iter.remove();
			}
		}
	}

	/*
	 * ----- ONLINE -----
	 */

	/**
	 * Calculates the standard deviation of the data seen so far (online).
	 * (http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#
	 * Online_algorithm)
	 * 
	 * @param newValue
	 *            The new value which came in
	 * @param info
	 *            The deviation information object you want to update
	 * @return Updated info object with new mean and new standard deviation
	 */
	protected DeviationInformation calcStandardDeviationOnline(double newValue, DeviationInformation info) {
		info.n += 1;
		double delta = newValue - info.mean;
		info.mean += (delta / info.n);
		info.m2 += delta * (newValue - info.mean);

		if (info.n < 2) {
			info.standardDeviation = 0;
		} else {
			double variance = info.m2 / (info.n - 1);
			double standardDeviation = Math.sqrt(variance);
			info.standardDeviation = standardDeviation;
		}

		return info;
	}

	/*
	 * ----- WINDOW (approximate)------
	 */

	/**
	 * Adds a value to the approximate standard deviation (and mean) calculation
	 * 
	 * @param value
	 *            The value which has to be added
	 * @param info
	 *            The info object for this group
	 */
	private void addValue(double value, DeviationInformation info) {
		// It would be nice to know the mean of the distribution, but we don't
		// know before we see the tuples. Hence, don't set k to the mean, but
		// to the first value we see. A value near the mean would be perfect.
		// A problem occurs when the mean value changes dramatically.
		if (info.n == 0)
			info.k = value;

		info.n++;
		info.sumWindow += value - info.k;
		info.sumWindowSqr += (value - info.k) * (value - info.k);
	}

	/**
	 * Removes a value from the approximate standard deviation (and mean)
	 * calculation
	 * 
	 * @param value
	 *            The value which has to be removed
	 * @param info
	 *            The info object for this group
	 */
	private void removeValue(double value, DeviationInformation info) {
		info.n--;
		info.sumWindow -= value - info.k;
		info.sumWindowSqr -= (value - info.k) * (value - info.k);
	}

	/**
	 * Calculates the approximate mean value for the given group info object
	 * 
	 * @param info
	 *            The info object for this group
	 * @return The approximate mean value
	 */
	private double getMeanValue(DeviationInformation info) {
		return info.k + (info.sumWindow / info.n);
	}

	/**
	 * Calculates an approximation of the standard calculation
	 * 
	 * @param info
	 *            The info object for this group
	 * @return The approximate standard deviation
	 */
	private double calcStandardDeviationApprox(DeviationInformation info) {
		double variance = (info.sumWindowSqr - ((info.sumWindow * info.sumWindow) / info.n)) / (info.n - 1);
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	/*
	 * ------ OFFLINE (exact) -------
	 */

	/**
	 * Exact (but expensive) calculation of the standard deviation
	 * 
	 * @param tuples
	 *            List of tuples for which the standard deviation needs to be
	 *            calculated
	 * @param info
	 *            The info object for this group (there you can find the mean
	 *            after the calculation)
	 * @return The standard deviation of the given list
	 */
	private double calcStandardDeviationOffline(List<T> tuples, DeviationInformation info) {
		info.n = 0;
		info.sum1 = 0;
		info.sum2 = 0;

		for (T tuple : tuples) {
			info.n++;
			info.sum1 += getValue(tuple);
		}

		info.mean = info.sum1 / info.n;

		for (T tuple : tuples) {
			info.sum2 += (getValue(tuple) - info.mean) * (getValue(tuple) - info.mean);
		}

		double variance = info.sum2 / (info.n - 1);
		info.standardDeviation = Math.sqrt(variance);
		return info.standardDeviation;
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
		int valueIndex = getInputSchema(0).findAttributeIndex(valueAttributeName);
		if (valueIndex >= 0) {
			double sensorValue = tuple.getAttribute(valueIndex);
			return sensorValue;
		}
		return 0;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation, port);
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
}
