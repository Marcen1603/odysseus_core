package de.uniol.inf.is.odysseus.datamining.classification.transform;

import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.ClassifyAO;
import de.uniol.inf.is.odysseus.datamining.classification.physicaloperator.ClassifyPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TClassifyAORule extends
		AbstractTransformationRule<ClassifyAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isExecutable(ClassifyAO operator,
			TransformationConfiguration config) {

		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {

		return "ClassifyAO -> ClassifyPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {

		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@SuppressWarnings({ "rawtypes"})
	@Override
	public void execute(ClassifyAO classifyAO,
			TransformationConfiguration config) {

		ClassifyPO<?> classifyPO = new ClassifyPO();
		classifyPO
				.setRestrictList(classifyAO.determineRestrictList());
		classifyPO.setLabelPosition(classifyAO.getLabelPosition());
		classifyPO.setOutputSchema(classifyAO.getOutputSchema(0), 0);
		replace(classifyAO, classifyPO, config);
		retract(classifyAO);

	}

}
