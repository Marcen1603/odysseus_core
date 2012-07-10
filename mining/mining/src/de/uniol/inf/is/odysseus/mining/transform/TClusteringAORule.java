/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
/** Copyright 2012 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.logicaloperator.ClusteringAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.WekaClusteringPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen
 * Created at: 14.05.2012
 */
public class TClusteringAORule extends AbstractTransformationRule<ClusteringAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ClusteringAO operator, TransformationConfiguration config) {
		//ClusteringPO<ITimeInterval> po = new ClusteringPO<ITimeInterval>(operator.getClusterer(), operator.getAttributePositions());
		WekaClusteringPO<ITimeInterval> po = new WekaClusteringPO<ITimeInterval>(operator.getClusterer(), operator.getAttributePositions(), operator.getOptions());
		po.setOutputSchema(operator.getOutputSchema());
		replace(operator, po, config);
		retract(operator);
	}

	@Override
	public boolean isExecutable(ClusteringAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ClusteringAO -> ClusteringPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
