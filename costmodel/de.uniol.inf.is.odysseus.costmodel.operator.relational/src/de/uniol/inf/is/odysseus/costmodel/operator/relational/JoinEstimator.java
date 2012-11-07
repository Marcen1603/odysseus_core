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
package de.uniol.inf.is.odysseus.costmodel.operator.relational;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateHistogramHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateSelectivityHelper;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;

@SuppressWarnings("rawtypes")
public class JoinEstimator implements IOperatorEstimator<JoinTIPO> {

	@Override
	public Class<JoinTIPO> getOperatorClass() {
		return JoinTIPO.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OperatorEstimation estimateOperator(JoinTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {

		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		for (OperatorEstimation estimation : prevOperators)
			histograms.add(estimation.getHistograms());

		IPredicate predicate = instance.getPredicate();
		if (predicate instanceof TruePredicate || predicate.toString().equalsIgnoreCase("true"))
			return estimateCrossProductOperator(instance, prevOperators, baseHistograms);

		OperatorEstimation estimation = new OperatorEstimation(instance);
		/** 1. Histograms **/
		PredicateHistogramHelper helper = new PredicateHistogramHelper(predicate, histograms);
		estimation.setHistograms(helper.getHistograms());

		/** 2. Selectivity **/
		IDataStream c0 = prevOperators.get(0).getDataStream();
		IDataStream c1 = prevOperators.get(1).getDataStream();
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		if (selectivity < 0.0) {
			PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(predicate, histograms);
			double trueProp = helper2.getSelectivity();
			double combis = c0.getDataRate() * (c1.getDataRate() * c1.getIntervalLength() ) + 
							c1.getDataRate() * (c0.getDataRate() * c0.getIntervalLength() );
			double outputRate = trueProp * combis;
			double inputRate = (c0.getDataRate() * c1.getDataRate() * c0.getIntervalLength() * c1.getIntervalLength());
			
			selectivity = outputRate / inputRate;
		}
		estimation.setSelectivity(selectivity);

		/** 3. DataStream **/
		// join has two inputs
		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
		if (dataRate < 0) {
			dataRate = selectivity * (c0.getDataRate() * c1.getDataRate() * c1.getIntervalLength() * c0.getIntervalLength() );
		}

		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream(instance, dataRate, intervalLength));
		
		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * ( c0.getDataRate() + c1.getDataRate() );
		} else {
			cpuCost = cpu * ( c0.getDataRate() + c1.getDataRate() ) ;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}

		double memCost = 0.0;
		if( !instance.isOpen() ) {
			memCost = ( c0.getDataRate() * c0.getIntervalLength() + c1.getDataRate() * c1.getIntervalLength() ) * EstimatorHelper.sizeInBytes(instance.getOutputSchema());
		} else {
			memCost = EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * EstimatorHelper.elementCountOfSweepAreas(instance.getAreas());
			memCost+= EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * instance.getTransferFunction().size();
		}
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));

		return estimation;
	}

	// Kreuzprodukt
	@SuppressWarnings("unchecked")
	private static OperatorEstimation estimateCrossProductOperator(JoinTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		OperatorEstimation estimation = new OperatorEstimation(instance);

		OperatorEstimation op1 = prevOperators.get(0);
		OperatorEstimation op2 = prevOperators.get(1);

		/** 1. Histograms **/
		double factor1 = op1.getDataStream().getDataRate() * op1.getDataStream().getIntervalLength();
		double factor2 = op2.getDataStream().getDataRate() * op2.getDataStream().getIntervalLength();

		Map<SDFAttribute, IHistogram> outputHistograms = new HashMap<SDFAttribute, IHistogram>();

		for (SDFAttribute attribute : op1.getHistograms().keySet()) {
			IHistogram histogram = op1.getHistograms().get(attribute).clone();

			double[] occs = histogram.getAllOccurences();
			for (int i = 0; i < histogram.getIntervalCount(); i++) {
				histogram.setOccurences(i, occs[i] * factor2);
			}

			outputHistograms.put(attribute, histogram);
		}

		for (SDFAttribute attribute : op2.getHistograms().keySet()) {
			IHistogram histogram = op2.getHistograms().get(attribute).clone();

			double[] occs = histogram.getAllOccurences();
			for (int i = 0; i < histogram.getIntervalCount(); i++) {
				histogram.setOccurences(i, occs[i] * factor1);
			}

			outputHistograms.put(attribute, histogram);
		}
		estimation.setHistograms(outputHistograms);

		/** 2. Selektivität **/
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		estimation.setSelectivity(selectivity >= 0.0 ? selectivity : 1.0);

		/** 3. DataStream **/
		IDataStream c0 = prevOperators.get(0).getDataStream();
		IDataStream c1 = prevOperators.get(1).getDataStream();

		double dataRate = EstimatorHelper.getDatarateMetadata(instance);
		if (dataRate < 0) {
			dataRate = c0.getDataRate() * (c1.getIntervalLength() * c1.getDataRate()) + c1.getDataRate() * (c0.getIntervalLength() * c0.getDataRate());
		}

		double intervalLength = (c0.getIntervalLength() * c1.getIntervalLength()) / (c0.getIntervalLength() + c1.getIntervalLength());
		estimation.setDataStream(new DataStream(instance, dataRate, intervalLength));

		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if (cpu < 0.0) {
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName() + "_Cross") * ( c0.getDataRate() + c1.getDataRate() );
		} else {
			cpuCost = cpu * ( c0.getDataRate() + c1.getDataRate() ) ;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName() + "_Cross", cpu);
		}

		double memCost = 0.0;
		if( !instance.isOpen() ) {
			memCost = ( c0.getDataRate() * c0.getIntervalLength() + c1.getDataRate() * c1.getIntervalLength() ) * EstimatorHelper.sizeInBytes(instance.getOutputSchema());
		} else {
			memCost = EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * EstimatorHelper.elementCountOfSweepAreas(instance.getAreas());
			memCost+= EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * instance.getTransferFunction().size();
		}
		estimation.setDetailCost(new OperatorDetailCost(instance, memCost, cpuCost));

		return estimation;
	}
}
