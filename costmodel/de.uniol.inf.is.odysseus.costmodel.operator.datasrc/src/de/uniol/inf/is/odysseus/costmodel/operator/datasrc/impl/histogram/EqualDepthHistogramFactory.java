package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public class EqualDepthHistogramFactory extends AbstractIntervalSamplingHistogramFactory {


	public EqualDepthHistogramFactory(SDFAttribute attribute, ISampling sampling, IIntervalCountEstimator estimator) {
		super(attribute, sampling, estimator);
	}

	@Override
	public IHistogram createWithIntervalCount( int binNum ) {
		List<Double> values = getSampledValues();
		int valueCount = values.size();
		
		if (binNum < 1)
			binNum = 1;
		
		double depth = (double)valueCount / (double)binNum;
		double[] borders = new double[binNum + 1];
		double[] counts = new double[binNum]; 
		
		// assume that values are sorted
		int actBin = 0;
		int actBorder = 1;
		borders[0] = values.get(0);
		borders[borders.length - 1] = values.get(values.size() - 1 );
		
		double overhead = 0.0;
		for( int i = 0; i < valueCount; i++ ) {
			counts[actBin]++;
			if( counts[actBin] > depth - overhead) {
				overhead += ((double)counts[actBin] - depth);
				
				borders[actBorder] = values.get(i);
				actBorder++;
				actBin++;
			}
		}
		
		return new EqualDepthHistogram(getAttribute(), borders, counts);
	}

	@Override
	public Collection<Double> getValues() {
		return Collections.unmodifiableCollection(getSampledValues());
	}

}
