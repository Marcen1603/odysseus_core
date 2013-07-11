/*
 * Copyright 2013 The Odysseus Team
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

package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

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
import de.uniol.inf.is.odysseus.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.transform.Activator;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TAggregateProbabilisticRule extends AbstractTransformationRule<AggregateTIPO<?, ?, ?>> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final void execute(final AggregateTIPO<?, ?, ?> operator, final TransformationConfiguration config) {
		@SuppressWarnings({ "rawtypes" })
		final RelationalGroupProcessor r = new RelationalGroupProcessor(operator.getInputSchema(), operator.getInternalOutputSchema(), operator.getGroupingAttribute(), operator.getAggregations());
		operator.setGroupProcessor(r);
		final SDFSchema inputSchema = operator.getInputSchema();

		final Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = operator.getAggregations();

		for (final SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				final Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(attrList);
				for (final Entry<AggregateFunction, SDFAttribute> e : funcs.entrySet()) {
					final FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(attrList, e.getKey());
					final int[] posArray = new int[p.getE1().size()];
					boolean partialAggregateInput = false;
					String inDatatype = null;
					for (int i = 0; i < p.getE1().size(); ++i) {
						final SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
						// For most cases its the only datatype ... so keep one of them
						inDatatype = attr.getDatatype().getURI();
						if (attr.getDatatype().isPartialAggregate()) {
							partialAggregateInput = true;
						}
					}
					final IAggregateFunctionBuilderRegistry registry = Activator.getAggregateFunctionBuilderRegistry();
					final IAggregateFunctionBuilder builder;

					if (e.getValue().getDatatype() instanceof SDFProbabilisticDatatype) {
						final SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) e.getValue().getDatatype();
						if (datatype.isContinuous()) {
							builder = registry.getBuilder(SchemaUtils.DATATYPE, p.getE2().getName());
						} else {
							builder = registry.getBuilder(SchemaUtils.DATATYPE, p.getE2().getName());
						}
					} else {
						builder = registry.getBuilder(SchemaUtils.DATATYPE, p.getE2().getName());
					}
					if (builder == null) {
						throw new RuntimeException("Could not find a builder for " + p.getE2().getName());
					}
					@SuppressWarnings("rawtypes")
					final IAggregateFunction aggFunction = builder.createAggFunction(p.getE2(), posArray, partialAggregateInput, inDatatype);
					operator.setInitFunction(p, aggFunction);
					operator.setMergeFunction(p, aggFunction);
					operator.setEvalFunction(p, aggFunction);
				}
			}
		}
		this.update(operator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final AggregateTIPO<?, ?, ?> operator, final TransformationConfiguration config) {
		if (config.getDataTypes().contains(SchemaUtils.DATATYPE)) {
			if (operator.getGroupProcessor() == null) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public final String getName() {
		return "AggregateTIPO use probabilistic aggregations (IProbabilistic)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super AggregateTIPO<?, ?, ?>> getConditionClass() {
		return AggregatePO.class;
	}

}
