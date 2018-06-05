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
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.TrajectoryCompareAO;
import de.uniol.inf.is.odysseus.trajectory.physicaloperator.TrajectoryComparePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This Rule creates a <tt>TrajectoryComparePO</tt> for a <tt>TrajectoryCompareAO</tt>.
 * 
 * @author marcus
 *
 */
public class TTrajectoryCompareAORule extends AbstractTransformationRule<TrajectoryCompareAO> {
	
	 @Override
	 public int getPriority() {
		 return 0;
	 }
	 
	@Override
	 public void execute(final TrajectoryCompareAO trajectoryAO, final TransformationConfiguration config) {
		 
		 this.defaultExecute(
				 trajectoryAO, 
				 new TrajectoryComparePO<Tuple<ITimeInterval>>(
						 trajectoryAO.getK(),
						 trajectoryAO.getQueryTrajectory().getAbsolutePath(),
						 trajectoryAO.getUtmZone(),
						 trajectoryAO.getLambda(),
						 trajectoryAO.getAlgorithm(),
						 trajectoryAO.getTextualAttr(),
						 trajectoryAO.getOptionsMap()),
				 config, 
				 true,
				 true
		 );
	 }
	 
	 @Override
	 public boolean isExecutable(final TrajectoryCompareAO operator, final TransformationConfiguration transformConfig) {
		 return operator.isAllPhysicalInputSet();
	 }
	 
	 @Override
	 public String getName() {
		 return "TrajectoryCompareAO -> TrajectoryComparePO";
	 }
	  
	 @Override
	 public IRuleFlowGroup getRuleFlowGroup() {
		 return TransformRuleFlowGroup.TRANSFORMATION;
	 }
}
