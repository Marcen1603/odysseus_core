package de.uniol.inf.is.odysseus.systemload.impl;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class NumberAverager {

	private final int maxCount;
	
	private final List<Double> values = Lists.newLinkedList();
	private double sum;
	
	public NumberAverager( int maxCount ) {
		Preconditions.checkArgument(maxCount > 0, "maxCount must be positive!");
		
		this.maxCount = maxCount;
	}
	
	public void addValue( double value ) {
		values.add(value);
		sum += value;
		
		if( values.size() > maxCount ) {
			double lastValue = values.remove(0);
			sum -= lastValue;
		}
	}
	
	public double getAverage() {
		if( values.size() == 0 ) {
			return 0.0;
		}
		return sum / values.size();
	}

	public double getCount() {
		return values.size();
	}
}
