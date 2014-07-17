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
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalSlidingElementWindowTIPORule extends
		AbstractRelationalIntervalTransformationRule<AbstractWindowAO> {

	@Override
	public void execute(AbstractWindowAO windowAO,
			TransformationConfiguration transformConfig) throws RuleException {
		SlidingElementWindowTIPO<Tuple<ITimeInterval>> windowPO = new SlidingElementWindowTIPO<>(
				windowAO);
		RelationalGroupProcessor<ITimeInterval> r = new RelationalGroupProcessor<>(
				windowAO.getInputSchema(), windowAO.getOutputSchema(),
				windowAO.getPartitionBy(), null, false);
		windowPO.setGroupProcessor(r);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
		insert(windowPO);
	}

	@Override
	public boolean isExecutable(AbstractWindowAO operator,
			TransformationConfiguration transformConfig) {
		if (super.isExecutable(operator, transformConfig)) {
			return operator.getWindowType() == WindowType.TUPLE
					&& operator.isPartitioned();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AbstractWindowAO> getConditionClass() {
		return AbstractWindowAO.class;
	}

}
