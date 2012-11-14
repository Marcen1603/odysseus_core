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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TIInputStreamSyncArea;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClassificationAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.ClassificationTreePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 * 
 */
public class TClassificationAORule extends AbstractTransformationRule<ClassificationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ClassificationAO operator, TransformationConfiguration config) {
		TIInputStreamSyncArea<Tuple<ITimeInterval>> inputArea = new TIInputStreamSyncArea<Tuple<ITimeInterval>>(operator.getSubscribedToSource().size());
		ClassificationTreePO<ITimeInterval> po = new ClassificationTreePO<ITimeInterval>(operator.getInputSchema(0), inputArea);
		defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(ClassificationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClassificationAO -> ClassificationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}