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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.engine.TransformationExecutor;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSenderAOViewRule extends AbstractTransformationRule<SenderAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(SenderAO senderAO, TransformationConfiguration transformConfig) {
		String sinkName = senderAO.getSinkname();
		ILogicalOperator view = getDataDictionary().getSinkForTransformation(senderAO.getSinkname(), getCaller());
		ILogicalOperator cPlan = view;

		ITransformation transformation = new TransformationExecutor();

		ArrayList<IPhysicalOperator> roots = transformation.transform(cPlan, transformConfig, getCaller(), getDataDictionary());
		// Clean up physical subscription for this plan!
		cPlan.clearPhysicalSubscriptions();
		// get the first root, since this is the physical operator for the
		// passed plan and this will be the connection to the current plan.
		if (roots.get(0).isSink()) {
			ISink<?> sink = (ISink<?>) roots.get(0);

			List<SenderAO> sinkAOs = new ArrayList<SenderAO>();
			List<?> currentWM = super.getCollection();
			for (Object o : currentWM) {
				if (o instanceof SenderAO) {
					SenderAO other = (SenderAO) o;
					if (other.getSinkname().equals(senderAO.getSinkname())) {
						sinkAOs.add(other);
					}
				}
			}

			insert(sink);
			getDataDictionary().putSinkplan(sinkName, sink);
			for (SenderAO curAO : sinkAOs) {
				replace(curAO, sink, transformConfig);
				retract(curAO);
			}
		} else {
			throw new RuntimeException("Cannot transform view: Root of view plan is no source.");
		}

	}

	@Override
	public boolean isExecutable(SenderAO senderAO, TransformationConfiguration transformConfig) {
		if (getDataDictionary().getSinkplan(senderAO.getSinkname()) == null) {
			ILogicalOperator stream = getDataDictionary().getSinkForTransformation(senderAO.getSinkname(), getCaller());
			// is there a suitable stream to transform and is this a view
			if (stream != null && (senderAO.getWrapper() == null || senderAO.getWrapper().isEmpty())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Transform Sender View";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<? super SenderAO> getConditionClass() {
		return SenderAO.class;
	}

}
