package de.uniol.inf.is.odysseus.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractNonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class CrossCorrelation<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractNonIncrementalAggregationFunction<M, T> {

	private static final long serialVersionUID = -4967537349862834000L;

	public CrossCorrelation(final int[] attributes, final String[] outputAttributeNames) {
		super(attributes, outputAttributeNames);
	}

	@Override
	public Object[] evaluate(Collection<T> elements, T trigger, PointInTime pointInTime) {
		final double[] values1 = elements.stream().mapToDouble(e -> (Double) getAttributes(e)[0]).toArray();
		final double[] values2 = elements.stream().mapToDouble(e -> (Double) getAttributes(e)[1]).toArray();

		int count = values1.length;
		final List<Integer> timeShifts = new ArrayList<Integer>();
		final List<Double> correlations = new ArrayList<Double>();

		for (int m = -count + 1; m < count; m++) {
			double sum = 0;

			for (int n = 0; n < count; n++) {
				int shiftedIndex = n + m;
				double factor = shiftedIndex >= 0 && shiftedIndex < count ? values2[shiftedIndex] : 0;
				sum += values1[n] * factor;
			}

			timeShifts.add(m);
			correlations.add(sum);
		}

		return new Object[] { timeShifts, correlations };
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
