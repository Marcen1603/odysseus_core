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
import java.util.List;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopKAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.RecommendAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TRecommendAORule extends AbstractTransformationRule<RecommendAO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void execute(final RecommendAO operator,
			final TransformationConfiguration config) throws RuleException {

		SDFAttribute predictedRatingAttribute = operator
				.getPredictedRatingAttribute();
		if (predictedRatingAttribute == null) {
			predictedRatingAttribute = FindAttributeHelper.findAttributeByName(
					operator, operator.DEFAULT_PREDICTED_RATING_ATTRIBUTE_NAME);
		}
		final String predictRatingAttributeName = predictedRatingAttribute
				.getAttributeName();

		operator.setOutputSchema(operator.getInputSchema());

		if (operator.getTopN() != -1) {
			// top n
			final NamedExpression scoringFunction = new NamedExpression(
					"scoringFunction", new SDFExpression(
							predictRatingAttributeName, null, MEP.getInstance()),null);

			final TopKAO topKAo = new TopKAO();
			// topKAo.setName(topKAo.getClass().getSimpleName());
			topKAo.setDescending(true);
			topKAo.setK(operator.getTopN());
			topKAo.setScoringFunction(scoringFunction);

			SDFAttribute userAttribute = operator.getUserAttribute();
			if (userAttribute == null) {
				userAttribute = FindAttributeHelper.findAttributeByName(
						operator, operator.DEFAULT_USER_ATTRIBUTE_NAME);
			}
			final List<SDFAttribute> groupingAttributes = new ArrayList<>();
			groupingAttributes.add(userAttribute);
			topKAo.setGroupingAttributes(groupingAttributes);
			topKAo.setSuppressDuplicates(false);
			topKAo.setTriggerByPunctuation(true);

			LogicalPlan.insertOperatorBefore(topKAo, operator);
			// LogicalPlan.replace(operator, topKAo);
			insert(topKAo);
		}

		if (!Double.isNaN(operator.getMinRating())) {
			// select
			final SDFExpression sdfExpression = new SDFExpression(
					predictRatingAttributeName + " > "
							+ operator.getMinRating(), null, MEP.getInstance());

			final RelationalExpression<?> predicate = new RelationalExpression<>(
					sdfExpression);
			final SelectAO selectAo = new SelectAO(predicate);
			// selectAo.setName(selectAo.getClass().getSimpleName());
			selectAo.setOutputSchema(operator.getInputSchema());
			LogicalPlan.insertOperatorBefore(selectAo, operator);
			insert(selectAo);
		}

		LogicalPlan.removeOperator(operator, false);
		retract(operator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final RecommendAO operator,
			final TransformationConfiguration config) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public String getName() {
		return "RecommendAO -> RecommendPO";
	}

}
