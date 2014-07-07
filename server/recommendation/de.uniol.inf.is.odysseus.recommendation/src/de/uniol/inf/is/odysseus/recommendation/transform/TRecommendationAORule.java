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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.RecommendationAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.RecommendationPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Cornelius Ludmann
 * 
 */
public class TRecommendationAORule extends
AbstractTransformationRule<RecommendationAO> {

	@Override
	public void execute(final RecommendationAO operator,
			final TransformationConfiguration config) throws RuleException {
		final CombinedMergeFunction<ITimeInterval> metaDataMerge = new CombinedMergeFunction<ITimeInterval>();
		metaDataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
		final TITransferArea<Tuple<ITimeInterval>, Tuple<ITimeInterval>> transferFunction = new TITransferArea<>();

		int modelPort = -1;
		int elementsPort = -1;
		int recommenderAttributeIndex = -1;
		int userAttibuteIndex = -1;
		for (int i = 0; i < operator.getNumberOfInputs(); ++i) {
			final int rAI = operator.getInputSchema(i).indexOf(operator.getRecommenderAttribute());
			if(rAI != -1) {
				if(modelPort != -1) {
					throw new IllegalArgumentException("There are more than one port with a recommender attribute.");
				} else {
					modelPort = i;
					recommenderAttributeIndex = rAI;
				}
			} else {
				final int uAI = operator.getInputSchema(i).indexOf(
						operator.getUserAttribute());
				if(uAI != -1) {
					if(elementsPort != -1) {
						throw new IllegalArgumentException("There are more than one port with a user attribute.");
					} else {
						elementsPort = i;
						userAttibuteIndex = uAI;
					}
				}
			}
		}

		if (modelPort == -1) {
			throw new IllegalArgumentException("Model port not found.");
		}
		if (elementsPort == -1) {
			throw new IllegalArgumentException("Elements port not found.");
		}


		final RecommendationPO<ITimeInterval> po = new RecommendationPO<ITimeInterval>(
				modelPort, recommenderAttributeIndex, elementsPort,
				userAttibuteIndex,
				operator.getNumberOfRecommendations(), metaDataMerge,
				transferFunction);

		defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(final RecommendationAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "RecommendationAO -> RecommendationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
