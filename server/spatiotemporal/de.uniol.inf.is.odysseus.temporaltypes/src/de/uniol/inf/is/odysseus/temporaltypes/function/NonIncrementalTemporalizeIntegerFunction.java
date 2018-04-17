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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.LinearIntegerFunction;
import de.uniol.inf.is.odysseus.temporaltypes.types.integer.TemporalInteger;

/**
 * Function for the aggregation operator to temporalize an integer attribute.
 * Makes a TemporalInteger from an integer.
 * 
 * Remark: Uses the non-incremental interface to deal with the second metadata
 * (ValidTimes) correctly.
 * 
 * @author Tobias Brandt
 *
 * @param <M>
 * @param <T>
 */
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
	
	public NonIncrementalTemporalizeIntegerFunction(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		this.temporalInteger = new TemporalInteger[inputAttributesLength];
		// TODO Fill values?
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public NonIncrementalTemporalizeIntegerFunction(NonIncrementalTemporalizeIntegerFunction<M, T> other) {
		super(other);
		this.temporalInteger = new TemporalInteger[other.temporalInteger.length];
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		// TODO What if there is more than one attribute? Handle each one independently?

		// Get the value from the newest element (the trigger)
		Object[] attributes = this.getAttributes(trigger);
		Object value = attributes[0];
		int newestValue = 0;
		if (value instanceof Integer) {
			newestValue = (Integer) value;
		} else {
			throw new ClassCastException("Cannot use any other attribute type than integer.");
		}

		// Get the value from the oldest element (the first from the sorted collection)
		int oldestValue = 0;
		PointInTime oldestStartTime = null;;
		if (!elements.iterator().hasNext()) {
			// We only have the trigger element, use zero slope
			oldestValue = newestValue;
			oldestStartTime = trigger.getMetadata().getStart();
		} else {
			T firstElement = elements.iterator().next();
			oldestStartTime = firstElement.getMetadata().getStart();
			Object[] attributesFromFirst = this.getAttributes(firstElement);
			Object valueFromFirst = attributesFromFirst[0];
			if (valueFromFirst instanceof Integer) {
				oldestValue = (Integer) valueFromFirst;
			} else {
				throw new ClassCastException("Cannot use any other attribute type than integer.");
			}
		}

		// Calculate slope
		// m = y / x = (|value_new - value_old| / |time_new - time_old|)
		double m = 0;
		long temporalDistance = Math.abs(trigger.getMetadata().getStart().minus(oldestStartTime).getMainPoint());
		if (temporalDistance != 0) {
			// Oldest and newest element are not valid at the same time -> we have a slope
			m = (Math.abs(newestValue - oldestValue)) / temporalDistance;			
		}
		
		// Calculate y axis intercept
		double b = newestValue - m * trigger.getMetadata().getStart().getMainPoint();

		// Create a linear function with the calculated values
		TemporalFunction<Integer> function = new LinearIntegerFunction(m, b);
		this.temporalInteger[0] = new TemporalInteger(function);
		return this.temporalInteger;
	}

	@Override
	public boolean needsOrderedElements() {
		// We need to get the first element in the SweepArea to calculate the slope
		return true;
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
			return new NonIncrementalTemporalizeIntegerFunction<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}

		return new NonIncrementalTemporalizeIntegerFunction<>(attributes, outputNames);
	}

	@Override
	public AbstractNonIncrementalAggregationFunction<M, T> clone() {
		return new NonIncrementalTemporalizeIntegerFunction<>(this);
	}

}
