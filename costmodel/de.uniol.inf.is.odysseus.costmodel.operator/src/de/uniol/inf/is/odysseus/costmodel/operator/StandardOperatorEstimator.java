/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.costmodel.operator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.monitoring.physicaloperator.MonitoringDataTypes;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;

/**
 * Standardoperatorschätzer. Wird eingesetzt, wenn ein Operator abgeschätzt
 * werden soll, zu dem kein konkreter Schätzer existiert. Dort werden
 * Histogramme nicht angepasst, die Selektivität ist 1, die Datenrate bleibt
 * unverändert und die Kosten werden aus den Standardkosten ermittelt.
 * 
 * @author Timo Michelsen
 * 
 * @param <T>
 *            Typ des Operators für den StandardSchätzers
 */
public class StandardOperatorEstimator<T> implements IOperatorEstimator<T> {

	private static final long DEFAULT_MEMORY_USAGE_BYTES = 4;

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getOperatorClass() {
		return (Class<T>) Object.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OperatorEstimation<T> estimateOperator(T obj, List<OperatorEstimation<?>> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {

		// Standard-bevavior
		OperatorEstimation<T> estimation = new OperatorEstimation<T>(obj);

		if (obj instanceof IPhysicalOperator) {
			IPhysicalOperator instance = (IPhysicalOperator) obj;
			
			if (instance.isSink()) {
				// assume, that we have only one input-operator
				OperatorEstimation<?> prevOperatorEstimation = prevOperators.get(0);

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
				estimation.setDataStream((IDataStream<T>) new DataStream<IPhysicalOperator>(instance, datarate, intervalLength));

				// OperatorDetailCost
				double cpu = getAvgCPUTimeMetadata(instance) * prevOperatorEstimation.getDataStream().getDataRate();
				if (cpu < 0.0)
					cpu = OperatorCostModelCfg.getInstance().getStandardCpuCost() * prevOperatorEstimation.getDataStream().getDataRate();
				double mem = DEFAULT_MEMORY_USAGE_BYTES;

				estimation.setDetailCost((IOperatorDetailCost<T>) new OperatorDetailCost<IPhysicalOperator>(instance, mem, cpu));
			} else {
				// its a source!
				/** 1. Histograms **/
				Map<SDFAttribute, IHistogram> histograms = new HashMap<SDFAttribute, IHistogram>();
				for (SDFAttribute attribute : instance.getOutputSchema()) {
					if (baseHistograms.containsKey(attribute))
						histograms.put(attribute, baseHistograms.get(attribute));
				}
				estimation.setHistograms(histograms);

				/** 2. Selectivity **/
				estimation.setSelectivity(1.0);

				/** 3. DataStream **/
				double datarate = getDatarateMetadata(instance);
				if (datarate < 0.0) {
					datarate = 1.0; // default... since there are no datastream
				}

				estimation.setDataStream((IDataStream<T>) new DataStream<IPhysicalOperator>(instance, datarate, 1.0));

				/** 4. DetailCost **/
				double cpu = getAvgCPUTimeMetadata(instance);
				// System.out.format("%-20.10f\n", cpu);
				double cpuCost = 0.0;
				if (cpu < 0.0)
					cpuCost = OperatorCostModelCfg.getInstance().getStandardCpuCost() * datarate;
				else {
					cpuCost = cpu * datarate;
				}

				estimation.setDetailCost((IOperatorDetailCost<T>) new OperatorDetailCost<IPhysicalOperator>(instance, DEFAULT_MEMORY_USAGE_BYTES, cpuCost));
			}
		} else {
			estimation.setSelectivity(1.0);

			if (prevOperators.isEmpty()) {
				// source
				estimation.setDataStream(new DataStream<T>(obj, 1.0, 1.0));
				estimation.setDetailCost(new OperatorDetailCost<T>(obj, DEFAULT_MEMORY_USAGE_BYTES, OperatorCostModelCfg.getInstance().getStandardCpuCost()));
			} else {
				// sink
				estimation.setHistograms(prevOperators.get(0).getHistograms());
				estimation.setDataStream(new DataStream<T>(obj, prevOperators.get(0).getDataStream().getDataRate(), prevOperators.get(0).getDataStream().getIntervalLength()));
				estimation.setDetailCost(new OperatorDetailCost<T>(obj, DEFAULT_MEMORY_USAGE_BYTES, OperatorCostModelCfg.getInstance().getStandardCpuCost() * estimation.getDataStream().getDataRate()));
			}
		}
		return estimation;
	}

	// helpers
	// same like in EstimatorHelper-class
	// but using the class would inflict cyclic dependencies...
	private static double getDatarateMetadata(IPhysicalOperator operator) {
		Double datarate = new Double(-1);
		try {
			if (operator.isOpen()) {
				// directly get the datarate
				IMonitoringData<Double> datarateMonitoringData = operator.getMonitoringData(MonitoringDataTypes.DATARATE.name);
				if (datarateMonitoringData != null) {

					datarate = datarateMonitoringData.getValue(); // tuples / ms

					if (datarate == null)
						return -1;

					if (Double.isNaN(datarate))
						return -1;

					if (Math.abs(datarate) < 0.000001)
						return -1;

					datarate *= 1000; // tupes per sec
				}

			}
		} catch (NullPointerException ex) {
		}
		return datarate;
	}

	private static double getSelectivityMetadata(IPhysicalOperator operator) {
		Double selectivity = -1.0;
		try {
			if (operator.isOpen()) {
				IMonitoringData<Double> selectivityMonitoringData = operator.getMonitoringData(MonitoringDataTypes.SELECTIVITY.name);
				if (selectivityMonitoringData != null) {
					selectivity = selectivityMonitoringData.getValue();
					if (selectivity == null || Double.isNaN(selectivity))
						return -1.0;

				}
			}
		} catch (NullPointerException ex) {
		}
		return selectivity;
	}

	private static double getAvgCPUTimeMetadata(IPhysicalOperator operator) {
		double time = -1.0;
		try {
			if (operator.isOpen()) {

				// measure directly
				IMonitoringData<Double> cpuTime = operator.getMonitoringData(MonitoringDataTypes.MEDIAN_PROCESSING_TIME.name);
				if (cpuTime != null && cpuTime.getValue() != null && !Double.isNaN(cpuTime.getValue()))
					time = cpuTime.getValue() / 1000000000.0;

			}
		} catch (NullPointerException ex) {
		}

		return time;
	}
}
