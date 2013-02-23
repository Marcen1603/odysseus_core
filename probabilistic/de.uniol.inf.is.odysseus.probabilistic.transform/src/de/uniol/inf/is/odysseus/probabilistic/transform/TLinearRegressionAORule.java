package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.LinearRegressionAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.LinearRegressionPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLinearRegressionAORule extends
		AbstractTransformationRule<LinearRegressionAO> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(LinearRegressionAO operator,
			TransformationConfiguration config) {
		IPhysicalOperator linearRegressionPO = new LinearRegressionPO<ITimeInterval>(
				operator.getInputSchema(), operator.determineDependentList(),
				operator.determineExplanatoryList());
		this.defaultExecute(operator, linearRegressionPO, config, true, true);

	}

	@Override
	public boolean isExecutable(LinearRegressionAO operator,
			TransformationConfiguration config) {
		if ((config.getDataTypes().contains(TransformUtil.DATATYPE))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "LinearRegressionAO -> LinearRegressionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super LinearRegressionAO> getConditionClass() {
		return LinearRegressionAO.class;
	}
}
