package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public class EqualWidthHistogramFactory extends AbstractIntervalSamplingHistogramFactory {

	public EqualWidthHistogramFactory(SDFAttribute attribute, ISampling sampling, IIntervalCountEstimator estimator) {
		super(attribute, sampling, estimator);
	}

	@Override
	public synchronized IHistogram createWithIntervalCount( int intervalCount) {
		List<Double> values = getSampledValues();

		// determine intervallength
		double min = values.get(0);
		double max = values.get(values.size() - 1);
		double intervalSize = (max - min) / intervalCount;

		// determine counts of intervals
		double[] counts = new double[intervalCount];
		for (Double val : values) {
			int index = (int) ((val - min) / intervalSize);

			if (index >= counts.length) {
				counts[counts.length - 1]++;
			} else {
				counts[index]++;
			}
		}
		// create equal-width-histogram
		return new EqualWidthHistogram(getAttribute(), min, max, intervalSize, counts);
	}

	@Override
	public Collection<Double> getValues() {
		return Collections.unmodifiableCollection(getSampledValues());
	}
}
