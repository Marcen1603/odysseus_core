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

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TApplicationTimestampRule extends AbstractTransformationRule<TimestampAO> {

	@Override 
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(TimestampAO timestampAO, TransformationConfiguration transformConfig) {
		SDFSchema schema =  timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());
		RelationalTimestampAttributeTimeIntervalMFactory mUpdater;
		if (pos >= 0){
			int posEnd = timestampAO.hasEndTimestamp() ? timestampAO.getInputSchema().indexOf(timestampAO.getEndTimestamp()) : -1;
			mUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(pos, posEnd, clearEnd, timestampAO.getDateFormat()); 
		}else{
			
			int year = schema.indexOf(timestampAO.getStartTimestampYear());
			int month = schema.indexOf(timestampAO.getStartTimestampMonth());
			int day = schema.indexOf(timestampAO.getStartTimestampDay());
			int hour = schema.indexOf(timestampAO.getStartTimestampHour());
			int minute = schema.indexOf(timestampAO.getStartTimestampMinute());
			int second = schema.indexOf(timestampAO.getStartTimestampSecond());
			int millisecond = schema.indexOf(timestampAO.getStartTimestampMillisecond());
			int factor = timestampAO.getFactor();
			mUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(year, month, day, hour, minute,second, millisecond, factor, clearEnd);
		}
		
		MetadataUpdatePO<?,?> po = new MetadataUpdatePO<ITimeInterval, Tuple<? extends ITimeInterval>>(mUpdater);
		po.setOutputSchema(timestampAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(timestampAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(timestampAO);		
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet()){
				if(!operator.isUsingSystemTime()){
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "TimestampAO -> MetadataUpdatePO(application timestamp/relational)";
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
