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
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.mining.MiningAlgorithmRegistry;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClassificationLearnAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClassificationLearnPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(ClassificationLearnAO operator, TransformationConfiguration config) throws RuleException {
		IClassificationLearner<ITimeInterval> learner = MiningAlgorithmRegistry.getInstance().createClassificationLearner(operator.getLearner());		
		learner.init(operator.getAlgorithm(), operator.getInputSchema(0), operator.getClassAttribute(), operator.getNominals());
		learner.setOptions(operator.getOptionsMap());
		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		// TODO:
		// ITimeIntervalSweepArea<Tuple<ITimeInterval>> sa = new DefaultTISweepArea<Tuple<M>>(new FastLinkedList<Tuple<ITimeInterval>>());
		ClassificationLearnPO<ITimeInterval> po = new ClassificationLearnPO<>(learner, sa);
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