package de.uniol.inf.is.odysseus.costmodel.physical.estimate;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.costmodel.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.physical.StandardPhysicalOperatorEstimator;
import de.uniol.inf.is.odysseus.server.intervalapproach.StreamGroupingWithAggregationPO;

@SuppressWarnings("rawtypes")
public class StreamGroupingWithAggregationPOEstimator extends StandardPhysicalOperatorEstimator<StreamGroupingWithAggregationPO> {

	private static final double CPU_PER_TUPLE = 0.00000001;
	@Override
	protected Class<? extends StreamGroupingWithAggregationPO> getOperatorClass() {
		return StreamGroupingWithAggregationPO.class;
	}
	
	@Override
	public double getMemory() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();

		double expectedDataCount = ( firstOperator.getDatarate() / 1000 ) * firstOperator.getWindowSize();
		return expectedDataCount * EstimatorHelper.sizeInBytes(getOperator().getInputSchema());
	}
	
	@Override
	public double getCpu() {
		Iterator<DetailCost> operatorIterator = getPrevCostMap().values().iterator();
		DetailCost firstOperator = operatorIterator.next();

		double expectedDataCount = ( firstOperator.getDatarate() / 1000 ) * firstOperator.getWindowSize();
		return expectedDataCount * CPU_PER_TUPLE;
	}
}
