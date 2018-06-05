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
package de.uniol.inf.is.odysseus.rcp.evaluation.processing.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.logicaloperator.MeasureThroughputAO;
import de.uniol.inf.is.odysseus.rcp.evaluation.processing.physicaloperator.MeasureThroughputPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 * 
 */
public class TMeasureThroughputAORule extends AbstractTransformationRule<MeasureThroughputAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MeasureThroughputAO operator, TransformationConfiguration config) throws RuleException {
		MeasureThroughputPO<IStreamObject<ITimeInterval>> po = new MeasureThroughputPO<IStreamObject<ITimeInterval>>(operator.getEach(), operator.getFilename(), operator.isActive(), operator.isDump());
		defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(MeasureThroughputAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "MeasureThroughputAO -> MeasureThroughputPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}