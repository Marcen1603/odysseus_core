package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;

/**
 * A generic class for temporal types. As it is not possible to create a nice
 * function for every temporal type, the values are stored in a map for each
 * (necessary) point in time (from the valid time).
 * 
 * @author Tobias Brandt
 *
 * @param <T>
 */
public class GenericTemporalType<T> implements IClone, Cloneable, Serializable, TemporalType<T> {

	private static final long serialVersionUID = -903000410292576845L;
	private SortedMap<PointInTime, T> values;

	private TemporalType<Double> trustFunction;

	public GenericTemporalType() {
		this.values = new TreeMap<>();
	}

	public GenericTemporalType(TemporalType<Double> trustFunction) {
		this.values = new TreeMap<>();
		this.trustFunction = trustFunction;
	}

	public GenericTemporalType(GenericTemporalType<T> other) {
		this.values = other.copyMap();
		this.trustFunction = other.trustFunction;
	}

	/**
	 * Set a value for a specific point in time.
	 * 
	 * @param time  The time at which the value is valid
	 * @param value The value
	 */
	public void setValue(PointInTime time, T value) {
		this.values.put(time, value);
	}

	@Override
	public T getValue(PointInTime time) {
		return this.values.get(time);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] getValues(TimeInterval interval) {

		PointInTime temporalDuration = interval.getEnd().minus(interval.getStart());
		if (temporalDuration.getMainPoint() > Integer.MAX_VALUE) {
			/*
			 * In case that we have more than max integer values we have a problem because
			 * calculation would take forever
			 */
			return null;
		}

		int duration = (int) (temporalDuration.getMainPoint());
		Object[] returnObject = new Object[duration];

		// Use a block to have the additional counter locally limited
		{
			int counter = 0;
			// Loop through all requested points in time
			for (PointInTime i = interval.getStart(); i.before(interval.getEnd()); i = i.plus(1)) {
				// Fill the result with all the necessary values
				returnObject[counter] = this.getValue(i);
				counter++;
			}
		}

		/*
		 * The suppress warning is necessary because it is not possible to create
		 * generic arrays.
		 */
		return (T[]) returnObject;
	}

	@Override
	public double getTrust(PointInTime time) {
		if (trustFunction == null) {
			return 1;
		}
		return trustFunction.getTrust(time);
	}

	/**
	 * Sets the trust function for this temporal attribute. For example, when the
	 * trust changes.
	 * 
	 * @param trustFunction The new trust function
	 */
	public void setTrustFunction(TemporalType<Double> trustFunction) {
		this.trustFunction = trustFunction;
	}

	/**
	 * To get all values from this temporal type. This is not a copy, so don't
	 * change the values if you don't want the original values to be changed.
	 * 
	 * @return The map which stores the values.
	 */
	public SortedMap<PointInTime, T> getValues() {
		return this.values;
	}

	public SortedMap<PointInTime, T> copyMap() {
		return new TreeMap<PointInTime, T>(this.values);
	}

	/**
	 * Removes all elements from the map which are not within the valid times
	 * 
	 * @param validTimes     The valid times which are needed. Others can be removed
	 * 
	 * @param streamTimeUnit The base time unit of the stream, which can differ from
	 *                       the base time unit of the prediction
	 */
	public void trim(IPredictionTimes validTimes, TimeUnit streamTimeUnit) {

		List<PointInTime> toRemove = new ArrayList<>();
		TimeUnit predictionTimeUnit = validTimes.getPredictionTimeUnit();
		if (predictionTimeUnit == null) {
			predictionTimeUnit = streamTimeUnit;
		}

		for (PointInTime asStreamTime : this.values.keySet()) {

			/*
			 * The values in this object are stored as stream points in time, so we have to
			 * convert from the potentially different prediction time base time first
			 */
			long inPredictionTime = predictionTimeUnit.convert(asStreamTime.getMainPoint(), streamTimeUnit);
			PointInTime asPredictionTime = new PointInTime(inPredictionTime);

			if (!validTimes.includes(asPredictionTime)) {
				toRemove.add(asStreamTime);
			}
		}
		toRemove.stream().forEach(e -> this.values.remove(e));
	}

	@Override
	public GenericTemporalType<?> clone() {
		return new GenericTemporalType<>(this);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GenericTemporalType: ");

		boolean first = true;
		for (PointInTime time : this.values.keySet()) {
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			builder.append(time + " = ");
			Object value = this.values.get(time);
			builder.append(objectToString(value));
		}

		return builder.toString();
	}

	private String objectToString(Object value) {
		if (value instanceof Object[]) {
			Object[] values = (Object[]) value;
			return Arrays.deepToString(values);
		} else {
			return value.toString();
		}
	}
}
