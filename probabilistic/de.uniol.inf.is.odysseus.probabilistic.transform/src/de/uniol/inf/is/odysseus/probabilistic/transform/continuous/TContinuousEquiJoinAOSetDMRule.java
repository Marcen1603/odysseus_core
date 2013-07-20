/**
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
package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TContinuousEquiJoinAOSetDMRule extends AbstractTransformationRule<ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {
	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return 1;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final void execute(final ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator, final TransformationConfiguration config) {

		final int[][] joinAttributePos = new int[2][];

		for (int port = 0; port < 2; port++) {
			final int otherPort = port ^ 1;
			if (JoinTransformationHelper.checkPhysicalPath(operator.getSubscribedToSource(port).getTarget())) {
				final Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (JoinTransformationHelper.checkPredicate(operator.getPredicate(), neededAttrs, operator.getSubscribedToSource(port).getSchema(), operator.getSubscribedToSource(otherPort).getSchema())) {

					final SDFSchema schema = operator.getSubscribedToSource(port).getSchema();
					final List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

					for (final Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						if (SchemaUtils.isContinuousProbabilisticAttribute(pair.getE2())) {
							joinAttributes.add(pair.getE1());
						}
					}
					joinAttributePos[port] = SchemaUtils.getAttributePos(schema, joinAttributes);

					final List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(schema.getAttributes());
					viewAttributes.removeAll(joinAttributes);
					@SuppressWarnings("unused")
					final int[] viewPos = SchemaUtils.getAttributePos(schema, viewAttributes);
				}
			}
		}

		// ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic> mergeFunction = new ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic>(
		// operator.getOutputSchema().size(), joinAttributePos);
		//
		// for (int port = 0; port < 2; port++) {
		// mergeFunction.setBetas(operator.getBetas(port), port);
		// mergeFunction.setSigmas(operator.getSigmas(port), port);
		// }
		// operator.setDataMerge(mergeFunction);
		this.update(operator);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>> operator, final TransformationConfiguration config) {
		if (config.getDataTypes().contains(SchemaUtils.DATATYPE)) {
			if (operator.getDataMerge() == null) {
				return true;
			}
		}
		return false;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public final String getName() {
		return "Insert DataMergeFunction ProbabilisticContinuousJoinPO (Probabilistic)";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super ContinuousProbabilisticEquiJoinPO<?, ?>> getConditionClass() {
		return ContinuousProbabilisticEquiJoinPO.class;
	}

}
