package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class LastNSampling implements ISampling {

	private int dataCount;
	private List<Double> values = new LinkedList<Double>();
	
	public LastNSampling( int n ) {
		dataCount = n;
	}
	
	@Override
	public void addValue(double value) {
		values.add(value);
		
		// remove oldest
		if( values.size() > dataCount )
			values.remove(0);
	}

	@Override
	public List<Double> getSampledValues() {
		Collections.sort(values);
		return values;
	}

	@Override
	public int getSampleSize() {
		return dataCount;
	}

}
