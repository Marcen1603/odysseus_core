/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.mining.clustering.transform;

import de.uniol.inf.is.odysseus.mining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.mining.clustering.physicaloperator.LeaderPO;
import de.uniol.inf.is.odysseus.mining.distance.EuclidianDistance;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This class represents a rule to transform a logical leader operator into a
 * physical leader operator.
 * 
 * 
 * @author Kolja Blohm
 * 
 */
public class TLeaderAORule extends AbstractTransformationRule<LeaderAO> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(LeaderAO leaderAO, TransformationConfiguration config) {
		// create and initialize physical leader operator
		LeaderPO<?> leaderPO = new LeaderPO();
		leaderPO.setDissimilarity(new EuclidianDistance());
		leaderPO.setRestrictList(leaderAO.determineRestrictList());
		leaderPO.setOutputSchema(leaderAO.getOutputSchema(0), 0);
		leaderPO.setOutputSchema(leaderAO.getOutputSchema(1), 1);
		leaderPO.setThreshold(leaderAO.getThreshold());
		replace(leaderAO, leaderPO, config);
		retract(leaderAO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(LeaderAO operator,
			TransformationConfiguration config) {

		return operator.isAllPhysicalInputSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {

		return "LeaderAO -> LeaderPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
