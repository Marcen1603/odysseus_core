package de.uniol.inf.is.odysseus.datamining.classification.transform;

import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.ClassifyAO;
import de.uniol.inf.is.odysseus.datamining.classification.physicaloperator.ClassifyPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This class defines a transformation rule to transform a logical classify
 * operator into an physical classify operator
 * 
 * @author Sven Vorlauf
 * 
 */
public class TClassifyAORule extends AbstractTransformationRule<ClassifyAO> {

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
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(ClassifyAO operator,
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

		return "ClassifyAO -> ClassifyPO";
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void execute(ClassifyAO classifyAO,
			TransformationConfiguration config) {
		// create the physical operator and set the parameters
		ClassifyPO<?> classifyPO = new ClassifyPO();
		classifyPO.setRestrictList(classifyAO.determineRestrictList());
		classifyPO.setLabelPosition(classifyAO.getLabelPosition());
		classifyPO.setOutputSchema(classifyAO.getOutputSchema(0), 0);
		replace(classifyAO, classifyPO, config);
		retract(classifyAO);

	}

}
