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

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator.StatelessCorrectionAO;
import de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator.StatelessCorrctionPO;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStatelessCorrectionAORule extends AbstractTransformationRule<StatelessCorrectionAO<Tuple<IMiningMetadata>>> {

	@Override
	public int getPriority() {	
		return 0;
	}

	@Override
	public void execute(StatelessCorrectionAO<Tuple<IMiningMetadata>> correctAO, TransformationConfiguration config) {		
		StatelessCorrctionPO<?> correctPO = new StatelessCorrctionPO<Tuple<IMiningMetadata>>(correctAO.getCorrections());
		correctPO.setOutputSchema(correctAO.getOutputSchema());
		replace(correctAO, correctPO, config);		
		retract(correctAO);
		
	}

	@Override
	public boolean isExecutable(StatelessCorrectionAO<Tuple<IMiningMetadata>> operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "StatelessCorrectionAO -> StatelessCorrectionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
