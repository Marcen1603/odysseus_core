/**********************************************************************************
 * Copyright 2014 The Odysseus Team
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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.PredictRatingAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.PredictRatingPO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.PredictRatingPO.PredcitRatingTupleSchema;
import de.uniol.inf.is.odysseus.recommendation.util.TupleSchemaHelper;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TPredictRatingAORule extends
AbstractTransformationRule<PredictRatingAO> {

	@Override
	public void execute(final PredictRatingAO operator,
			final TransformationConfiguration config) throws RuleException {

		if (operator.getSubscribedToSource().size() > 1) {
			insertJoinBefore(operator);
		}

		SDFAttribute userAttribute = operator.getUserAttribute();
		if (userAttribute == null) {
			userAttribute = FindAttributeHelper.findAttributeByName(operator,
					PredictRatingAO.DEFAULT_USER_ATTRIBUTE_NAME);
		}

		SDFAttribute itemAttribute = operator.getItemAttribute();
		if (itemAttribute == null) {
			itemAttribute = FindAttributeHelper.findAttributeByName(operator,
					PredictRatingAO.DEFAULT_ITEM_ATTRIBUTE_NAME);
		}

		SDFAttribute modelAttribute = operator.getModelAttribute();
		if (modelAttribute == null) {
			modelAttribute = FindAttributeHelper.findAttributeByName(operator,
					PredictRatingAO.DEFAULT_MODEL_ATTRIBUTE_NAME);
		}

		final int modelPort = FindAttributeHelper.findPortWithAttribute(
				operator, modelAttribute);
		final int elementsPort = FindAttributeHelper.findPortWithAttribute(
				operator, itemAttribute);
		final int modelAttributeIndex = FindAttributeHelper.findAttributeIndex(
				operator, modelAttribute, modelPort);
		final int userAttibuteIndex = FindAttributeHelper.findAttributeIndex(
				operator, userAttribute, elementsPort);
		final int itemAttibuteIndex = FindAttributeHelper.findAttributeIndex(
				operator, itemAttribute, elementsPort);

		if (modelPort == -1) {
			throw new IllegalArgumentException("Model port not found.");
		}
		if (elementsPort == -1) {
			throw new IllegalArgumentException("Elements port not found.");
		}

		final Map<PredcitRatingTupleSchema, Integer> map = new HashMap<PredcitRatingTupleSchema, Integer>();
		map.put(PredcitRatingTupleSchema.MODEL, modelAttributeIndex);
		map.put(PredcitRatingTupleSchema.ITEM, itemAttibuteIndex);
		map.put(PredcitRatingTupleSchema.USER, userAttibuteIndex);
		final TupleSchemaHelper<ITimeInterval, PredcitRatingTupleSchema> tsh = new TupleSchemaHelper<>(
				map);

		final PredictRatingPO<ITimeInterval, ?, ?> po = new PredictRatingPO<>(
				tsh);

		defaultExecute(operator, po, config, true, true);

	}

	/**
	 *
	 */
	private void insertJoinBefore(final PredictRatingAO operator) {
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

	@Override
	public boolean isExecutable(final PredictRatingAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "PredictRatingAO -> PredictRatingPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
