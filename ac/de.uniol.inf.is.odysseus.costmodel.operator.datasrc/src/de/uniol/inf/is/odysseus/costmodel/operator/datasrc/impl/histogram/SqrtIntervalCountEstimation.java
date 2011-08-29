package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

public class SqrtIntervalCountEstimation implements IIntervalCountEstimator {

	@Override
	public int estimateIntervalCount(List<Double> data) {
		int result = (int)Math.sqrt(data.size());
		if( result < 1 )
			result = 1;
		return result;
	}

}
