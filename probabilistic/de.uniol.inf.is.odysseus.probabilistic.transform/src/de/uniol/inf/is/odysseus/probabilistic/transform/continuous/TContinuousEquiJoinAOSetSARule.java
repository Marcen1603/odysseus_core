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
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.LinearRegressionTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ContinuousProbabilisticEquiJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TContinuousEquiJoinAOSetSARule extends AbstractTransformationRule<ContinuousProbabilisticEquiJoinPO<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(final ContinuousProbabilisticEquiJoinPO joinPO, final TransformationConfiguration transformConfig) {
		final LinearRegressionTISweepArea[] areas = new LinearRegressionTISweepArea[2];

		for (int port = 0; port < 2; port++) {
			final int otherPort = port ^ 1;
			if (JoinTransformationHelper.checkPhysicalPath(joinPO.getSubscribedToSource(port).getTarget())) {
				final Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

				if (JoinTransformationHelper.checkPredicate(joinPO.getPredicate(), neededAttrs, joinPO.getSubscribedToSource(port).getSchema(), joinPO.getSubscribedToSource(otherPort).getSchema())) {

					final SDFSchema schema = joinPO.getSubscribedToSource(port).getSchema();
					final List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

					for (final Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
						if (SchemaUtils.isContinuousProbabilisticAttribute(pair.getE2())) {
							joinAttributes.add(pair.getE1());
						}
					}
					final int[] joinPos = SchemaUtils.getAttributePos(schema, joinAttributes);

					final List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(schema.getAttributes());
					viewAttributes.removeAll(joinAttributes);
					final int[] viewPos = SchemaUtils.getAttributePos(schema, viewAttributes);
					areas[port] = new LinearRegressionTISweepArea(joinPos, viewPos);
					joinPO.setBetas(areas[port].getRegressionCoefficients(), port);
				}
			}
		}

		joinPO.setAreas(areas);

	}

	@Override
	public boolean isExecutable(final ContinuousProbabilisticEquiJoinPO operator, final TransformationConfiguration transformConfig) {
		if ((transformConfig.getDataTypes().contains(SchemaUtils.DATATYPE)) && transformConfig.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
			if (operator.getAreas() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProbabilisticContinuousJoinPO set SweepArea";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super ContinuousProbabilisticEquiJoinPO> getConditionClass() {
		return ContinuousProbabilisticEquiJoinPO.class;
	}

}
