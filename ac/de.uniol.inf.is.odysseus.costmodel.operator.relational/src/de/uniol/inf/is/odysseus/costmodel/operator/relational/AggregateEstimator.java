package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class AggregateEstimator implements IOperatorEstimator<StreamGroupingWithAggregationPO> {

	@Override
	public Class<StreamGroupingWithAggregationPO> getOperatorClass() {
		return StreamGroupingWithAggregationPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(StreamGroupingWithAggregationPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation estimation = new OperatorEstimation(instance);
		
		/** 1. Histograms **/
		// TODO
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
		for( OperatorEstimation prevOp : prevOperators) {
			histograms.putAll(prevOp.getHistograms());
		}
		estimation.setHistograms(histograms);
		
		/** 2. Selectivity **/
		// TODO
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		estimation.setSelectivity(selectivity >= 0.0 ? selectivity : 1.0 );
		
		/** 3. Datastream **/
		// TODO
		IDataStream prevStream = prevOperators.get(0).getDataStream();
		double datarate = EstimatorHelper.getDatarateMetadata(instance);
		if( datarate < 0.0 ) {
			datarate = prevStream.getDataRate();
		}
		estimation.setDataStream(new DataStream(instance, datarate, prevStream.getIntervalLength()));		
		
		/** 4. DetailCost **/
		// TODO: Mem
		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
//		System.out.format("%-8.6f\n", cpu);
		double cpuCost = 0.0;
		if( cpu < 0.0 )
			cpuCost = OperatorCostModelCfg.getInstance().getStandardCpuCost() * datarate;
		else
			cpuCost = cpu * datarate;
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));
		
		return estimation;	
	}

}
