package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.Collection;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

public interface IHistogramFactory {

	public void addValue( double value );
	public Collection<Double> getValues();
	
	public IHistogram create();
}
