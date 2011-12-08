package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

public class SturgesRule implements IIntervalCountEstimator {

	@Override
	public int estimateIntervalCount(List<Double> data) {
		return (int)Math.floor( 1 + log2(data.size()));
	}

	private static double log2( double x ) {
		return Math.log(x) / Math.log(2.0);
	}
}
