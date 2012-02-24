package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;

@SuppressWarnings("rawtypes")
public class RelationalProjectPOEstimator implements IOperatorEstimator<RelationalProjectPO> {

	@Override
	public Class<RelationalProjectPO> getOperatorClass() {
		return RelationalProjectPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(RelationalProjectPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation estimation = new OperatorEstimation(instance);
		OperatorEstimation lastOpEstimation = prevOperators.get(0);

		/** 1. Histograms **/
		// project has only one input
		Map<SDFAttribute, IHistogram> inputHistograms = lastOpEstimation.getHistograms();
		SDFSchema outputSchema = instance.getOutputSchema();

		// only provide those histograms, which
		// are projected from this operator
		Map<SDFAttribute, IHistogram> outputHistograms = new HashMap<SDFAttribute, IHistogram>();
		for (SDFAttribute attribute : outputSchema) {
			IHistogram histogram = inputHistograms.get(attribute);
			if (histogram != null)
				outputHistograms.put(attribute, histogram);
		}
		estimation.setHistograms(outputHistograms);

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		double datarate = EstimatorHelper.getDatarateMetadata(instance);
		if( datarate < 0.0 )
			datarate = lastOpEstimation.getDataStream().getDataRate();
		
		estimation.setDataStream(new DataStream(instance, datarate, lastOpEstimation.getDataStream().getIntervalLength()));

		/** 4. DetailCost **/
		// TODO: Memorycost
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) 
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * lastOpEstimation.getDataStream().getDataRate();
		else {
			cpuCost = cpu * lastOpEstimation.getDataStream().getDataRate();
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));

		return estimation;
	}

}
