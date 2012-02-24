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
package de.uniol.inf.is.odysseus.mining.classification.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.classification.logicaloperator.HoeffdingTreeAO;
import de.uniol.inf.is.odysseus.mining.classification.model.AbstractAttributeEvaluationMeasure;
import de.uniol.inf.is.odysseus.mining.classification.model.GiniIndex;
import de.uniol.inf.is.odysseus.mining.classification.model.InformationGain;
import de.uniol.inf.is.odysseus.mining.classification.physicaloperator.HoeffdingTreePO;
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
