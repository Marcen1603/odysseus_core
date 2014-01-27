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
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.costmodel.operator.DataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IDataStream;
import de.uniol.inf.is.odysseus.costmodel.operator.IOperatorEstimator;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorDetailCost;
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.costmodel.operator.datasrc.IHistogram;
import de.uniol.inf.is.odysseus.costmodel.operator.util.CPURateSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.EstimatorHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.MemoryUsageSaver;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateHistogramHelper;
import de.uniol.inf.is.odysseus.costmodel.operator.util.PredicateSelectivityHelper;

@SuppressWarnings("rawtypes")
public class SelectEstimator implements IOperatorEstimator<SelectPO> {

	@Override
	public Class<SelectPO> getOperatorClass() {
		return SelectPO.class;
	}

	@Override
	public OperatorEstimation<SelectPO> estimateOperator(SelectPO instance, List<OperatorEstimation<?>> prevOperators, Map<SDFAttribute, IHistogram> baseHistograms) {
		List<Map<SDFAttribute, IHistogram>> histograms = new ArrayList<Map<SDFAttribute, IHistogram>>();
		for( OperatorEstimation<?> estimation : prevOperators) {
			histograms.add(estimation.getHistograms());
		}
		
		OperatorEstimation<SelectPO> estimation = new OperatorEstimation<SelectPO>(instance);
				
		/** 1. Histograms **/
		IPredicate predicate = instance.getPredicate();

		PredicateHistogramHelper helper = new PredicateHistogramHelper(predicate, histograms);
		estimation.setHistograms(helper.getHistograms());
		
		/** 2. Selectivity **/
		double selectivity = EstimatorHelper.getSelectivityMetadata(instance);
		if( selectivity < 0.0 ) {
			PredicateSelectivityHelper helper2 = new PredicateSelectivityHelper(predicate, histograms);
			selectivity = helper2.getSelectivity();
		}
		estimation.setSelectivity(selectivity);
		
		
		/** 3. Datarate **/
		IDataStream c = prevOperators.get(0).getDataStream();
		double datarate = EstimatorHelper.getDatarateMetadata(instance);
		if( datarate < 0.0) {
			datarate = c.getDataRate() * selectivity;
		}
		estimation.setDataStream(new DataStream<SelectPO>(instance, datarate, c.getIntervalLength()));

		/** 4. DetailCost **/
		double cpu = EstimatorHelper.getMedianCPUTimeMetadata(instance);
		double cpuCost = 0.0;
		if( cpu < 0.0 )
			cpuCost = CPURateSaver.getInstance().get(instance.getClass().getSimpleName()) * c.getDataRate();
		else {
			cpuCost = cpu * c.getDataRate();
			CPURateSaver.getInstance().set(instance.getClass().getSimpleName(), cpu);
		}
		estimation.setDetailCost(new OperatorDetailCost<SelectPO>(instance, MemoryUsageSaver.get(instance), cpuCost));
		
		return estimation;
	}
}
