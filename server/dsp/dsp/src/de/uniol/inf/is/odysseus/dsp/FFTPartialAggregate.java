package de.uniol.inf.is.odysseus.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class FFTPartialAggregate<T> extends AbstractPartialAggregate<T> {

	private static final long serialVersionUID = 6847721673109069345L;

	private final List<Double> values = new ArrayList<>();
	
	public FFTPartialAggregate(final double value) {
		values.add(value);
	}

	public FFTPartialAggregate(final FFTPartialAggregate<T> fftPartialAggregate) {
		values.addAll(fftPartialAggregate.values);
	}

	@Override
	public IPartialAggregate<T> clone() {
		return new FFTPartialAggregate<T>(this);
	}

	public void add(final double value) {
		values.add(value);
	}
	
	public List<Double> evaluate() {
		FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] transform = fastFourierTransformer.transform(values.stream().mapToDouble(Double::doubleValue).toArray(), TransformType.FORWARD);
		
		return Arrays.asList(transform).stream().map(Complex::abs).collect(Collectors.toList());
	}

}
