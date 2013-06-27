/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a joinPlan of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.pubsub.transform;

import de.uniol.inf.is.odysseus.pubsub.logicaloperator.SubscribeAO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

/**
 * Transformation Class for Subscribe Operator
 * 
 * @author ChrisToenjesDeye
 *
 */
public class TSubscribeAORule extends AbstractTransformationRule<SubscribeAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(SubscribeAO subscribe,
			TransformationConfiguration config) {
		defaultExecute(
				subscribe,
				new SubscribePO(subscribe.getPredicates(), subscribe
						.getBrokerName(), subscribe.getSchema(), subscribe
						.getTopics(), subscribe.getDomain()), config, true,
				true);
	}

	@Override
	public boolean isExecutable(SubscribeAO operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SubscribeAO -> SubscribePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SubscribeAO> getConditionClass() {
		return SubscribeAO.class;
	}

}