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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.UnionPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TUnionTIPORule extends AbstractIntervalTransformationRule<UnionAO> {

	@Override
	public void execute(UnionAO unionAO,
			TransformationConfiguration transformConfig) throws RuleException {
		UnionPO<IStreamObject<ITimeInterval>> unionPO = new UnionPO<IStreamObject<ITimeInterval>>(
				new TITransferArea<IStreamObject<ITimeInterval>, IStreamObject<ITimeInterval>>());

		defaultExecute(unionAO, unionPO, transformConfig, true, true);

		// Update schema
		if (unionAO.isUseInputPortAsOutputPort()) {
			unionPO.setUseInputPortAsOutputPort(unionAO
					.isUseInputPortAsOutputPort());
			for (int i = 0; i < unionAO.getNumberOfInputs(); i++) {
				unionPO.setOutputSchema(unionAO.getOutputSchema(i), i);
			}
		}
		
		unionPO.setDrainAtClose(unionAO.isDrainAtClose());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super UnionAO> getConditionClass() {
		return UnionAO.class;
	}

}
