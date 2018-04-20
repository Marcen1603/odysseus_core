package de.uniol.inf.is.odysseus.dsp;

import java.util.Arrays;
import java.util.Collection;
import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class Const<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = 4701984246348134053L;

	public Const() {
		super(null, new String[] { "v1", "v3" });
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		
//		FastFourierTransformer fastFourierTransformer = new FastFourierTransformer(DftNormalization.STANDARD);
//		Complex[] transform = fastFourierTransformer.transform(values.stream().mapToDouble(Double::doubleValue).toArray(), TransformType.FORWARD);
//		
//		return Arrays.asList(transform).stream().map(Complex::abs).collect(Collectors.toList());
		
		return new Integer[] { 42, 14 };
	}

	@Override
	public boolean needsOrderedElements() {
		return true;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return Arrays.asList(new SDFAttribute(null, outputAttributeNames[0], SDFDatatype.INTEGER), new SDFAttribute(null, outputAttributeNames[1], SDFDatatype.INTEGER));
	}

}
