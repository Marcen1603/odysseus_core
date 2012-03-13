package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

public class ScottsRule implements IIntervalCountEstimator {

	@Override
	public int estimateIntervalCount(List<Double> data) {

		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		
		// sum of all data
		double sum = 0.0;
		for (Double d : data) {
			if( d < min )
				min = d;
			if( d > max )
				max = d;
			
			sum += d;
		}
		
		// erwartungswert
		double e = sum / data.size();
		
		// variances
		double sum2 = 0.0;
		for( Double d : data ) {
			sum2 += (( d - e ) * (d - e));
		}
		
		// standard derivation
		double s = 0.0;
		if( data.size() > 1 ) {
			double sPow2 = (1.0 / (data.size() - 1.0)) * sum2;
			s = Math.sqrt(sPow2);
		} else {
			s = 1.0;
		}
		
		// result
		double powered = Math.pow(data.size(), -0.3);
		double intervalWidth = 3.5 * s * powered;
		
		
		
		return (int)( (max - min) / intervalWidth );
	}

}
