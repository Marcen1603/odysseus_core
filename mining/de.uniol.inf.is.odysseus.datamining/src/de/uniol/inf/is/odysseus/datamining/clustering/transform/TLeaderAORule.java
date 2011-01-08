package de.uniol.inf.is.odysseus.datamining.clustering.transform;

import de.uniol.inf.is.odysseus.datamining.clustering.EuclidianDistance;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.LeaderAO;
import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.LeaderPO;
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
