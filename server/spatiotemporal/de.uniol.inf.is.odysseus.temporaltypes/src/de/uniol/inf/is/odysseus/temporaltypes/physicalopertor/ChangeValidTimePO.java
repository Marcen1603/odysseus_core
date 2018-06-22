package de.uniol.inf.is.odysseus.temporaltypes.physicalopertor;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangeValidTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ITemporalTrust;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;
import de.uniol.inf.is.odysseus.trust.ITrust;
import de.uniol.inf.is.odysseus.trust.Trust;

/**
 * This operator manipulates the ValidTimes metadata. It is doing this based on
 * the stream time interval. It uses the start timestamp of the stream time
 * interval (the "normal" time interval) of a stream element and adds /
 * substracts the given values to / from it to calculate the start and end
 * timestamp of the ValidTime.
 * 
 * Removes all previous information in the ValidTimes metadata.
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class ChangeValidTimePO<T extends IStreamObject<?>> extends AbstractPipe<T, T> {

	private boolean copyTimeInterval;
	private TimeValueItem valueToAddStart;
	private TimeValueItem valueToAddEnd;
	private boolean alingAtStreamEnd;
	private TimeUnit streamBaseTimeUnit;
	private TimeUnit predictionBaseTimeUnit;

	public ChangeValidTimePO(ChangeValidTimeAO ao) {
		this.valueToAddStart = ao.getValueToAddStart();
		this.valueToAddEnd = ao.getValueToAddEnd();
		this.alingAtStreamEnd = ao.isAlignAtEnd();
		this.streamBaseTimeUnit = ao.getBaseTimeUnit();
		this.copyTimeInterval = ao.isCopyTimeInterval();
		this.predictionBaseTimeUnit = ao.getBaseTimeUnit();
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

		IValidTimes validTimes = (IValidTimes) object.getMetadata();

		// The base time could be changed
		if (this.predictionBaseTimeUnit != null) {
			validTimes.setTimeUnit(predictionBaseTimeUnit);
		}

		// If the trust value is used, it needs to be set here
		if (object.getMetadata() instanceof IValidTimes) {
			changeTrust(object, validTimes);
		}

		transfer(object);
	}

	private T copyTimeInterval(T object) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
			// Use the stream time as the starting point for the calculations
			ITimeInterval streamTime = (ITimeInterval) metadata;
			changeValidTime(metadata, streamTime.getStart(), streamTime.getEnd());
		}
		return object;
	}

	private T setTimeInterval(T object, TimeValueItem addToStart, TimeValueItem addToEnd, boolean alignAtStreamEnd) {
		IMetaAttribute metadata = object.getMetadata();
		if (metadata instanceof ITimeInterval) {
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
			PointInTime newValidStart = baseTime.plus(convertedValueToAddStart);
			PointInTime newValidEnd = baseTime.plus(convertedValueToAddEnd);
			changeValidTime(metadata, newValidStart, newValidEnd);
		}
		return object;
	}

	private void changeTrust(T streamElement, IValidTimes validTimes) {
		IMetaAttribute metadata = streamElement.getMetadata();

		if (metadata instanceof ITemporalTrust && streamElement instanceof Tuple) {
			// Put the trusts in the meta attribute
			ITemporalTrust oldTempTrust = (ITemporalTrust) metadata;

			for (IValidTime validTime : validTimes.getValidTimes()) {
				double[] trusts = getTrusts((Tuple<?>) streamElement, validTime);
				insertTrusts(trusts, validTime, oldTempTrust);
			}
		}
	}

	private void insertTrusts(double[] trusts, IValidTime validTime, ITemporalTrust temporalTrust) {
		int i = 0;
		for (PointInTime time = validTime.getValidStart(); time.before(validTime.getValidEnd()); time = time.plus(1)) {
			ITrust trust = new Trust();
			trust.setTrust(trusts[i]);
			temporalTrust.setTrust(time, trust);
			i++;
		}
	}

	private double[] getTrusts(Tuple<?> tuple, IValidTime validTime) {

		int size = (int) validTime.getValidEnd().minus(validTime.getValidStart()).getMainPoint();
		double[] trusts = new double[size];

		int i = 0;
		for (PointInTime time = validTime.getValidStart(); time.before(validTime.getValidEnd()); time = time.plus(1)) {
			double trust = getTrust(tuple, time);
			trusts[i] = trust;
			i++;
		}

		return trusts;
	}

	private double getTrust(Tuple<?> tuple, PointInTime validTime) {
		// Use lowest trust of all temporal attributes
		double minTrust = 1;
		Object[] attributes = tuple.getAttributes();
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i] instanceof TemporalType) {
				TemporalType<?> temporalAttribute = (TemporalType<?>) attributes[i];
				double thisTrust = temporalAttribute.getTrust(validTime);
				if (thisTrust < minTrust) {
					minTrust = thisTrust;
				}
			}
		}
		return minTrust;
	}

	private IMetaAttribute changeValidTime(IMetaAttribute metadata, PointInTime newValidStart,
			PointInTime newValidEnd) {
		if (metadata instanceof IValidTimes) {
			// Use the calculated start and end timestamps for the ValidTime
			IValidTimes validTime = (IValidTimes) metadata;
			validTime.clear();
			IValidTime newTime = new ValidTime();
			
			PointInTime validStart = convertPointInTime(newValidStart);
			PointInTime validEnd = convertPointInTime(newValidEnd);
			
			newTime.setValidStartAndEnd(validStart, validEnd);
			validTime.addValidTime(newTime);
		}
		return metadata;
	}

	/**
	 * Converts the timestamp to be for a different time unit, e.g., from
	 * milliseconds to seconds.
	 * 
	 * @param streamTime
	 *            The timestamp in the steam time
	 * @return The timestamp in the prediction time
	 */
	private PointInTime convertPointInTime(PointInTime streamTime) {
		if (predictionBaseTimeUnit == null) {
			return streamTime;
		}

		long convertedValidTime = predictionBaseTimeUnit.convert(streamTime.getMainPoint(), streamBaseTimeUnit);
		PointInTime newValidTime = new PointInTime(convertedValidTime);
		return newValidTime;
	}
}
