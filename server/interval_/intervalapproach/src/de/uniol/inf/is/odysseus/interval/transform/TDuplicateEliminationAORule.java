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
package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DuplicateEliminationAO;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.DuplicateEliminationTIPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 *
 */
public class TDuplicateEliminationAORule extends AbstractTransformationRule<DuplicateEliminationAO> {

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
	public void execute(DuplicateEliminationAO operator, TransformationConfiguration config) {
		DefaultTISweepArea<Tuple<ITimeInterval>> sweepArea = new DefaultTISweepArea<Tuple<ITimeInterval>>();
		DuplicateEliminationTIPO<Tuple<ITimeInterval>> po = new DuplicateEliminationTIPO<Tuple<ITimeInterval>>(sweepArea);
		po.setOutputSchema(operator.getOutputSchema());
		replace(operator, po, config);
		retract(operator);		
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(DuplicateEliminationAO operator, TransformationConfiguration config) {		
		return operator.isAllPhysicalInputSet();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "DuplicateEliminationAO -> DuplicateEliminationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super DuplicateEliminationAO> getConditionClass() {	
		return DuplicateEliminationAO.class;
	}

}
