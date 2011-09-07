package de.uniol.inf.is.odysseus.costmodel.operator.relational;

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
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingTimeWindowTIPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class SlidingTimeWindowTIPOEstimator implements IOperatorEstimator<SlidingTimeWindowTIPO> {
	@Override
	public Class<SlidingTimeWindowTIPO> getOperatorClass() {
		return SlidingTimeWindowTIPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(SlidingTimeWindowTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation estimation = new OperatorEstimation(instance);
		OperatorEstimation lastOpEstimation = prevOperators.get(0);
		
		/** 1. Histograms **/
		estimation.setHistograms(lastOpEstimation.getHistograms());

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		long windowSize = instance.getWindowSize() / 1000;
		long windowAdvance = instance.getWindowAdvance();
			
		double r = lastOpEstimation.getDataStream().getDataRate();
		double g = 0;
		if( windowAdvance == 1 ) // gleitend zeitbasiert
			g = windowSize;
		else  // springend zeitbasiert
			g = windowSize / 2.0;
		
		DataStream stream = new DataStream(instance, r, g);
		estimation.setDataStream(stream);

		/** 4. DetailCost **/
		// TODO: Memorycost
		// TODO: Memorycost
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
//		System.out.format("%8.6f\n", cpu);
		double cpuCost = 0.0;
		if (cpu < 0.0)
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * r;
		else {
			cpuCost = cpu * r;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));
		
		return estimation;
	}
}
