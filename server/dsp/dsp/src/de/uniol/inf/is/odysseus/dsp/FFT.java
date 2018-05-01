package de.uniol.inf.is.odysseus.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.complexnumber.ComplexNumber;
import de.uniol.inf.is.odysseus.complexnumber.SDFComplexNumberDatatype;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public class FFT<M extends ITimeInterval, T extends Tuple<M>> extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = 4701984246348134053L;

	private static final FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(
			DftNormalization.STANDARD);

	public FFT(final int[] attributes, final String[] outputAttributeNames) {
		super(attributes, outputAttributeNames);
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		final List<Object> result = new ArrayList<>();

		for (final int inputAttributeIndex : inputAttributeIndices) {
			final double[] values = elements.stream()
					.mapToDouble(element -> (Double) getAttributes(element)[inputAttributeIndex]).toArray();

			result.add(evaluate(values));
		}

		return result.stream().toArray();
	}

	private List<ComplexNumber> evaluate(final double[] values) {

		final Complex[] transform = fastFourierTransform(values);

		return Arrays.stream(transform).map(complex -> new ComplexNumber(complex.getReal(), complex.getImaginary()))
				.collect(Collectors.toList());
	}

	private Complex[] fastFourierTransform(final double[] values) {
		return fastFourierTransformer.transform(values, TransformType.FORWARD);
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> outputAttributes = new ArrayList<>();
		for (final String outputAttributeName : outputAttributeNames) {
			outputAttributes
					.add(new SDFAttribute(null, outputAttributeName, SDFComplexNumberDatatype.LIST_COMPLEX_NUMBER));
		}
		return outputAttributes;
	}

}
