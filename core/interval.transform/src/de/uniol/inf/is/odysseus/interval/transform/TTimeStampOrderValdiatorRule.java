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

import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TimeStampOrderValidatorTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.TimeStampOrderValidatorAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTimeStampOrderValdiatorRule extends
		AbstractTransformationRule<TimeStampOrderValidatorAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TimeStampOrderValidatorAO operator,
			TransformationConfiguration config) {		
		TimeStampOrderValidatorTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>> po = new TimeStampOrderValidatorTIPO<ITimeInterval, MetaAttributeContainer<ITimeInterval>>();
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(TimeStampOrderValidatorAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TimeStampOrderValidatorAO --> TimeStampOrderValidatorTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super TimeStampOrderValidatorAO>  getConditionClass() {	
		return TimeStampOrderValidatorAO.class;
	}

}
