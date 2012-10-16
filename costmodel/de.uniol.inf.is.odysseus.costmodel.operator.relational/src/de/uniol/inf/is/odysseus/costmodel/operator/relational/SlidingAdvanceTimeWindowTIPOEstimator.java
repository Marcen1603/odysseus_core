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
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.MemoryUsageSaver;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

@SuppressWarnings("rawtypes")
public class SlidingAdvanceTimeWindowTIPOEstimator implements IOperatorEstimator<SlidingAdvanceTimeWindowTIPO> {

	@Override
	public Class<SlidingAdvanceTimeWindowTIPO> getOperatorClass() {
		return SlidingAdvanceTimeWindowTIPO.class;
	}

	@Override
	public OperatorEstimation estimateOperator(SlidingAdvanceTimeWindowTIPO instance, List<OperatorEstimation> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
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
		if (windowAdvance == 1) // gleitend zeitbasiert
			g = windowSize;
		else
			// springend zeitbasiert
			g = windowSize / 2.0;

		DataStream stream = new DataStream(instance, r, g);
		estimation.setDataStream(stream);

		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
//		System.out.format("%8.6f\n", cpu);
		double cpuCost = 0.0;
		if (cpu < 0.0)
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * r;
		else {
			cpuCost = cpu * r;
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		
		estimation.setDetailCost(new OperatorDetailCost(instance, MemoryUsageSaver.get(instance), cpuCost));

		return estimation;
	}

}
