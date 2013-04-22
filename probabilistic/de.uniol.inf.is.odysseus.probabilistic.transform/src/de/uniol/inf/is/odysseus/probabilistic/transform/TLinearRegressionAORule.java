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

package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.LinearRegressionAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.LinearRegressionPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 *
 */
public class TLinearRegressionAORule extends
		AbstractTransformationRule<LinearRegressionAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(LinearRegressionAO operator,
			TransformationConfiguration config) {
		IPhysicalOperator linearRegressionPO = new LinearRegressionPO<ITimeInterval>(
				operator.getInputSchema(), operator.determineDependentList(),
				operator.determineExplanatoryList());
		this.defaultExecute(operator, linearRegressionPO, config, true, true);
	}

	@Override
	public boolean isExecutable(LinearRegressionAO operator,
			TransformationConfiguration config) {
		if ((config.getDataTypes().contains(TransformUtil.DATATYPE))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "LinearRegressionAO -> LinearRegressionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super LinearRegressionAO> getConditionClass() {
		return LinearRegressionAO.class;
	}
}
