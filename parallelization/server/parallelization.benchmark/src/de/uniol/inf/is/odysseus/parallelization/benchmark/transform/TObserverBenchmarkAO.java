/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.benchmark.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.parallelization.benchmark.logicaloperator.ObserverBenchmarkAO;
import de.uniol.inf.is.odysseus.parallelization.benchmark.physicaloperator.ObserverBenchmarkPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation for ObserverBenchmark
 * @author ChrisToenjesDeye
 *
 */
public class TObserverBenchmarkAO extends
		AbstractTransformationRule<ObserverBenchmarkAO> {

	@Override
	public void execute(ObserverBenchmarkAO operator,
			TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new ObserverBenchmarkPO<IStreamObject<?>>(),
				config, true, true);
	}

	@Override
	public boolean isExecutable(ObserverBenchmarkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ObserverBenchmarkAO -> ObserverBenchmarkPO";
	}
}
