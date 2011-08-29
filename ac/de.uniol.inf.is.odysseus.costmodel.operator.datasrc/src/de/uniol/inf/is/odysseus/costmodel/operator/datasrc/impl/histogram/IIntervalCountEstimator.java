package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.impl.histogram;

import java.util.List;

public interface IIntervalCountEstimator {

	public int estimateIntervalCount( List<Double> data );
}
