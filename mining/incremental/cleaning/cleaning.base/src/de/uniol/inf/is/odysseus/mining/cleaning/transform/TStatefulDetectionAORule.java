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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatefulDetectionAO;
import de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator.StatefulDetectionPO;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningTimeIntervall;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 07.07.2011
 */
public class TStatefulDetectionAORule extends AbstractTransformationRule<StatefulDetectionAO<IMetaAttributeContainer<IMiningTimeIntervall>>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(StatefulDetectionAO<IMetaAttributeContainer<IMiningTimeIntervall>> detectAO, TransformationConfiguration config) {
		StatefulDetectionPO<IMiningTimeIntervall, IMetaAttributeContainer<IMiningTimeIntervall>> detectPO = new StatefulDetectionPO<IMiningTimeIntervall,IMetaAttributeContainer<IMiningTimeIntervall>>(detectAO.getDetections());
		detectPO.setOutputSchema(detectAO.getOutputSchema());
		detectPO.setInputSchemas(detectAO.getInputSchema(0), detectAO.getInputSchema(1));
		replace(detectAO, detectPO, config);		
		retract(detectAO);		
	}

	@Override
	public boolean isExecutable(StatefulDetectionAO<IMetaAttributeContainer<IMiningTimeIntervall>> operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "StatefulDetectionAO -> StatefulDetectionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
