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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.intervalapproach.StreamGroupingWithAggregationPO;

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
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
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
