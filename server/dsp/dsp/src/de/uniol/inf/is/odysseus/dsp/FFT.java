package de.uniol.inf.is.odysseus.dsp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class FFT<M extends ITimeInterval, T extends Tuple<M>> extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = 4701984246348134053L;

	public FFT(final int[] attributes) {
		super(attributes, new String[] { "k", "X" });
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		final double[] values = elements.stream().mapToDouble(e -> (Double) getAttributes(e)[0]).toArray();

		final FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
		final Complex[] transform = fastFourierTransformer.transform(values, TransformType.FORWARD);

		final List<Double> amplitudes = Arrays.stream(transform).map(Complex::abs).collect(Collectors.toList());
		final List<Integer> index = IntStream.range(0, elements.size()).boxed().collect(Collectors.toList());
		
		return new Object[] { index, amplitudes };
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Arrays.asList(new SDFAttribute(null, outputAttributeNames[0], SDFDatatype.LIST_INTEGER),
				new SDFAttribute(null, outputAttributeNames[1], SDFDatatype.LIST_DOUBLE));
	}

}
