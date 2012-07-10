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
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferedPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BlockingBufferedPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({ "rawtypes" })
public class TBufferedPipeRule extends AbstractTransformationRule<BufferAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(BufferAO algebraOp, TransformationConfiguration trafo) {
		BufferedPipe po = null;
		if (algebraOp.getMaxBufferSize() > 0) {
			po = new BlockingBufferedPipe(algebraOp.getMaxBufferSize());
		} else {
			po = new BufferedPipe();
		}
		po.setBufferName(algebraOp.getBuffername());
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper()
				.replace(algebraOp, po);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(algebraOp);

	}

	@Override
	public boolean isExecutable(BufferAO operator,
			TransformationConfiguration transformConfig) {
		if (operator.isAllPhysicalInputSet()) {
			if (operator.getType() == null
					|| operator.getType().equalsIgnoreCase("Normal")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "BufferAO -> BufferedPipe";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
