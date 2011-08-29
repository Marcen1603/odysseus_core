package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class StandardOperatorEstimator<T extends IPhysicalOperator> implements IOperatorEstimator<T> {

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getOperatorClass() {
		return (Class<T>) IPhysicalOperator.class;
	}

	@Override
	public OperatorEstimation estimateOperator(T instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		// Standard-bevavior
		OperatorEstimation estimation = new OperatorEstimation(instance);
		
		// sink?
		if( instance instanceof ISink) {
			// assume, that we have only one input-operator
			OperatorEstimation prevOperatorEstimation = prevOperators.get(0);
	
			// Histograms
			estimation.setHistograms(prevOperatorEstimation.getHistograms());
	
			// Selectivity
			double selectivity = getSelectivityMetadata(instance);
			if (selectivity < 0.0)
				selectivity = 1.0;
			estimation.setSelectivity(selectivity);
	
			// DataStream
			double datarate = getDatarateMetadata(instance);
			if (datarate < 0.0)
				datarate = prevOperatorEstimation.getDataStream().getDataRate() * selectivity;
			double intervalLength = prevOperatorEstimation.getDataStream().getIntervalLength();
			estimation.setDataStream(new DataStream(instance, datarate, intervalLength));
	
			// OperatorDetailCost
			double cpu = getAvgCPUTimeMetadata(instance) * prevOperatorEstimation.getDataStream().getDataRate();
			if (cpu < 0.0)
				cpu = OperatorCostModelCfg.getInstance().getStandardCpuCost() * prevOperatorEstimation.getDataStream().getDataRate();
			double mem = OperatorCostModelCfg.getInstance().getStandardMemCost();
	
			estimation.setDetailCost(new OperatorDetailCost(instance, mem, cpu));
		} else {
			// its a source!		
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
			double datarate = getDatarateMetadata(instance);
			if( datarate < 0.0 ) {
				datarate = 1.0; // default... since there are no datastream
			} 
			
			estimation.setDataStream(new DataStream(instance, datarate, 1.0));
			
			/** 4. DetailCost **/
			double cpu = getAvgCPUTimeMetadata(instance);
//			System.out.format("%-20.10f\n", cpu);
			double cpuCost = 0.0;
			if( cpu < 0.0 )
				cpuCost = OperatorCostModelCfg.getInstance().getStandardCpuCost() * datarate;
			else {
				cpuCost = cpu * datarate;
			}
			
			estimation.setDetailCost(new OperatorDetailCost(instance, OperatorCostModelCfg.getInstance().getStandardMemCost(), cpuCost));		
		}
		
		return estimation;
	}

	public static double getDatarateMetadata(IPhysicalOperator operator) {
		double datarate = -1;
		try {
			if (operator.isOpen()) {
				// directly get the datarate
				Collection<String> monitoringDataTypes = operator.getProvidedMonitoringData();
				for (String type : monitoringDataTypes) {

					if (type.contains("datarate")) {
						IMonitoringData<Double> datarateMonitoringData = operator.getMonitoringData(type);

						datarate = datarateMonitoringData.getValue(); // tuples
																		// per
																		// ms
						if (Double.isNaN(datarate))
							return -1;

						if (Math.abs(datarate) < 0.000001)
							return -1;

						datarate *= 1000; // tupes per sec
						break;
					}

				}
			}
		} catch (NullPointerException ex) {
		}

		return datarate;
	}

	public static double getSelectivityMetadata(IPhysicalOperator operator) {
		double selectivity = -1.0;
		try {
			if (operator.isOpen()) {
				Collection<String> monitoringDataTypes = operator.getProvidedMonitoringData();
				for (String type : monitoringDataTypes) {

					if (type.contains("selectivity")) {
						IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(type);
						if (selectivityMonitoringData == null)
							return -1;

						selectivity = selectivityMonitoringData.getValue();
						if (Double.isNaN(selectivity))
							return -1.0;
					}
				}
			}
		} catch (NullPointerException ex) {
		}
		return selectivity;
	}

	// helpers
	public static double getAvgCPUTimeMetadata(IPhysicalOperator operator) {
		double time = -1.0;
		try {
			if (operator.isOpen()) {

				// measure directly
				IMonitoringData<Double> cpuTime = operator.getMonitoringData("avgCPUTime");
				if (cpuTime != null && cpuTime.getValue() != null && !Double.isNaN(cpuTime.getValue()))
					time = cpuTime.getValue() / 1000000000.0;

			}
		} catch (NullPointerException ex) {
		}
		return time;
	}
}
