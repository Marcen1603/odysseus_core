/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ReOrderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.ReOrderPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * @author Merlin Wasmann
 *
 */
public class TReOrderAORule extends AbstractIntervalTransformationRule<ReOrderAO> {


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(ReOrderAO operator,
			TransformationConfiguration config) throws RuleException {
		ReOrderPO orderPO = new ReOrderPO();

		orderPO.setTransferFunction(new TITransferArea());

		defaultExecute(operator, orderPO, config, true, true);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "ReOrderAO -> AssureOrderPO";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ReOrderAO> getConditionClass() {
		return ReOrderAO.class;
	}

}
