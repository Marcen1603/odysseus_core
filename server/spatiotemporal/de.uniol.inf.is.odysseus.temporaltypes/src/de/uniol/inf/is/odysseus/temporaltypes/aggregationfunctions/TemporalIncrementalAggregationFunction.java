package de.uniol.inf.is.odysseus.temporaltypes.aggregationfunctions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.temporaltypes.merge.PredictionTimesMetadataUnionMergeFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.PredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

/**
 * Makes it possible for an incremental aggregation function to work with
 * temporal attributes.
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 * @param <T>
 */
public class TemporalIncrementalAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 7093178271016785160L;

	protected AbstractIncrementalAggregationFunction<M, T> nonTemporalFunction;
	protected Map<PointInTime, AbstractIncrementalAggregationFunction<M, T>> nonTemporalFunctions;
	/* For each temporal element, multiple non-temporal elements can exist */
	protected Map<T, Map<PointInTime, Tuple<M>>> nonTemporalElements;
	/* To track the valid time */
	protected List<T> validElements;
	protected PredictionTimesMetadataUnionMergeFunction mergeFunction;

	private TimeUnit streamBaseTimeUnit;

	public TemporalIncrementalAggregationFunction(AbstractIncrementalAggregationFunction<M, T> nonTemporalFunction,
			TimeUnit streamBaseTimeUnit) {
		super();
		this.nonTemporalFunction = nonTemporalFunction;
		this.nonTemporalFunctions = new HashMap<>();
		this.nonTemporalElements = new HashMap<>();
		this.validElements = new ArrayList<>();
		this.mergeFunction = new PredictionTimesMetadataUnionMergeFunction();
		this.streamBaseTimeUnit = streamBaseTimeUnit;
	}

	@Override
	public void addNew(T newElement) {
		addToAllValidTimes(newElement);
		this.validElements.add(newElement);
	}

	@SuppressWarnings("unchecked")
	private void addToAllValidTimes(T newElement) {
		Map<PointInTime, Tuple<M>> nonTemporal = new HashMap<>();
		if (newElement.getMetadata() instanceof IPredictionTimes) {
			IPredictionTimes validTimes = (IPredictionTimes) newElement.getMetadata();
			for (IPredictionTime validTime : validTimes.getPredictionTimes()) {
				for (PointInTime asPredictionTime = validTime.getPredictionStart(); asPredictionTime
						.before(validTime.getPredictionEnd()); asPredictionTime = asPredictionTime.plus(1)) {
					// Use the correct aggregation function

					// Convert to stream time from prediction time base time
					PointInTime asStreamTime = convertToStreamTime(asPredictionTime, validTimes);

					Tuple<M> nonTemporalElement = createNonTemporalElement(newElement, asStreamTime);
					nonTemporal.put(asStreamTime, nonTemporalElement);
					this.getAggregationFunction(asStreamTime).addNew((T) nonTemporalElement);
				}
			}
		}
		this.nonTemporalElements.put(newElement, nonTemporal);
	}
	
	private PointInTime convertToStreamTime(PointInTime asPredictionTime, IPredictionTimes validTimes) {
		TimeUnit predictionTimeUnit = validTimes.getPredictionTimeUnit();
		if (predictionTimeUnit == null) {
			// There is no difference
			predictionTimeUnit = streamBaseTimeUnit;
		}
		
		// Convert to stream time from prediction time base time
		PointInTime asStreamTime = new PointInTime(
				streamBaseTimeUnit.convert(asPredictionTime.getMainPoint(), predictionTimeUnit));
		return asStreamTime;
		
	}

	private AbstractIncrementalAggregationFunction<M, T> getAggregationFunction(PointInTime time) {
		if (this.nonTemporalFunctions.get(time) == null) {
			this.nonTemporalFunctions.put(time, this.nonTemporalFunction.clone());
		}
		return this.nonTemporalFunctions.get(time);
	}

	@SuppressWarnings("rawtypes")
	private Tuple<M> createNonTemporalElement(T newElement, PointInTime validTime) {
		Tuple<M> copyElement = newElement.clone();
		for (int i = 0; i < copyElement.getAttributes().length; i++) {
			Object attribute = copyElement.getAttribute(i);
			if (attribute instanceof TemporalType) {
				TemporalType temporalAttribute = (TemporalType) attribute;
				Object value = temporalAttribute.getValue(validTime);
				copyElement.setAttribute(i, value);
			}
		}
		return copyElement;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		/*
		 * Remove the non-temporal elements, because we filled the functions with the
		 * non-temporal elements, not the temporal ones.
		 */
		for (T outdatedElement : outdatedElements) {
			Map<PointInTime, Tuple<M>> outdated = this.nonTemporalElements.get(outdatedElement);

			// Only remove from those function which are affected by this element
			IPredictionTimes validTimes = (IPredictionTimes) outdatedElement.getMetadata();
			for (IPredictionTime validTime : validTimes.getPredictionTimes()) {
				for (PointInTime asPredictionTime = validTime.getPredictionStart(); asPredictionTime
						.before(validTime.getPredictionEnd()); asPredictionTime = asPredictionTime.plus(1)) {
					// Convert to stream time from prediction time base time
					PointInTime asStreamTime = convertToStreamTime(asPredictionTime, validTimes);
					Tuple<M> singleOutdatedElement = outdated.get(asStreamTime);
					Collection<Tuple<M>> outdatedCollection = new ArrayList<>();
					outdatedCollection.add(singleOutdatedElement);
					getAggregationFunction(asStreamTime).removeOutdated((List<T>) outdatedCollection, trigger, pointInTime);
				}
			}

			// Also remove from this temporary storage
			this.nonTemporalElements.remove(outdatedElement);

			this.validElements.remove(outdatedElement);
		}

		IPredictionTimes validTimes = calculateValidTimes();
		removeOldFunctions(validTimes);
	}

	/**
	 * Uses the valid stream elements to calculate the valid time
	 */
	private IPredictionTimes calculateValidTimes() {
		IPredictionTimes validTimes = new PredictionTimes();

		for (T element : this.validElements) {
			if (element.getMetadata() instanceof IPredictionTimes) {
				IPredictionTimes currentValidTimes = (IPredictionTimes) element.getMetadata();
				this.mergeFunction.mergeInto(validTimes, currentValidTimes, new PredictionTimes());
			}
		}
		return validTimes;
	}

	/**
	 * Remove the functions that are no longer needed because they are for a
	 * ValidTime that is not needed any longer
	 */
	private void removeOldFunctions(IPredictionTimes validTimes) {
		List<PointInTime> toRemove = new ArrayList<>();
		for (PointInTime key : this.nonTemporalFunctions.keySet()) {
			
			// Convert to stream time from prediction time base time
			PointInTime asStreamTime = convertToStreamTime(key, validTimes);
			
			if (!validTimes.includes(asStreamTime)) {
				toRemove.add(asStreamTime);
			}
		}
		for (PointInTime remove : toRemove) {
			this.nonTemporalFunctions.remove(remove);
		}
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		GenericTemporalType<Object[]> result = new GenericTemporalType<>();
		for (PointInTime validTime : this.nonTemporalFunctions.keySet()) {
			Object[] evaluationResult = this.nonTemporalFunctions.get(validTime).evalute(trigger, pointInTime);
			Object[] clonedResult = evaluationResult.clone();
			result.setValue(validTime, clonedResult);
		}
		if (differsFromPrevious(result)) {
			Object[] returnValue = new Object[1];
			returnValue[0] = result;
			return returnValue;
		} else {
			Object[] returnValue = { null };
			return returnValue;
		}
	}

	/**
	 * Checks if the result contains more than only null values. This is the
	 * behavior from the AggregationPO, which also does not transfers empty
	 * (non-changed) results.
	 */
	private boolean differsFromPrevious(GenericTemporalType<Object[]> result) {
		for (Object[] array : result.getValues().values()) {
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					if (array[i] != null) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return this.nonTemporalFunction.getOutputAttributes();
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		if (this.nonTemporalFunction instanceof IAggregationFunctionFactory) {
			return ((IAggregationFunctionFactory) this.nonTemporalFunction).checkParameters(parameters,
					attributeResolver);
		}
		return false;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		if (this.nonTemporalFunction instanceof IAggregationFunctionFactory) {
			return ((IAggregationFunctionFactory) this.nonTemporalFunction).createInstance(parameters,
					attributeResolver);
		}
		return null;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new TemporalIncrementalAggregationFunction<>(this.nonTemporalFunction.clone(), this.streamBaseTimeUnit);
	}

	@Override
	public int[] getOutputAttributeIndices() {
		return this.nonTemporalFunction.getOutputAttributeIndices();
	}

}
