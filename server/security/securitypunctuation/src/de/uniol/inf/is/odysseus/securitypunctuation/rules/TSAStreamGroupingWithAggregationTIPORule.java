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
package de.uniol.inf.is.odysseus.securitypunctuation.rules;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator.SAAggregateTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSAStreamGroupingWithAggregationTIPORule extends AbstractTransformationRule<AggregateAO> {

	@Override
	public int getPriority() {
		return 100;
	}
	
	@Override
	public void execute(AggregateAO aggregateAO, TransformationConfiguration transformConfig) {
		SAAggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>> po = new SAAggregateTIPO<ITimeInterval, IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>(aggregateAO.getInputSchema(), aggregateAO.getOutputSchemaIntern(0) , aggregateAO.getGroupingAttributes(),
				aggregateAO.getAggregations());
		po.setDumpAtValueCount(aggregateAO.getDumpAtValueCount());
		po.setMetadataMerge(new CombinedMergeFunction<ITimeInterval>());
		// ACHTUNG: Die Zeit-Metadaten werden manuell in der Aggregation gesetzt!!
		//((CombinedMergeFunction) po.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		defaultExecute(aggregateAO, po, transformConfig, true, true);
		
	}

	@Override
	public boolean isExecutable(AggregateAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				if (transformConfig.getOption("isSecurityAware") != null) {
					if (Boolean.parseBoolean((String)transformConfig.getOption("isSecurityAware")) ){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateAO -> SAAggregateTIPO";
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
