package de.uniol.inf.is.odysseus.datamining.classification.transform;

import de.uniol.inf.is.odysseus.datamining.classification.AbstractAttributeEvaluationMeasure;
import de.uniol.inf.is.odysseus.datamining.classification.GiniIndex;
import de.uniol.inf.is.odysseus.datamining.classification.InformationGain;
import de.uniol.inf.is.odysseus.datamining.classification.logicaloperator.HoeffdingTreeAO;
import de.uniol.inf.is.odysseus.datamining.classification.physicaloperator.HoeffdingTreePO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This class defines a transformation rule to transform a logical hoeffdingTree
 * operator operator into an physical hoeffdingTree operator
 * 
 * @author Sven Vorlauf
 * 
 */
public class THoeffdingTreeAORule extends
		AbstractTransformationRule<HoeffdingTreeAO> {

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
	public boolean isExecutable(HoeffdingTreeAO operator,
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

		return "HoeffdingTreeAO -> HoeffdingTreePO";
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(HoeffdingTreeAO hoeffdingTreeAO,
			TransformationConfiguration config) {
		// create the physical operator and set the parameters
		HoeffdingTreePO<?> hoeffdingTreePO = new HoeffdingTreePO();
		hoeffdingTreePO
				.setRestrictList(hoeffdingTreeAO.determineRestrictList());
		hoeffdingTreePO.setLabelPosition(hoeffdingTreeAO.getLabelPosition());
		hoeffdingTreePO
				.setAttributeEvaluationMeasure(createEvaluationMeasure(hoeffdingTreeAO));
		hoeffdingTreePO.setOutputSchema(hoeffdingTreeAO.getOutputSchema(0), 0);
		hoeffdingTreePO.initTree();
		replace(hoeffdingTreeAO, hoeffdingTreePO, config);
		retract(hoeffdingTreeAO);

	}

	@SuppressWarnings("rawtypes")
	private AbstractAttributeEvaluationMeasure createEvaluationMeasure(
			HoeffdingTreeAO hoeffdingTreeAO) {
		String evaluationMeasure = hoeffdingTreeAO
				.getAttributeEvaluationMeasure();
		if ("gini".equalsIgnoreCase(evaluationMeasure)
				|| "giniindex".equalsIgnoreCase(evaluationMeasure)) {
			return new GiniIndex(hoeffdingTreeAO.getProbability(), null);
		} else {
			return new InformationGain(hoeffdingTreeAO.getProbability(), null);
		}
	}

}
