package de.uniol.inf.is.odysseus.datamining.clustering.transform;

import de.uniol.inf.is.odysseus.datamining.clustering.EuclidianDistance;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.LeaderPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLeaderAORule extends AbstractTransformationRule<LeaderAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(LeaderAO leaderAO, TransformationConfiguration config) {
		
		
		
		
		LeaderPO<?> leaderPO = new LeaderPO();
		leaderPO.setDissimilarity(new EuclidianDistance());
		leaderPO.setRestrictList(leaderAO.determineRestrictList());
		leaderPO.setOutputSchema(leaderAO.getOutputSchema());
		leaderPO.setThreshold(leaderAO.getThreshold());
		replace(leaderAO, leaderPO, config);		
		retract(leaderAO);
	}

	@Override
	public boolean isExecutable(LeaderAO operator,
			TransformationConfiguration config) {
		
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		
		return "LeaderAO -> LeaderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
