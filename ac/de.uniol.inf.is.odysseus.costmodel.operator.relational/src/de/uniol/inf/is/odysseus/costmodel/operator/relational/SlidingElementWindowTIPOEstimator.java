package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

@SuppressWarnings("rawtypes")
public class SlidingElementWindowTIPOEstimator implements IOperatorEstimator<SlidingElementWindowTIPO> {

	@Override
	public Class<SlidingElementWindowTIPO> getOperatorClass() {
		return SlidingElementWindowTIPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(SlidingElementWindowTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation estimation = new OperatorEstimation(instance);
		OperatorEstimation lastOpEstimation = prevOperators.get(0);
		
		/** 1. Histograms **/
		estimation.setHistograms(lastOpEstimation.getHistograms());

		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);

		/** 3. Datarate **/
		long windowSize = instance.getWindowSize();
		long windowAdvance = instance.getWindowAdvance();
		
		double r = lastOpEstimation.getDataStream().getDataRate();
		double g = 0;
		if( windowAdvance == 1 ) // elementbasiert gleitend
			g = windowSize / r;
		else // elementbasiert springend
			g = windowSize / (2.0 * r);
		
		DataStream stream = new DataStream(instance, r, g);
		estimation.setDataStream(stream);

		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getAvgCPUTimeMetadata(instance);
//		System.out.format("%8.6f\n", cpu);
		double cpuCost = 0.0;
		if (cpu < 0.0)
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * r;
		else {
			cpuCost = cpu * r;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		double memCost = EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * windowSize;
		
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));
		
		return estimation;
	}

}
