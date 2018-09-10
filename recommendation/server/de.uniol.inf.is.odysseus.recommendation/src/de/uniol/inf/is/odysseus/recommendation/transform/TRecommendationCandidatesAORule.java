/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.transform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.RecommendationCandidatesAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.RecommendationCandidatesPO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.RecommendationCandidatesPO.RecommCandTupleSchema;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TRecommendationCandidatesAORule extends
		AbstractTransformationRule<RecommendationCandidatesAO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final RecommendationCandidatesAO operator,
			final TransformationConfiguration config) throws RuleException {

		if (operator.getSubscribedToSource().size() > 1) {
			insertJoinBefore(operator);
		}

		final int userAttributePort = FindAttributeHelper
				.findPortWithAttribute(operator,
						operator.getRequestingUserAttribute());
		final int userAttrIndex = FindAttributeHelper.findAttributeIndex(
				operator, operator.getRequestingUserAttribute(),
				userAttributePort);

		final int ratedInfoAttributePort = FindAttributeHelper
				.findPortWithAttribute(operator,
						operator.getRatedItemsInfoAttribute());
		final int ratedInfoAttrIndex = FindAttributeHelper.findAttributeIndex(
				operator, operator.getRatedItemsInfoAttribute(),
				ratedInfoAttributePort);

		final Map<RecommCandTupleSchema, Integer> map = new HashMap<RecommCandTupleSchema, Integer>();
		map.put(RecommCandTupleSchema.USER, userAttrIndex);
		map.put(RecommCandTupleSchema.RATED_ITEMS_INFO, ratedInfoAttrIndex);
		final TupleSchemaHelper<ITimeInterval, RecommCandTupleSchema> tsh = new TupleSchemaHelper<>(
				map);

		defaultExecute(operator, new RecommendationCandidatesPO<>(tsh), config,
				true, true);

	}

	/**
	 *
	 */
	private void insertJoinBefore(final RecommendationCandidatesAO operator) {
		final JoinAO join = new JoinAO();

		// LogicalPlan.insertOperator(join, operator, 0, 0, 0);
		// LogicalPlan.insertOperator(join, operator, 1, 1, 0);

		final Collection<LogicalSubscription> subscriptions = new ArrayList<>(
				operator.getSubscribedToSource());
		for (final LogicalSubscription s : subscriptions) {
			operator.unsubscribeFromSource(s);
			join.subscribeToSource(s.getSource(), s.getSinkInPort(),
					s.getSourceOutPort(), s.getSchema());
		}
		operator.subscribeToSource(join, 0, 0, join.getOutputSchema());

		insert(join);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final RecommendationCandidatesAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "RecommendationCandidatesAO -> RecommendationCandidatesPO";
	}
}
