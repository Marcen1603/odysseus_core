package de.uniol.inf.is.odysseus.temporaltypes.aggregationfunctions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.GenericTemporalType;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;

public class TemporalIncrementalAggregationFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 7093178271016785160L;

	protected AbstractIncrementalAggregationFunction<M, T> nonTemporalFunction;
	protected Map<PointInTime, AbstractIncrementalAggregationFunction<M, T>> nonTemporalFunctions;

	public TemporalIncrementalAggregationFunction(AbstractIncrementalAggregationFunction<M, T> nonTemporalFunction) {
		super();
		this.nonTemporalFunction = nonTemporalFunction;
		this.nonTemporalFunctions = new HashMap<>();
	}

	@Override
	public void addNew(T newElement) {
		addToAllValidTimes(newElement);
	}

	@SuppressWarnings("unchecked")
	private void addToAllValidTimes(T newElement) {
		if (newElement.getMetadata() instanceof IValidTimes) {
			IValidTimes validTimes = (IValidTimes) newElement.getMetadata();
			for (IValidTime validTime : validTimes.getValidTimes()) {
				for (PointInTime time = validTime.getValidStart(); time
						.before(validTime.getValidEnd()); time = time.plus(1)) {
					// Use the correct aggregation function
					Tuple<M> nonTemporalElement = createNonTemporalElement(newElement, time);
					this.getAggregationFunction(time).addNew((T) nonTemporalElement);
				}
			}
		}
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

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		this.nonTemporalFunctions.values().stream()
				.forEach(f -> f.removeOutdated(outdatedElements, trigger, pointInTime));
	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		GenericTemporalType<Object[]> result = new GenericTemporalType<>();
		for (PointInTime validTime : this.nonTemporalFunctions.keySet()) {
			result.setValue(validTime, this.nonTemporalFunctions.get(validTime).evalute(trigger, pointInTime));
		}
		Object[] returnValue = new Object[1];
		returnValue[0] = result;
		return returnValue;
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
