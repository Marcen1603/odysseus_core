package de.uniol.inf.is.odysseus.temporaltypes.physicaloperator;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangePredictionTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ITemporalTrust;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.PredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * This operator manipulates the PredictionTimes metadata. It is doing this based on
 * the stream time interval. It uses the start timestamp of the stream time
 * interval (the "normal" time interval) of a stream element and adds /
 * substracts the given values to / from it to calculate the start and end
 * timestamp of the PredictionTime.
 * 
 * Removes all previous information in the PredictionTimes metadata.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class ChangePredictionTimePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	protected static final Logger LOG = LoggerFactory.getLogger(ChangePredictionTimePO.class);

	private boolean copyTimeInterval;
	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private boolean alingAtStreamEnd;
	private TimeUnit streamBaseTimeUnit;
	private TimeUnit predictionBaseTimeUnit;

	// Expressions can be used to calculate the start and end timestamp
	private RelationalExpression<IPredictionTimes> startExpression;
	private RelationalExpression<IPredictionTimes> endExpression;

	public ChangePredictionTimePO(ChangePredictionTimeAO ao, RelationalExpression<IPredictionTimes> startExpression,
			RelationalExpression<IPredictionTimes> endExpression) {
		this.valueToAddStart = ao.getValueToAddStart();
		this.valueToAddEnd = ao.getValueToAddEnd();
		this.alingAtStreamEnd = ao.isAlignAtEnd();
		this.streamBaseTimeUnit = ao.getBaseTimeUnit();
		this.copyTimeInterval = ao.isCopyTimeInterval();
		this.predictionBaseTimeUnit = ao.getPredictionBaseTimeUnit();

		this.startExpression = startExpression;
		this.endExpression = endExpression;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Do nothing
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		if (copyTimeInterval) {
			object = copyTimeInterval(object);
		} else {
			object = setTimeInterval(object, valueToAddStart, valueToAddEnd, alingAtStreamEnd);
		}

		IPredictionTimes predictionTimes = (IPredictionTimes) object.getMetadata();

		// The base time could be changed
		if (this.predictionBaseTimeUnit != null) {
			predictionTimes.setTimeUnit(predictionBaseTimeUnit);
		}

		// If the trust value is used, it needs to be set here
		if (object.getMetadata() instanceof IPredictionTimes) {
			changeTrust(object, predictionTimes);
		}

		transfer(object);
	}

	/**
	 * Sets the start and end timestamp of the PredictionTime using the given
	 * expressions.
	 * 
	 * @param object The tuple where the PredictionTimes have to be set with the
	 *               expressions
	 * @return The given tuple with edited metadata or null, if the expressions
	 *         cannot be used.
	 */
	private Tuple<IPredictionTimes> setStartEndWithExpression(T object) {

		if (!useExpressions(object)) {
			return null;
		}

		@SuppressWarnings("unchecked")
		Tuple<IPredictionTimes> tuple = (Tuple<IPredictionTimes>) object;

		try {
			Object startResult = startExpression.evaluate(tuple, null, null);
			Object endResult = endExpression.evaluate(tuple, null, null);
			if (startResult != null && endResult != null) {
				PointInTime start = new PointInTime(((Number) startResult).longValue());
				PointInTime end = new PointInTime(((Number) endResult).longValue());
				tuple.getMetadata().addPredictionTime(new PredictionTime(start, end));
			}
		} catch (Exception e) {
			LOG.error("Could not evaluate expressions.");
			e.printStackTrace();
		}

		return tuple;
	}

	private boolean useExpressions(T object) {
		if (!(object instanceof Tuple) || startExpression == null || endExpression == null) {
			return false;
		}
		return true;
	}

	private T copyTimeInterval(T object) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;
			changePredictionTime(metadata, streamTime.getStart(), streamTime.getEnd());
		}
		return object;
	}

	private T setTimeInterval(T object, TimeValueItem addToStart, TimeValueItem addToEnd, boolean alignAtStreamEnd) {
		IMetaAttribute metadata = object.getMetadata();
		if (useExpressions(object)) {
			// If we want to use an expression to set the start and end time
			setStartEndWithExpression(object);
		} else if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;

			long convertedValueToAddStart = streamBaseTimeUnit.convert(addToStart.getTime(), addToStart.getUnit());
			long convertedValueToAddEnd = streamBaseTimeUnit.convert(addToEnd.getTime(), addToEnd.getUnit());

			PointInTime baseTime;
			if (!alignAtStreamEnd) {
				baseTime = streamTime.getStart();
			} else {
				baseTime = streamTime.getEnd();
			}
			PointInTime newPredictionStart = baseTime.plus(convertedValueToAddStart);
			PointInTime newPredictionEnd = baseTime.plus(convertedValueToAddEnd);
			changePredictionTime(metadata, newPredictionStart, newPredictionEnd);
		}
		return object;
	}

	private void changeTrust(T streamElement, IPredictionTimes predictionTimes) {
		IMetaAttribute metadata = streamElement.getMetadata();

		if (metadata instanceof ITemporalTrust && streamElement instanceof Tuple) {
			// Put the trusts in the meta attribute
			ITemporalTrust oldTempTrust = (ITemporalTrust) metadata;

			for (IPredictionTime predictionTime : predictionTimes.getPredictionTimes()) {
				double[] trusts = getTrusts((Tuple<?>) streamElement, predictionTime);
				insertTrusts(trusts, predictionTime, oldTempTrust);
			}
		}
	}

	private void insertTrusts(double[] trusts, IPredictionTime predictionTime, ITemporalTrust temporalTrust) {
		int i = 0;
		for (PointInTime time = predictionTime.getPredictionStart(); time.before(predictionTime.getPredictionEnd()); time = time.plus(1)) {
			ITrust trust = new Trust();
			trust.setTrust(trusts[i]);
			temporalTrust.setTrust(time, trust);
			i++;
		}
	}

	private double[] getTrusts(Tuple<?> tuple, IPredictionTime predictionTime) {

		int size = (int) predictionTime.getPredictionEnd().minus(predictionTime.getPredictionStart()).getMainPoint();
		double[] trusts = new double[size];

		int i = 0;
		for (PointInTime time = predictionTime.getPredictionStart(); time.before(predictionTime.getPredictionEnd()); time = time.plus(1)) {
			double trust = getTrust(tuple, time);
			trusts[i] = trust;
			i++;
		}

		return trusts;
	}

	private double getTrust(Tuple<?> tuple, PointInTime predictionTime) {
		// Use lowest trust of all temporal attributes
		double minTrust = Double.MAX_VALUE;
		Object[] attributes = tuple.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof TemporalType) {
				TemporalType<?> temporalAttribute = (TemporalType<?>) attributes[i];
				double thisTrust = temporalAttribute.getTrust(predictionTime);
				if (thisTrust < minTrust) {
					minTrust = thisTrust;
				}
			}
		}
		return minTrust;
	}

	private IMetaAttribute changePredictionTime(IMetaAttribute metadata, PointInTime newPredictionStart,
			PointInTime newPredictionEnd) {
		if (metadata instanceof IPredictionTimes) {
			// Use the calculated start and end timestamps for the PredictionTime
			IPredictionTimes predictionTime = (IPredictionTimes) metadata;
			predictionTime.clear();
			IPredictionTime newTime = new PredictionTime();

			PointInTime predictionStart = convertToPredictionTime(newPredictionStart);
			PointInTime predictionEnd = convertToPredictionTime(newPredictionEnd);

			newTime.setPredictionStartAndEnd(predictionStart, predictionEnd);
			predictionTime.addPredictionTime(newTime);
		}
		return metadata;
	}

	/**
	 * Converts the timestamp to be for a different time unit, e.g., from
	 * milliseconds to seconds.
	 * 
	 * @param streamTime The timestamp in the steam time
	 * @return The timestamp in the prediction time
	 */
	private PointInTime convertToPredictionTime(PointInTime streamTime) {
		if (predictionBaseTimeUnit == null) {
			return streamTime;
		}

		long convertedPredictionTime = predictionBaseTimeUnit.convert(streamTime.getMainPoint(), streamBaseTimeUnit);
		PointInTime newPredictionTime = new PointInTime(convertedPredictionTime);
		return newPredictionTime;
	}
}
