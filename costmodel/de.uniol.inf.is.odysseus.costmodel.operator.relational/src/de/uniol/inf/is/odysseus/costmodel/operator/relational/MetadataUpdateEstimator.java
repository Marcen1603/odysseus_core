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

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorCostModelCfg;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.StandardOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;

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
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
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
