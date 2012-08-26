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
package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.logicaloperator.StatCounterAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.StatCounterPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 *
 */
public class TStatCounterAORule extends AbstractTransformationRule<StatCounterAO>{

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
	@Override
	public void execute(StatCounterAO operator, TransformationConfiguration config) {
		StatCounterPO<ITimeInterval> po = new StatCounterPO<ITimeInterval>(operator.getOutputeach());
		po.setOutputSchema(operator.getOutputSchema(0), 0);		
		replace(operator, po, config);
		retract(operator);
		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(StatCounterAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "StatCounterAO -> StatCounterPO";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
