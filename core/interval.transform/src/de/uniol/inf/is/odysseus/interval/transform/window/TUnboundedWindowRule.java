/** Copyright [2011] [The Odysseus Team]
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

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.window.UnboundedWindowTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnboundedWindowRule extends AbstractTransformationRule<WindowAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(WindowAO windowAO, TransformationConfiguration transformConfig) {
		UnboundedWindowTIPO window = new UnboundedWindowTIPO(windowAO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(windowAO, window);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(windowAO);
	}

	@Override
	public boolean isExecutable(WindowAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
			if (operator.isAllPhysicalInputSet()) {
				if (operator.getWindowType() == WindowType.UNBOUNDED) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "WindowAO -> Unbounded Window";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return WindowAO.class;
	}

}
