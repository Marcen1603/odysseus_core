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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.StandardOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;

@SuppressWarnings("rawtypes")
public class BufferedPipeEstimator implements IOperatorEstimator<BufferPO> {

	@Override
	public Class<BufferPO> getOperatorClass() {
		return BufferPO.class;
	}

	@Override
	public OperatorEstimation<BufferPO> estimateOperator(BufferPO instance, List<OperatorEstimation<?>> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		
		OperatorEstimation<BufferPO> estimation = new StandardOperatorEstimator<BufferPO>().estimateOperator(instance, prevOperators, baseHistograms);
		
		int memUsage = EstimatorHelper.sizeInBytes(instance.getOutputSchema()) * instance.size() + 4;
		
		OperatorDetailCost<BufferPO> cost = new OperatorDetailCost<BufferPO>(instance, memUsage, estimation.getDetailCost().getProcessorCost());
		estimation.setDetailCost(cost);
		
		return estimation;
	}

}
