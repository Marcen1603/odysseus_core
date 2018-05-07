package de.uniol.inf.is.odysseus.temporaltypes.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.LinearIntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.TemporalInteger;

/**
 * Aggregation function to convert an integer to a TemporalInteger. Attention to
 * the second metadata. The AggregationPO may not be able to deal with secondary
 * metadata (ValidTime) in incremental mode. Use the non-incremental function
 * instead.
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 * @param <T>
 */
public class TemporalizeInteger<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T> implements IAggregationFunctionFactory {

	private static final long serialVersionUID = 6606817774193458664L;

	protected final TemporalInteger[] temporalInteger;

	public TemporalizeInteger() {
		super();
		this.temporalInteger = new TemporalInteger[1];
	}

	public TemporalizeInteger(final TemporalizeInteger<M, T> other) {
		super(other);
		this.temporalInteger = new TemporalInteger[other.temporalInteger.length];
		// TODO Fill values?
	}

	public TemporalizeInteger(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		this.temporalInteger = new TemporalInteger[attributes.length];
		// TODO Fill values?
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public TemporalizeInteger(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		this.temporalInteger = new TemporalInteger[inputAttributesLength];
		// TODO Fill values?
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	@Override
	public void addNew(T newElement) {
		final Object[] attr = getAttributes(newElement);
		if (attr[0] != null && attr[0] instanceof Integer) {
			int value = (int) (attr[0]);
			TemporalFunction<Integer> function = new LinearIntegerFunction(0, value);
			this.temporalInteger[0] = new TemporalInteger(function);
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {
		return this.temporalInteger;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> result = new ArrayList<>(this.temporalInteger.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, SDFDatatype.INTEGER, null, TemporalDatatype.getTemporalConstraint(), null));
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

		return new TemporalizeInteger<>(attributes, outputNames);
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new TemporalizeInteger<>(this);
	}

}
