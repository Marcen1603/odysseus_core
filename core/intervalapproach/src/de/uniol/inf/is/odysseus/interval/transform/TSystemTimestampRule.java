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

import de.uniol.inf.is.odysseus.core.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.window.SystemTimeIntervalFactory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSystemTimestampRule extends AbstractTransformationRule<TimestampAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TimestampAO timestampAO, TransformationConfiguration transformConfig) {
		SystemTimeIntervalFactory<ITimeInterval, MetaAttributeContainer<ITimeInterval>> mUpdater = new SystemTimeIntervalFactory<ITimeInterval, MetaAttributeContainer<ITimeInterval>>();		
		MetadataUpdatePO<ITimeInterval, MetaAttributeContainer<ITimeInterval>> po = new MetadataUpdatePO<ITimeInterval, MetaAttributeContainer<ITimeInterval>>(mUpdater);
		defaultExecute(timestampAO, po, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet() && operator.isUsingSystemTime()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "TimestampAO -> MetadataUpdatePO(system timestamp)";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super TimestampAO> getConditionClass() {	
		return TimestampAO.class;
	}

}
