package de.uniol.inf.is.odysseus.p2p.ac.estimator;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base.P2PSinkPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Operator-Schätzer für den physischen Operator {@link P2PSinkPO}.
 * 
 * @author Timo Michelsen
 *
 */
@SuppressWarnings("rawtypes")
public class P2PSinkEstimator implements IOperatorEstimator<P2PSinkPO> {

	@Override
	public Class<P2PSinkPO> getOperatorClass() {
		return P2PSinkPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(P2PSinkPO instance,
			List<OperatorEstimation> prevOperators,
			Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation estimation = new OperatorEstimation(instance);
		OperatorEstimation lastOpEstimation = prevOperators.get(0);

		/** 1. Histograms **/
		// project has only one input
		Map<SDFAttribute, IHistogram> inputHistograms = lastOpEstimation.getHistograms();
		estimation.setHistograms(inputHistograms);

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		// pure sink has no datarate
		double datarate = lastOpEstimation.getDataStream().getDataRate();
		estimation.setDataStream(new DataStream(instance, datarate, lastOpEstimation.getDataStream().getIntervalLength()));

		/** 4. DetailCost **/
		// TODO: Memorycost
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) 
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * datarate;
		else {
			cpuCost = cpu * datarate;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));
		
		return estimation;
	}

}
