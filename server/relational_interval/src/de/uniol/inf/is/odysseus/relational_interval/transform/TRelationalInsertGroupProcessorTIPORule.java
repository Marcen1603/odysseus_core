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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.AbstractPartitionedWindowTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalInsertGroupProcessorTIPORule
		extends
		AbstractTransformationRule<AbstractPartitionedWindowTIPO<IStreamObject<ITimeInterval>>> {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(
			AbstractPartitionedWindowTIPO<IStreamObject<ITimeInterval>> windowPO,
			TransformationConfiguration transformConfig) throws RuleException {
		@SuppressWarnings("rawtypes")
		RelationalGroupProcessor r = new RelationalGroupProcessor(
				windowPO.getInputSchema(), windowPO.getOutputSchema(), windowPO.getPartitionedBy(),
				null, false);
		windowPO.setGroupProcessor(r);
		update(windowPO);
	}

	@Override
	public boolean isExecutable(
			AbstractPartitionedWindowTIPO<IStreamObject<ITimeInterval>> operator,
			TransformationConfiguration transformConfig) {
		if (operator.getOutputSchema().getType() == Tuple.class) {
			return operator.isPartitioned();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AbstractPartitionedWindowTIPO<IStreamObject<ITimeInterval>>> getConditionClass() {
		return AbstractPartitionedWindowTIPO.class;
	}

}
