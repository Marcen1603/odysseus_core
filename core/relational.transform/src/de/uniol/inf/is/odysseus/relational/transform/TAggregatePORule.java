/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.IAggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
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
				aggregatePO.getInputSchema(), aggregatePO.getOutputSchema(),
				aggregatePO.getGroupingAttribute(),
				aggregatePO.getAggregations());
		aggregatePO.setGroupProcessor(r);
		SDFAttributeList inputSchema = aggregatePO.getInputSchema();
		
		Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations = aggregatePO
				.getAggregations();
		
		for (SDFAttributeList attrList : aggregations.keySet()) {
			if (SDFAttributeList.subset(attrList, inputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					FESortedClonablePair<SDFAttributeList, AggregateFunction> p = new FESortedClonablePair<SDFAttributeList, AggregateFunction>(
							attrList, e.getKey());
					int[] posArray = new int[p.getE1().size()];
					for (int i = 0; i < p.getE1().size(); ++i) {
						SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
					}
					IAggregateFunctionBuilderRegistry registry = Activator.getAggregateFunctionBuilderRegistry();
					IAggregateFunctionBuilder builder = registry.getBuilder(transformConfig.getDataType(),p.getE2().getName());
					IAggregateFunction aggFunction = builder.createAggFunction(p.getE2(), posArray);
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
		if (transformConfig.getDataType().equals("relational")) {
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
	public Class<?> getConditionClass() {	
		return AggregatePO.class;
	}

}
