package de.uniol.inf.is.odysseus.temporaltypes.aggregationfunctions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.temporaltypes.merge.ValidTimesMetadataUnionMergeFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.ValidTimes;
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
	protected Map<T, List<Tuple<M>>> nonTemporalElements;
	/* To track the valid time */
	protected List<T> validElements;
	protected ValidTimesMetadataUnionMergeFunction mergeFunction;

	public TemporalIncrementalAggregationFunction(AbstractIncrementalAggregationFunction<M, T> nonTemporalFunction) {
		super();
		this.nonTemporalFunction = nonTemporalFunction;
		this.nonTemporalFunctions = new HashMap<>();
		this.nonTemporalElements = new HashMap<>();
		this.validElements = new ArrayList<>();
		this.mergeFunction = new ValidTimesMetadataUnionMergeFunction();
	}

	@Override
	public void addNew(T newElement) {
		addToAllValidTimes(newElement);
		this.validElements.add(newElement);
	}

	@SuppressWarnings("unchecked")
	private void addToAllValidTimes(T newElement) {
		List<Tuple<M>> nonTemporal = new ArrayList<>();
		if (newElement.getMetadata() instanceof IValidTimes) {
			IValidTimes validTimes = (IValidTimes) newElement.getMetadata();
			for (IValidTime validTime : validTimes.getValidTimes()) {
				for (PointInTime time = validTime.getValidStart(); time
						.before(validTime.getValidEnd()); time = time.plus(1)) {
					// Use the correct aggregation function
					Tuple<M> nonTemporalElement = createNonTemporalElement(newElement, time);
					nonTemporal.add(nonTemporalElement);
					this.getAggregationFunction(time).addNew((T) nonTemporalElement);
				}
			}
		}
		this.nonTemporalElements.put(newElement, nonTemporal);
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
			this.nonTemporalFunctions.values().stream().forEach(f -> f
					.removeOutdated((List<T>) this.nonTemporalElements.get(outdatedElement), trigger, pointInTime));

			// Also remove from this temporary storage
			this.nonTemporalElements.remove(outdatedElement);

			this.validElements.remove(outdatedElement);
		}

		IValidTimes validTimes = calculateValidTimes();
		removeOldFunctions(validTimes);
	}

	/**
	 * Uses the valid stream elements to calculate the valid time
	 */
	private IValidTimes calculateValidTimes() {
		IValidTimes validTimes = new ValidTimes();

		for (T element : this.validElements) {
			if (element.getMetadata() instanceof IValidTimes) {
				IValidTimes currentValidTimes = (IValidTimes) element.getMetadata();
				this.mergeFunction.mergeInto(validTimes, currentValidTimes, new ValidTimes());
			}
		}
		return validTimes;
	}

	/**
	 * Remove the functions that are no longer needed because they are for a
	 * ValidTime that is not needed any longer
	 */
	private void removeOldFunctions(IValidTimes validTimes) {
		List<PointInTime> toRemove = new ArrayList<>();
		for (PointInTime key : this.nonTemporalFunctions.keySet()) {
			if (!validTimes.includes(key)) {
				toRemove.add(key);
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
			result.setValue(validTime, this.nonTemporalFunctions.get(validTime).evalute(trigger, pointInTime));
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
		return new TemporalIncrementalAggregationFunction<>(this.nonTemporalFunction.clone());
	}

	@Override
	public int[] getOutputAttributeIndices() {
		return this.nonTemporalFunction.getOutputAttributeIndices();
	}

}
