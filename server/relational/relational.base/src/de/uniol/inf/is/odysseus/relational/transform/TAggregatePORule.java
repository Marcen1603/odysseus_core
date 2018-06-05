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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IAggregateFunctionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
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
			TransformationConfiguration transformConfig) throws RuleException {

		SDFSchema outputSchema = aggregatePO.getInternalOutputSchema();
		List<SDFAttribute> outAttributes = new ArrayList<>(
				outputSchema.getAttributes());
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
					String datatype = null;
					for (int i = 0; i < p.getE1().size(); ++i) {
						SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
						if (attr.getDatatype().isPartialAggregate()) {
							partialAggregateInput = true;
						}
						// For most cases its the only datatype ... so keep one
						// of them
						datatype = attr.getDatatype().getURI();
					}
			
					IAggregateFunctionBuilder builder = AggregateFunctionBuilderRegistry.getBuilder(
							inputSchema.getType(), p.getE2().getName());
					if (builder == null) {
						throw new RuntimeException(
								"Could not find a builder for "
										+ p.getE2().getName());
					}
					IAggregateFunction aggFunction = builder.createAggFunction(
							p.getE2(), p.getE1(), posArray, partialAggregateInput,
							datatype);
					aggregatePO.setAggregateFunction(p, aggFunction);
				}
			}
		}

		// UPDATE PARTIAL_AGGREGATE IN OUTPUT SCHEMA!!
		// Determine Aggregate-Function for output attribute
		for (int i = 0; i < outAttributes.size(); i++) {
			SDFAttribute attr = outAttributes.get(i);
			if (attr.getDatatype().equals(SDFDatatype.PARTIAL_AGGREGATE)) {
				IInitializer ifunc = aggregatePO.getInitFunction(attr);
				if (ifunc != null) {
					SDFDatatype dt = ifunc.getPartialAggregateType();
					outAttributes.remove(i);
					outAttributes.add(i, new SDFAttribute(attr.getSourceName(), attr.getAttributeName(), dt, null, null, null));
				}
			}

		}
		
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewWithAttributes(outAttributes, outputSchema); 

		RelationalGroupProcessor r = new RelationalGroupProcessor(
				aggregatePO.getInputSchema(), newOutputSchema,
				aggregatePO.getGroupingAttribute(),
				aggregatePO.getAggregations(), aggregatePO.isFastGrouping());
		aggregatePO.setGroupProcessor(r);
		aggregatePO.setOutputSchema(newOutputSchema);
		update(aggregatePO);
	}

	@Override
	public boolean isExecutable(AggregatePO operator,
			TransformationConfiguration transformConfig) {
		if (operator.getInputSchema().getType() == Tuple.class) {
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
