package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ReceiverPO;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.DataStreamRateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;

@SuppressWarnings("rawtypes")
public class ByteBufferRecieverPOEstimator implements IOperatorEstimator<ReceiverPO> {

	@Override
	public Class<ReceiverPO> getOperatorClass() {
		return ReceiverPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(ReceiverPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation estimation = new OperatorEstimation(instance);
		
		// retrieve source name
		String sourceName = instance.getOutputSchema().get(0).getSourceName();
		
		/** 1. Histograms **/
		Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
		for( SDFAttribute attribute : instance.getOutputSchema() ) {
			if( baseHistograms.containsKey(attribute))
				histograms.put(attribute, baseHistograms.get(attribute));
		}
		estimation.setHistograms(histograms);
		
		/** 2. Selectivity **/
		estimation.setSelectivity(1.0);
		
		/** 3. DataStream **/
		double datarate = EstimatorHelper.getDatarateMetadata(instance);
		if( datarate < 0.0 ) {
			datarate = DataStreamRateSaver.getInstance().get(sourceName);
			if( Math.abs(datarate) < 0.000001 ) 
				datarate = 1.0; // default... since there are no datastream
		} else { 
			DataStreamRateSaver.getInstance().set(sourceName, datarate);
		}
		
		estimation.setDataStream(new DataStream(instance, datarate, 1.0));
		
		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
//		System.out.format("%-20.10f\n", cpu);
		double cpuCost = 0.0;
		if( cpu < 0.0 )
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * datarate;
		else {
			cpuCost = cpu * datarate;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));
		
		return estimation;
	}


}
