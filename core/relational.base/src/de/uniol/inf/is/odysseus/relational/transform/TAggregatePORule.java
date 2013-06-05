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
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational.base.Relational;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings("rawtypes")
public class TAggregatePORule extends AbstractTransformationRule<AggregatePO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(AggregatePO aggregatePO,
			TransformationConfiguration transformConfig) {
		
		RelationalGroupProcessor r = new RelationalGroupProcessor(
				aggregatePO.getInputSchema(), aggregatePO.getInternalOutputSchema(),
				aggregatePO.getGroupingAttribute(),
				aggregatePO.getAggregations());
		aggregatePO.setGroupProcessor(r);
		SDFSchema inputSchema = aggregatePO.getInputSchema();
		
		Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = aggregatePO
				.getAggregations();
		
		for (SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(
							attrList, e.getKey());
					int[] posArray = new int[p.getE1().size()];
					boolean partialAggregateInput = false;
					for (int i = 0; i < p.getE1().size(); ++i) {
						SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
						if (attr.getDatatype().isPartialAggregate()){
							partialAggregateInput = true;
						}
					}
					IAggregateFunctionBuilderRegistry registry = Activator.getAggregateFunctionBuilderRegistry();
					IAggregateFunctionBuilder builder = registry.getBuilder(Relational.RELATIONAL,p.getE2().getName());
					if (builder == null){
						throw new RuntimeException("Could not find a builder for "+p.getE2().getName());
					}
					IAggregateFunction aggFunction = builder.createAggFunction(p.getE2(), posArray, partialAggregateInput);
					aggregatePO.setInitFunction(p, aggFunction);
					aggregatePO.setMergeFunction(p, aggFunction);
					aggregatePO.setEvalFunction(p, aggFunction);
				}
			}
		}
		update(aggregatePO);
	}

	@Override
	public boolean isExecutable(AggregatePO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataTypes().contains(Relational.RELATIONAL)) {
			if (operator.getGroupProcessor() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Insert RelationalTupleGroupingHelper";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}
	
	@Override
	public Class<? super AggregatePO> getConditionClass() {	
		return AggregatePO.class;
	}

}
