/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParallelIntraOperatorSetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValue;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.value.ParallelIntraOperatorSettingValueElement;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.threaded.ThreadedAggregateTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TAggregationTIPORule extends AbstractIntervalTransformationRule<AggregateAO> {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(AggregateAO aggregateAO, TransformationConfiguration transformConfig) throws RuleException {
		List<String> metadataSet = aggregateAO.getInputSchema().getMetaAttributeNames();
		// Attention: Time meta data is set in aggregation
		metadataSet.remove(ITimeInterval.class.getName());
		@SuppressWarnings("rawtypes")
		IMetadataMergeFunction mf = MetadataRegistry.getMergeFunction(metadataSet);

		AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>> po = null;

		// @Autor: Chris Tönjes-Deye
		// threaded aggregate operator
		boolean useThreadedOperator = false;
		boolean useRoundRobinAllocation = false;
		int degree = 0;
		int maxBuffersize = 0;

		if (aggregateAO.getNumberOfThreads() > 1) {
			// if the aggregate operator is configured via pql
			useThreadedOperator = true;
			degree = aggregateAO.getNumberOfThreads();
			maxBuffersize = aggregateAO.getMaxBufferSize();
			useRoundRobinAllocation = aggregateAO.isUseRoundRobinAllocation();
		} else if (transformConfig.getOption(ParallelIntraOperatorSetting.class.getName()) != null) {
			// if configuration is done via odysseus script
			ParallelIntraOperatorSettingValue value = ((ParallelIntraOperatorSetting) transformConfig
					.getOption(ParallelIntraOperatorSetting.class.getName())).getValue();
			if (value != null) {
				if (!value.hasIndividualSettings()) {
					// if configuration is done for every operator use global
					// configuration
					degree = value.getGlobalDegree();
					maxBuffersize = value.getGlobalBuffersize();
					useThreadedOperator = true;
				} else {
					// if configuration is only for specific operators, check if
					// id is equal and get configuration
					String operatorId = aggregateAO.getUniqueIdentifier();
					if (value.hasIndividualSettingsForOperator(operatorId)) {
						ParallelIntraOperatorSettingValueElement individualSettings = value
								.getIndividualSettings(operatorId);
						degree = individualSettings.getIndividualDegree();
						maxBuffersize = individualSettings.getIndividualBuffersize();
						useThreadedOperator = true;
					}
				}
			}
		}

		// if we could use an threaded operator, create it, but only if degree
		// and buffersize are valid
		if (useThreadedOperator && degree > 1 && maxBuffersize > 1) {
				po = new ThreadedAggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>(
						aggregateAO.getInputSchema(), aggregateAO.getOutputSchemaIntern(0),
						aggregateAO.getGroupingAttributes(), aggregateAO.getAggregations(),
						aggregateAO.isFastGrouping(), mf, degree, maxBuffersize, useRoundRobinAllocation);

		} else {
			// otherwise create normal aggregate (non threaded)
				po = new AggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>(
						aggregateAO.getInputSchema(), aggregateAO.getOutputSchemaIntern(0),
						aggregateAO.getGroupingAttributes(), aggregateAO.getAggregations(),
						aggregateAO.isFastGrouping(), mf);
			
		}
		if (po != null) {
			po.setDumpAtValueCount(aggregateAO.getDumpAtValueCount());

			po.setOutputPA(aggregateAO.isOutputPA());
			po.setDrainAtDone(aggregateAO.isDrainAtDone());
			po.setDrainAtClose(aggregateAO.isDrainAtClose());
			// ACHTUNG: Die Zeit-Metadaten werden manuell in der Aggregation
			// gesetzt!!
			// ((CombinedMergeFunction) po.getMetadataMerge()).add(new
			// TimeIntervalInlineMetadataMergeFunction());
			defaultExecute(aggregateAO, po, transformConfig, true, true);
		}
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AggregateAO> getConditionClass() {
		return AggregateAO.class;
	}

}
