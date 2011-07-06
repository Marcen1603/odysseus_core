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
package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.window.SameTimeFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSameTimestampRule extends AbstractTransformationRule<TimestampAO> {

	@Override
	public int getPriority() {
		// the priority must be higher than the priority
		// of the other timestamp rules to avoid conflicts
		return 5;
	}

	@Override
	public void execute(TimestampAO timestampAO, TransformationConfiguration transformConfig) {
		SameTimeFactory mUpdater = new SameTimeFactory();		
		MetadataUpdatePO po = new MetadataUpdatePO(mUpdater);
		po.setOutputSchema(timestampAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(timestampAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(timestampAO);		
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		// although we don't use the time interval attached to the elements
		// we need it to set the start timestamps of each element to 0.
		if(transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())){
			if(operator.isAllPhysicalInputSet() && operator.isUsingNoTime()){
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
	public Class<?> getConditionClass() {	
		return TimestampAO.class;
	}

}
