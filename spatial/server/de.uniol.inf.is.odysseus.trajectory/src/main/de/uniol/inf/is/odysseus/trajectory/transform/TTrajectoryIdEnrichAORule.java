/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryIdEnrichAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.TrajectoryIdEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This Rule creates a <tt>TrajectoryIdEnrichPO</tt> for a <tt>TrajectoryIdEnrichAO</tt>.
 * 
 * @author marcus
 *
 */
public class TTrajectoryIdEnrichAORule extends AbstractTransformationRule<TrajectoryIdEnrichAO> {

	@Override
	public void execute(final TrajectoryIdEnrichAO operator,
			final TransformationConfiguration config) throws RuleException {
		
		this.defaultExecute(operator, new TrajectoryIdEnrichPO<Tuple<ITimeInterval>>(), config, true, true);
	}

	@Override
	public boolean isExecutable(final TrajectoryIdEnrichAO operator,
			final TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "TrajectoryIdEnricherAO -> TrajectoryIdEnricherPO";
	}

}
