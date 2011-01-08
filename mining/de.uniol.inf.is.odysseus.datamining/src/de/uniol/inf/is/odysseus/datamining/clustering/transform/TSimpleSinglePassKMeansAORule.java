package de.uniol.inf.is.odysseus.datamining.clustering.transform;

import de.uniol.inf.is.odysseus.datamining.clustering.EuclidianDistance;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.SimpleSinglePassKMeansAO;
import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.SimpleSinglePassKMeansPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This class represents a rule to transform a logical simple single pass
 * k-means operator into a physical logical simple single pass k-means operator.
 * 
 * 
 * @author Kolja Blohm
 * 
 */
public class TSimpleSinglePassKMeansAORule extends
		AbstractTransformationRule<SimpleSinglePassKMeansAO> {

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
	public void execute(SimpleSinglePassKMeansAO simpleSinglePassKMeansAO,
			TransformationConfiguration config) {
		// create and initialize physical simple single pass k-means operator
		SimpleSinglePassKMeansPO<?> simpleSinglePassKMeansPO = new SimpleSinglePassKMeansPO();
		simpleSinglePassKMeansPO.setDissimilarity(new EuclidianDistance());
		simpleSinglePassKMeansPO.setClusterCount(simpleSinglePassKMeansAO
				.getClusterCount());
		simpleSinglePassKMeansPO.setBufferSize(simpleSinglePassKMeansAO
				.getBufferSize());
		simpleSinglePassKMeansPO.setRestrictList(simpleSinglePassKMeansAO
				.determineRestrictList());
		simpleSinglePassKMeansPO.setOutputSchema(
				simpleSinglePassKMeansAO.getOutputSchema(0), 0);
		simpleSinglePassKMeansPO.setOutputSchema(
				simpleSinglePassKMeansAO.getOutputSchema(1), 1);

		replace(simpleSinglePassKMeansAO, simpleSinglePassKMeansPO, config);
		retract(simpleSinglePassKMeansAO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(SimpleSinglePassKMeansAO operator,
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

		return "SimpleSinglePassKMeansAO -> SimpleSinglePassKMeansPO";
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
