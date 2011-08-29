package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.StandardOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class MetadataUpdateEstimator implements IOperatorEstimator<MetadataUpdatePO> {

	private StandardOperatorEstimator<MetadataUpdatePO> std = new StandardOperatorEstimator<MetadataUpdatePO>();
	
	@Override
	public Class<MetadataUpdatePO> getOperatorClass() {
		return MetadataUpdatePO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(MetadataUpdatePO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		// Use Standardestimator
		OperatorEstimation estimation = std.estimateOperator(instance, prevOperators, baseHistograms);
		
		IDataStream c = prevOperators.get(0).getDataStream();
		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if( cpu < 0.0 )
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * c.getDataRate();
		else {
			cpuCost = cpu * c.getDataRate();
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));
		
		return estimation;
	}

}
