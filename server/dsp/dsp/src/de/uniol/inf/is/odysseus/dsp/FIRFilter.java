package de.uniol.inf.is.odysseus.dsp;

import java.util.Collection;
import java.util.Collections;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class FIRFilter<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = -4090136616976263206L;

	// Simple Low pass filter
	private static final double[] h = new double[] { -0.008835451, 0.074140008, 0.506826540, 0.506826540, 0.074140008,
			-0.008835451 };

	public FIRFilter(int[] attributes) {
		super(attributes, new String[] { "value" });
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {

		final double[] inputs = elements.stream().mapToDouble(e -> (Double) getAttributes(e)[0]).toArray();
		
		double sum = 0;
		
		if (inputs.length < h.length) {
			return new Object[] { sum };
		}
		
		for (int i = 0; i < h.length; i++) {
			sum += h[i] * inputs[h.length - 1 - i];
		}

		return new Object[] { sum };
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0], SDFDatatype.DOUBLE));
	}

}
