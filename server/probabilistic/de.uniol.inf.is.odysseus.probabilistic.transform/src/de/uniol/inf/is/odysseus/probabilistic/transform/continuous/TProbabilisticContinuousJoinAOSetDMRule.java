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

import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticContinuousJoinAOSetDMRule extends AbstractTransformationRule<JoinTIPO<ITimeInterval, Tuple<ITimeInterval>>> {

	@Override
	public int getPriority() {
		return TransformationConstants.PRIORITY;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(final JoinTIPO joinPO, final TransformationConfiguration transformConfig) {
		final IDataMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic> dataMerge = new ProbabilisticMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic>(joinPO.getOutputSchema().size());
		joinPO.setDataMerge(dataMerge);
		update(joinPO);
	}

	@Override
	public boolean isExecutable(final JoinTIPO<ITimeInterval, Tuple<ITimeInterval>> operator, final TransformationConfiguration transformConfig) {
		if (operator.getOutputSchema().getType() == ProbabilisticTuple.class) {
			if (operator.getDataMerge() == null) {
				IPredicate<?> predicate = operator.getPredicate();
				final Set<SDFAttribute> attributes = PredicateUtils.getAttributes(predicate);
				if (SchemaUtils.containsContinuousProbabilisticAttributes(attributes)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Insert DataMergeFunction JoinTIPO (Probabilistic)";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

	@Override
	public Class<? super JoinTIPO<?, ?>> getConditionClass() {
		return JoinTIPO.class;
	}

}
