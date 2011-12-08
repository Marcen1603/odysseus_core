package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

public interface ISampling {

	public void addValue( double value );
	public List<Double> getSampledValues();
	
	public int getSampleSize();
}
