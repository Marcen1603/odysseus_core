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
package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.classification.WekaClassificationLearner;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClassificationLearnAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClassificationLearnPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 * 
 */
public class TClassificationLearnAORule extends AbstractTransformationRule<ClassificationLearnAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ClassificationLearnAO operator, TransformationConfiguration config) {
		IClassificationLearner<ITimeInterval> learner = null;
		if (operator.getLearner().equalsIgnoreCase("weka")) {
			learner = new WekaClassificationLearner<ITimeInterval>();
			learner.setOptions(operator.getOptions());
		}
		// extend with others...
		learner.init(operator.getInputSchema(0), operator.getClassAttribute(), operator.getNominals());
		ClassificationLearnPO<ITimeInterval> po = new ClassificationLearnPO<>(learner);
		defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(ClassificationLearnAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClassificationLearnAO -> ClassificationLearnPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}