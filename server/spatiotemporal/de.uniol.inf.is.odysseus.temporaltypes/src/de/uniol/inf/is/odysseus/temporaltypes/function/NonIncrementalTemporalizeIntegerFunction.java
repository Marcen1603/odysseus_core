package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.temporaltypes.types.IntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.LinearIntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalInteger;

public class NonIncrementalTemporalizeIntegerFunction<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 8803557321939037142L;

	protected final TemporalInteger[] temporalInteger;

	public NonIncrementalTemporalizeIntegerFunction() {
		super();
		this.temporalInteger = new TemporalInteger[1];
	}

	public NonIncrementalTemporalizeIntegerFunction(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		this.temporalInteger = new TemporalInteger[attributes.length];
		// TODO Fill values?
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public NonIncrementalTemporalizeIntegerFunction(NonIncrementalTemporalizeIntegerFunction<M, T> other) {
		super(other);
		this.temporalInteger = new TemporalInteger[other.temporalInteger.length];
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		Object[] attributes = this.getAttributes(trigger);
		Object value = attributes[0];
		if (value instanceof Integer) {
			// TODO Use slope of values for function
			Integer valueAsInteger = (Integer) value;
			IntegerFunction function = new LinearIntegerFunction(0, valueAsInteger);
			this.temporalInteger[0] = new TemporalInteger(function);
			return this.temporalInteger;
		} else {
			throw new ClassCastException("Cannot use any other attribute type than integer.");
		}
	}

	@Override
	public boolean needsOrderedElements() {
		return false;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> result = new ArrayList<>(this.temporalInteger.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, TemporalDatatype.TEMPORAL_INTEGER, null, null, null));
		}

		return result;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final boolean checkInputOutputLength = AggregationFunctionParseOptionsHelper
				.checkInputAttributesLengthEqualsOutputAttributesLength(parameters, attributeResolver);
		final boolean checkNumericInput = AggregationFunctionParseOptionsHelper.checkNumericInput(parameters,
				attributeResolver);
		return checkInputOutputLength && checkNumericInput;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		if (attributes == null) {
			return new TemporalizeInteger<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new NonIncrementalTemporalizeIntegerFunction<>(attributes, outputNames);
	}

	@Override
	public AbstractNonIncrementalAggregationFunction<M, T> clone() {
		return new NonIncrementalTemporalizeIntegerFunction<>(this);
	}

}
