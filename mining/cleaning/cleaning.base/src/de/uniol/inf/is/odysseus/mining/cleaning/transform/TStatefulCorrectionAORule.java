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

import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatefulCorrectionAO;
import de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator.StatefulCorrectionPO;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningTimeIntervall;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public class TStatefulCorrectionAORule extends AbstractTransformationRule<StatefulCorrectionAO<IMetaAttributeContainer<IMiningTimeIntervall>>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(StatefulCorrectionAO<IMetaAttributeContainer<IMiningTimeIntervall>> correctAO, TransformationConfiguration config) {
		StatefulCorrectionPO<IMiningTimeIntervall, IMetaAttributeContainer<IMiningTimeIntervall>> correctPO = new StatefulCorrectionPO<IMiningTimeIntervall,IMetaAttributeContainer<IMiningTimeIntervall>>(correctAO.getCorrections());
		correctPO.setOutputSchema(correctAO.getOutputSchema());
		correctPO.setInputSchemas(correctAO.getInputSchema(0), correctAO.getInputSchema(1));
		replace(correctAO, correctPO, config);		
		retract(correctAO);		
	}

	@Override
	public boolean isExecutable(StatefulCorrectionAO<IMetaAttributeContainer<IMiningTimeIntervall>> operator, TransformationConfiguration config) {
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
