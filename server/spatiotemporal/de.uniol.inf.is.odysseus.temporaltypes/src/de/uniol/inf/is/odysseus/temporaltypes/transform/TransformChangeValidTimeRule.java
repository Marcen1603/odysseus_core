package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangeValidTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicaloperator.ChangeValidTimePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TransformChangeValidTimeRule extends AbstractTransformationRule<ChangeValidTimeAO> {

	@Override
	public void execute(ChangeValidTimeAO operator, TransformationConfiguration config) throws RuleException {
		RelationalExpression<IValidTimes> startExpression = getStartExpression(operator);
		RelationalExpression<IValidTimes> endExpression = getEndExpression(operator);
		defaultExecute(operator, new ChangeValidTimePO<>(operator, startExpression, endExpression), config, true, true);
	}

	@Override
	public boolean isExecutable(ChangeValidTimeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public String getName() {
		return "ChangeValidTimeAO --> ChangeValidTimePO";
	}

	private RelationalExpression<IValidTimes> getEndExpression(ChangeValidTimeAO operator) {
		RelationalExpression<IValidTimes> endExpression = null;

		if (operator.getStartExpression() != null) {
			int pos = onlyOneAttributeInExpression(operator.getEndExpression(), operator.getInputSchema());

			if (pos == -1) {
				endExpression = new RelationalExpression<>(operator.getEndExpression());
				endExpression.initVars(operator.getInputSchema());
			}
		}

		return endExpression;
	}

	private RelationalExpression<IValidTimes> getStartExpression(ChangeValidTimeAO operator) {
		RelationalExpression<IValidTimes> startExpression = null;

		if (operator.getStartExpression() != null) {
			int pos = onlyOneAttributeInExpression(operator.getStartExpression(), operator.getInputSchema());

			if (pos == -1) {
				startExpression = new RelationalExpression<>(operator.getStartExpression());
				startExpression.initVars(operator.getInputSchema());
			}
		}

		return startExpression;
	}

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
