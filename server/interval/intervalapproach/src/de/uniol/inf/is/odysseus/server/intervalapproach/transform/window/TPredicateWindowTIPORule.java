/********************************************************************************** 
  * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform.window;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.PredicateWindowTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPredicateWindowTIPORule extends
		AbstractTransformationRule<AbstractWindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(AbstractWindowAO operator, TransformationConfiguration config) {
		PredicateWindowTIPO window = new PredicateWindowTIPO(operator.getStartCondition(), operator.getEndCondition(), operator.getWindowSize(), operator.isSameStarttime(), operator.getTimeUnit());
		defaultExecute(operator, window, config, true, true);
	}

	@Override
	public boolean isExecutable(AbstractWindowAO operator,
			TransformationConfiguration config) {
		if (operator.getWindowType() == WindowType.PREDICATE) {
			if (operator.isAllPhysicalInputSet()) {
		if (config.getMetaTypes().contains(
				ITimeInterval.class.getCanonicalName())) {
			
				
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO --> PredicateWindow";
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
