package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

// simple sampling
public abstract class AbstractSamplingHistogramFactory implements IHistogramFactory {

	private ISampling sampling;
	private SDFAttribute attribute;

	public AbstractSamplingHistogramFactory(SDFAttribute attribute, ISampling sampling) {
		this.attribute = attribute;
		this.sampling = sampling;
	}

	@Override
	public synchronized void addValue(double value) {
		sampling.addValue(value);
	}

	public List<Double> getSampledValues() {
		return sampling.getSampledValues();
	}
	
	public SDFAttribute getAttribute() {
		return attribute;
	}
}
