package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangePredictionTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IPredictionTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicaloperator.ChangePredictionTimePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TransformChangePredictionTimeRule extends AbstractTransformationRule<ChangePredictionTimeAO> {

	@Override
	public void execute(ChangePredictionTimeAO operator, TransformationConfiguration config) throws RuleException {
		RelationalExpression<IPredictionTimes> startExpression = getStartExpression(operator);
		RelationalExpression<IPredictionTimes> endExpression = getEndExpression(operator);
		defaultExecute(operator, new ChangePredictionTimePO<>(operator, startExpression, endExpression), config, true, true);
	}

	@Override
	public boolean isExecutable(ChangePredictionTimeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ChangePredictionTimeAO --> ChangePredictionTimePO";
	}

	private RelationalExpression<IPredictionTimes> getEndExpression(ChangePredictionTimeAO operator) {
		RelationalExpression<IPredictionTimes> endExpression = null;

		if (operator.getStartExpression() != null) {
			int pos = onlyOneAttributeInExpression(operator.getEndExpression(), operator.getInputSchema());

			if (pos == -1) {
				endExpression = new RelationalExpression<>(operator.getEndExpression());
				endExpression.initVars(operator.getInputSchema());
			}
		}

		return endExpression;
	}

	private RelationalExpression<IPredictionTimes> getStartExpression(ChangePredictionTimeAO operator) {
		RelationalExpression<IPredictionTimes> startExpression = null;

		if (operator.getStartExpression() != null) {
			int pos = onlyOneAttributeInExpression(operator.getStartExpression(), operator.getInputSchema());

			if (pos == -1) {
				startExpression = new RelationalExpression<>(operator.getStartExpression());
				startExpression.initVars(operator.getInputSchema());
			}
		}

		return startExpression;
	}

	/**
	 * @return The index of the one attribute, iff only one attribute is in the
	 *         expressions, and -1 otherwise
	 */
	private int onlyOneAttributeInExpression(SDFExpression expression, SDFSchema schema) {
		List<SDFAttribute> attributes = expression.getAllAttributes();

		if (expression.getMEPExpression().isVariable() && attributes.size() == 1) {

			SDFAttribute attr = schema.findAttribute(attributes.get(0).getAttributeName());
			if (attr != null) {
				return schema.indexOf(attr);
			}
		}

		return -1;
	}

}
