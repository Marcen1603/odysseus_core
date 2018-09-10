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
package de.uniol.inf.is.odysseus.mining.evaluation.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.evaluation.logicaloperator.ClassificationEvaluateAO;
import de.uniol.inf.is.odysseus.mining.evaluation.physicaloperator.ClassificationEvaluatePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Stephan Wessels
 * 
 */
public class TClassificationEvaluateAORule extends AbstractTransformationRule<ClassificationEvaluateAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(ClassificationEvaluateAO operator, TransformationConfiguration config) throws RuleException {
		int positionOfClassifier = operator.getInputSchema(0).indexOf(operator.getClassifier());
		if (positionOfClassifier == -1) {
			throw new IllegalArgumentException("the classifier attribute must be in port 0!");
		}
		int positionOfClass = operator.getInputSchema(1).indexOf(operator.getClassAttribute());
		if (positionOfClass == -1) {
			throw new IllegalArgumentException("the class attribute must be in port 1!");
		}
		ITimeIntervalSweepArea sa1;
		try {
			sa1 = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		ITimeIntervalSweepArea sa2;
		try {
			sa2 = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		ClassificationEvaluatePO<ITimeInterval> po = new ClassificationEvaluatePO<ITimeInterval>(positionOfClassifier, positionOfClass, operator.getClassAttribute(), operator.getNominals(), operator.getMetrics(), operator.getFading(), sa1, sa2);
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(ClassificationEvaluateAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClassificationEvaluateAO -> ClassificationEvaluatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}