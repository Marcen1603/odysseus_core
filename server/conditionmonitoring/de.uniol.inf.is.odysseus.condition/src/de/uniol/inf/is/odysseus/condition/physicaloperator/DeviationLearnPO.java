package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator learns the mean and the standard deviation of an (number)
 * attribute of the incoming tuple. It sends the mean and the standard deviation
 * to the output port. The operator supports the different algorithms to
 * calculate the mean and standard deviation. The methods have different pros
 * and cons, therefore you can choose the algorithm that fits best to your
 * needs.
 * 
 * @author Tobias Brandt
 *
 */
@SuppressWarnings("rawtypes")
public class DeviationLearnPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	public static final int DATA_OUT = 1;

	private String valueAttributeName;
	private double manualStandardDeviation;
	private double manualMean;
	private long tuplesToLearn;
	protected IGroupProcessor<T, T> groupProcessor;
	private Map<Object, DeviationInformation> deviationInfo;
	private Map<Object, List<T>> tupleMap;
	private boolean exactCalculation;
	private TrainingMode trainingMode;

	// For save backup management
	private String uniqueId;

	// Ports for input and output
	private static final int DATA_PORT = 0;
	private static final int BACKUP_PORT = 2;

	public DeviationLearnPO(TrainingMode trainingMode, String attributeName, IGroupProcessor<T, T> groupProcessor) {
		init();
		this.groupProcessor = groupProcessor;
		this.valueAttributeName = attributeName;
		this.trainingMode = trainingMode;
	}

	public DeviationLearnPO(DeviationLearnAO ao, IGroupProcessor<T, T> groupProcessor) {
		init();
		this.groupProcessor = groupProcessor;
		this.trainingMode = ao.getTrainingMode();
		this.valueAttributeName = ao.getNameOfValue();
		this.tuplesToLearn = ao.getTuplesToLearn();
		this.exactCalculation = ao.isExactCalculation();
		this.uniqueId = ao.getUniqueBackupId();

		if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			this.manualMean = ao.getManualMean();
			this.manualStandardDeviation = ao.getManualStandardDeviation();
		}
	}

	private void init() {
		this.deviationInfo = new HashMap<>();
		this.tupleMap = new HashMap<>();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {

		// Get the group for this tuple (e.g., the incoming values have
		// different contexts)
		Object gId = groupProcessor.getGroupID(tuple);
		DeviationInformation info = this.deviationInfo.get(gId);

		if (info == null) {
			info = new DeviationInformation();
			this.deviationInfo.put(gId, info);
		}

		if (port == DATA_PORT) {
			// Learn the deviation with a now incoming tuple
			processStandardTuple(tuple, info, gId);

			// Transfer learn information (whole deviation information) to port
			// 1 to back it up
			Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(11, false);
			output.setMetadata(tuple.getMetadata());
			output.setAttribute(0, gId);
			output.setAttribute(1, uniqueId.toString());

			output.setAttribute(2, info.mean);
			output.setAttribute(3, info.standardDeviation);

			output.setAttribute(4, info.n);
			output.setAttribute(5, info.m2);

			output.setAttribute(6, info.k);
			output.setAttribute(7, info.sumWindow);
			output.setAttribute(8, info.sumWindowSqr);

			output.setAttribute(9, info.sum1);
			output.setAttribute(10, info.sum2);
			transfer(output, BACKUP_PORT);
		} else if (port == BACKUP_PORT) {
			// Update the deviation information with the info from an external
			// operator, e.g. a backup database connection
			info = updateInfo(tuple, info, gId);
		}
	}

	/**
	 * Updates the deviation information with the data we got. Writes all
	 * deviation data (sums, etc.) info the info object
	 * 
	 * @param infoTuple
	 * @param info
	 * @param gId
	 * @return
	 */
	private DeviationInformation updateInfo(T infoTuple, DeviationInformation info, Object gId) {
		// Only use the data if it's from this operator
		String tupleBackupId = getValue(infoTuple, BACKUP_PORT, "backupId", String.class);
		if (tupleBackupId.equals(this.uniqueId)) {
			info.mean = getValue(infoTuple, BACKUP_PORT, "mean", Double.class);
			info.standardDeviation = getValue(infoTuple, BACKUP_PORT, "standardDeviation", Double.class);

			info.n = Math.round(getValue(infoTuple, BACKUP_PORT, "n", Double.class));
			info.m2 = getValue(infoTuple, BACKUP_PORT, "m2", Double.class);

			info.k = getValue(infoTuple, BACKUP_PORT, "k", Double.class);
			info.sumWindow = getValue(infoTuple, BACKUP_PORT, "sumWindow", Double.class);
			info.sumWindowSqr = getValue(infoTuple, BACKUP_PORT, "sumWindowSqr", Double.class);

			info.sum1 = getValue(infoTuple, BACKUP_PORT, "sum1", Double.class);
			info.sum2 = getValue(infoTuple, BACKUP_PORT, "sum2", Double.class);
		}
		return info;
	}

	private void processStandardTuple(T tuple, DeviationInformation info, Object gId) {

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
			info = processTupleWithWindow(gId, tuple, info, this.exactCalculation);
		}

		// Transfer results on port 0
		Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(3, false);
		output.setMetadata(tuple.getMetadata());
		output.setAttribute(0, gId);
		output.setAttribute(1, info.mean);
		output.setAttribute(2, info.standardDeviation);
		transfer(output);

		// Transfer original data (with group) on port 1 (the next operator
		// needs to map the groups identical)
		Tuple<ITimeInterval> inputDataWithGroup = new Tuple<ITimeInterval>(1, false);
		inputDataWithGroup.setMetadata(tuple.getMetadata());
		inputDataWithGroup.setAttribute(0, gId);
		inputDataWithGroup = inputDataWithGroup.appendList(Arrays.asList(tuple.getAttributes()), false);
		transfer(inputDataWithGroup, DATA_OUT);
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
	private DeviationInformation processTupleWithWindow(Object gId, T tuple, DeviationInformation info,
			boolean exactCalculation) {
		List<T> tuples = tupleMap.get(gId);
		if (tuples == null) {
			tuples = new ArrayList<T>();
			tupleMap.put(gId, tuples);
		}
		tuples.add(tuple);
		removeOldValues(tuples, tuple.getMetadata().getStart(), info, exactCalculation);
		if (!exactCalculation) {
			addValue(getValue(tuple), info);
			info.standardDeviation = calcStandardDeviationApprox(info);
			info.mean = getMeanValue(info);
		} else {
			info = calcStandardDeviationOffline(tuples, info);
		}

		return info;
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
	private void removeOldValues(List<T> tuples, PointInTime start, DeviationInformation info,
			boolean exactCalculation) {
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
	private DeviationInformation calcStandardDeviationOffline(List<T> tuples, DeviationInformation info) {
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

		double variance = 0;
		if (info.sum2 != 0) {
			// We have more than one value / not all values have the same value
			variance = info.sum2 / (info.n - 1);
		}
		info.standardDeviation = Math.sqrt(variance);
		return info;
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
			double sensorValue = 0;
			if (getInputSchema(DATA_PORT).findAttribute(valueAttributeName).getDatatype().isInteger()) {
				Integer intValue = tuple.getAttribute(valueIndex);
				sensorValue = intValue.doubleValue();
			} else {
				sensorValue = tuple.getAttribute(valueIndex);				
			}
			return sensorValue;
		}
		return 0;
	}

	protected <X extends Object> X getValue(T tuple, int port, String attributeName, Class<X> type) {
		int valueIndex = getInputSchema(port).findAttributeIndex(attributeName);
		if (valueIndex >= 0) {
			Object value = tuple.getAttribute(valueIndex);
			if (type.isInstance(value))
				return type.cast(value);
		}
		return null;
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
