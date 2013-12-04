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
package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssureOrderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AssureOrderPO;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Merlin Wasmann
 *
 */
public class TAssureOrderAORule extends AbstractTransformationRule<AssureOrderAO> {

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(AssureOrderAO operator,
			TransformationConfiguration config) {
		AssureOrderPO orderPO = new AssureOrderPO();
		
		orderPO.setTransferFunction(new TITransferArea());
		
		defaultExecute(operator, orderPO, config, true, true);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(AssureOrderAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName()); 
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "AssureOrderAO -> AssureOrderPO";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super AssureOrderAO> getConditionClass() {
		return AssureOrderAO.class;
	}

}
