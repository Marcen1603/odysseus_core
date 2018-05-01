package de.uniol.inf.is.odysseus.dsp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class FIRFilter<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = -4090136616976263206L;

	private final double[] coefficients;

	public FIRFilter(final int[] attributes, final String[] outputAttributeNames, final double[] coefficients) {
		super(attributes, outputAttributeNames);
		this.coefficients = coefficients;
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		final List<Object> result = new ArrayList<>();
		for (final int inputAttributeIndex : inputAttributeIndices) {
			final double[] values = elements.stream()
					.mapToDouble(element -> (Double) getAttributes(element)[inputAttributeIndex]).toArray();
			result.add(filter(values));
		}
		return result.stream().toArray();
	}

	private double filter(final double[] inputs) {
		double sum = 0;

		if (inputs.length < coefficients.length) {
			return sum;
		}
		for (int i = 0; i < coefficients.length; i++) {
			sum += coefficients[i] * inputs[coefficients.length - 1 - i];
		}
		return sum;
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> outputAttributes = new ArrayList<>();
		for (final String outputAttributeName : outputAttributeNames) {
			outputAttributes.add(new SDFAttribute(null, outputAttributeName, SDFComplexNumberDatatype.DOUBLE));
		}
		return outputAttributes;
	}

}
