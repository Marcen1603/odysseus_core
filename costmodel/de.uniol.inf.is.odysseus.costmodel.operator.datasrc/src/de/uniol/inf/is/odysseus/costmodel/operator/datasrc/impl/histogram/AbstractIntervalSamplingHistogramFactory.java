package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public abstract class AbstractIntervalSamplingHistogramFactory extends AbstractSamplingHistogramFactory {

	private IIntervalCountEstimator intervalCountEstimator;
	
	public AbstractIntervalSamplingHistogramFactory(SDFAttribute attribute, ISampling sampling, IIntervalCountEstimator estimator) {
		super(attribute, sampling);
		
		this.intervalCountEstimator = estimator;
	}

	@Override
	public final IHistogram create() {
		List<Double> values = getSampledValues();
		if( values == null || values.isEmpty() ) 
			return null;
		
		int count = intervalCountEstimator.estimateIntervalCount(getSampledValues());
		return createWithIntervalCount(count);
	}

	protected abstract IHistogram createWithIntervalCount( int count );
}
