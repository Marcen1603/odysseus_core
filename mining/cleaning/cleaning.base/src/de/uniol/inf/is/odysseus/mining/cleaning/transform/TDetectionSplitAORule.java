/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.transform;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.DetectionSplitAO;
import de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator.DetectionSplitPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDetectionSplitAORule extends AbstractTransformationRule<DetectionSplitAO<RelationalTuple<IMetaAttribute>>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(DetectionSplitAO<RelationalTuple<IMetaAttribute>> detectAO, TransformationConfiguration config) {
		DetectionSplitPO<?> detectPO = new DetectionSplitPO<RelationalTuple<IMetaAttribute>>(detectAO.getDetections());
		detectPO.setOutputSchema(detectPO.getOutputSchema());
		replace(detectAO, detectPO, config);		
		retract(detectAO);
		
	}

	@Override
	public boolean isExecutable(DetectionSplitAO<RelationalTuple<IMetaAttribute>> operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DetectionSplitAO -> DetectionSplitPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
