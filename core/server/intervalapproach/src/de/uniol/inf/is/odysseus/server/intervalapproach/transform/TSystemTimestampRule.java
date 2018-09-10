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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SystemTimeIntervalFactory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TSystemTimestampRule extends AbstractIntervalTransformationRule<TimestampAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(TimestampAO timestampAO, TransformationConfiguration transformConfig) throws RuleException {
		if(timestampAO.hasEndTimestampExpression() || timestampAO.hasStartTimestampExpression()){
			throw new TransformationException("You cannot use start and end parameters for system time processing");
		}
		SystemTimeIntervalFactory<ITimeInterval, IStreamObject<ITimeInterval>> mUpdater = new SystemTimeIntervalFactory<ITimeInterval, IStreamObject<ITimeInterval>>();		
		mUpdater.clearEnd(timestampAO.isClearEnd());
		// two case:
		// 1) the input operator can process meta data updates
		if (timestampAO.getPhysInputPOs().size() == 1 && timestampAO.getPhysInputOperators().get(0) instanceof IMetadataInitializer){
			IPhysicalOperator source = timestampAO.getPhysInputOperators().get(0);
			((IMetadataInitializer) source).addMetadataUpdater(mUpdater);
			// Use the existing physical operator as replacement ...
			defaultExecute(timestampAO,source, transformConfig, true, false, false);
		}else{
			MetadataUpdatePO<ITimeInterval, IStreamObject<ITimeInterval>> po = new MetadataUpdatePO<ITimeInterval, IStreamObject<ITimeInterval>>(mUpdater);
			defaultExecute(timestampAO, po, transformConfig, true, true);
		}
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		if(super.isExecutable(operator, transformConfig)){
			return operator.isUsingSystemTime();
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
