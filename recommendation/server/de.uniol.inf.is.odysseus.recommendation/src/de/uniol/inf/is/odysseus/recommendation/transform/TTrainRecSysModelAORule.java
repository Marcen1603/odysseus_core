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

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.TrainRecSysModelAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.TrainRecSysModelPO;
import de.uniol.inf.is.odysseus.recommendation.registry.RecommendationLearnerRegistry;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 *
 */
public class TTrainRecSysModelAORule extends AbstractTransformationRule<TrainRecSysModelAO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final TrainRecSysModelAO operator, final TransformationConfiguration config)
			throws RuleException {

		final TrainRecSysModelPO<?, ?, ?, ?> trainRecSysModelPO = RecommendationLearnerRegistry.getInstance()
				.createTrainRecSysModelPO(operator.getLearner(), operator.getInputSchema(0),
						operator.getUserAttribute(), operator.getItemAttribute(), operator.getRatingAttribute(),
						operator.getOptions(), operator.getOutputUsedItems());
		trainRecSysModelPO.setOutputSchema(operator.getOutputSchema(0), 0);
		trainRecSysModelPO.setOutputSchema(operator.getOutputSchema(1), 1);
		trainRecSysModelPO.setTransferModelOnlyOnPunctuation(Boolean.parseBoolean(operator.getOptions().getOrDefault("transferModelOnlyOnPunctuation", "false")));

		defaultExecute(operator, trainRecSysModelPO, config, true, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(final TrainRecSysModelAO operator, final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TrainRecSysModelAO -> TrainRecSysModelPO";
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

}
