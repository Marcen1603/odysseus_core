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
package de.uniol.inf.is.odysseus.interval.transform.window;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.window.SlidingPeriodicWindowTIPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSlidingPeriodicWindowTIPORule extends
		AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public void execute(WindowAO windowAO,
			TransformationConfiguration transformConfig) {
		SlidingPeriodicWindowTIPO windowPO = new SlidingPeriodicWindowTIPO(
				windowAO);
		defaultExecute(windowAO, windowPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(WindowAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getMetaTypes().contains(
				ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				WindowType windowType = operator.getWindowType();
				if (windowType != WindowType.UNBOUNDED
						&& operator.getWindowSlide() > 0) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> SlidingPeriodicWindowTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super WindowAO> getConditionClass() {	
		return WindowAO.class;
	}

}
