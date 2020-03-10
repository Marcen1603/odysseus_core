/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO2;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalHeartbeatPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class THeartbeatAO2Rule extends AbstractRelationalIntervalTransformationRule<HeartbeatAO2> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final HeartbeatAO2 operator, final TransformationConfiguration config) throws RuleException {
		RelationalExpression<ITimeInterval> timeExpression = new RelationalExpression<ITimeInterval>(
				operator.getTimeExpression());
		timeExpression.initVars(operator.getInputSchema());

		RelationalExpression<ITimeInterval> createWhenCondition = null;
		if (operator.getFireOn() != null) {
			createWhenCondition = new RelationalExpression<>(operator.getFireOn());
			createWhenCondition.initVars(operator.getInputSchema());
		}

		final RelationalHeartbeatPO<ITimeInterval> po = new RelationalHeartbeatPO<ITimeInterval>(timeExpression,
				createWhenCondition, operator.isCreateOnHeartbeat());

		defaultExecute(operator, po, config, true, true);
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
	public Class<? super HeartbeatAO2> getConditionClass() {
		return HeartbeatAO2.class;
	}

}
