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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.MemoryUsageSaver;
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
		
		estimation.setDetailCost(new OperatorDetailCost(instance, MemoryUsageSaver.get(instance), cpuCost));

		return estimation;
	}

}
