package de.uniol.inf.is.odysseus.datamining.clustering.transform;

import de.uniol.inf.is.odysseus.datamining.clustering.EuclidianDistance;
import de.uniol.inf.is.odysseus.datamining.clustering.logicaloperator.SimpleSinglePassKMeansAO;
import de.uniol.inf.is.odysseus.datamining.clustering.physicaloperator.SimpleSinglePassKMeansPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSimpleSinglePassKMeansAORule extends AbstractTransformationRule<SimpleSinglePassKMeansAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(SimpleSinglePassKMeansAO simpleSinglePassKMeansAO, TransformationConfiguration config) {
		
		
		
		
		SimpleSinglePassKMeansPO<?> simpleSinglePassKMeansPO = new SimpleSinglePassKMeansPO();
		simpleSinglePassKMeansPO.setDissimilarity(new EuclidianDistance());
		simpleSinglePassKMeansPO.setClusterCount(simpleSinglePassKMeansAO.getClusterCount());
		simpleSinglePassKMeansPO.setBufferSize(simpleSinglePassKMeansAO.getBufferSize());
		simpleSinglePassKMeansPO.setRestrictList(simpleSinglePassKMeansAO.determineRestrictList());
		simpleSinglePassKMeansPO.setOutputSchema(simpleSinglePassKMeansAO.getOutputSchema(0), 0);
		simpleSinglePassKMeansPO.setOutputSchema(simpleSinglePassKMeansAO.getOutputSchema(1), 1);
		
		replace(simpleSinglePassKMeansAO, simpleSinglePassKMeansPO, config);		
		retract(simpleSinglePassKMeansAO);
	}

	@Override
	public boolean isExecutable(SimpleSinglePassKMeansAO operator,
			TransformationConfiguration config) {
		
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		
		return "SimpleSinglePassKMeansAO -> SimpleSinglePassKMeansPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
