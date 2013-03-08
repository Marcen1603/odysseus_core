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

package de.uniol.inf.is.odysseus.probabilistic.transform;

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
import de.uniol.inf.is.odysseus.probabilistic.Activator;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregate.functions.ProbabilisticConstants;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TAggregateProbabilisticRule extends
		AbstractTransformationRule<AggregateTIPO<?, ?, ?>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(final AggregateTIPO<?, ?, ?> operator,
			final TransformationConfiguration config) {
		@SuppressWarnings({ "rawtypes" })
		final RelationalGroupProcessor r = new RelationalGroupProcessor(
				operator.getInputSchema(), operator.getInternalOutputSchema(),
				operator.getGroupingAttribute(), operator.getAggregations());
		operator.setGroupProcessor(r);
		final SDFSchema inputSchema = operator.getInputSchema();

		final Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = operator
				.getAggregations();

		for (final SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				final Map<AggregateFunction, SDFAttribute> funcs = aggregations
						.get(attrList);
				for (final Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					final FESortedClonablePair<SDFSchema, AggregateFunction> p = new FESortedClonablePair<SDFSchema, AggregateFunction>(
							attrList, e.getKey());
					final int[] posArray = new int[p.getE1().size()];
					for (int i = 0; i < p.getE1().size(); ++i) {
						final SDFAttribute attr = p.getE1().get(i);
						posArray[i] = inputSchema.indexOf(attr);
					}
					final IAggregateFunctionBuilderRegistry registry = Activator
							.getAggregateFunctionBuilderRegistry();
					final IAggregateFunctionBuilder builder;

					if (e.getValue().getDatatype() instanceof SDFProbabilisticDatatype) {
						SDFProbabilisticDatatype datatype = (SDFProbabilisticDatatype) e
								.getValue().getDatatype();
						if (datatype.isContinuous()) {
							builder = registry
									.getBuilder(
											TransformUtil.DATATYPE,
											ProbabilisticConstants.CONTINUOUS_PROBABILISTIC_NAMESPACE
													+ p.getE2().getName());
						} else {
							builder = registry
									.getBuilder(
											TransformUtil.DATATYPE,
											ProbabilisticConstants.PROBABILISTIC_NAMESPACE
													+ p.getE2().getName());
						}
					} else {
						builder = registry.getBuilder(TransformUtil.DATATYPE, p
								.getE2().getName());
					}
					if (builder == null) {
						throw new RuntimeException(
								"Could not find a builder for "
										+ p.getE2().getName());
					}
					@SuppressWarnings("rawtypes")
					final IAggregateFunction aggFunction = builder
							.createAggFunction(p.getE2(), posArray);
					operator.setInitFunction(p, aggFunction);
					operator.setMergeFunction(p, aggFunction);
					operator.setEvalFunction(p, aggFunction);
				}
			}
		}
		this.update(operator);
	}

	@Override
	public boolean isExecutable(final AggregateTIPO<?, ?, ?> operator,
			final TransformationConfiguration config) {
		if (config.getDataTypes().contains(TransformUtil.DATATYPE)) {
			if (operator.getGroupProcessor() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AggregateTIPO use probabilistic aggregations (IProbabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super AggregateTIPO<?, ?, ?>> getConditionClass() {
		return AggregatePO.class;
	}

}
