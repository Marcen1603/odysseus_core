package de.uniol.inf.is.odysseus.temporaltypes.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalSelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalSelectAORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {
		if (operator.getPredicate() instanceof RelationalExpression) {
			@SuppressWarnings("unchecked")
			RelationalExpression<IValidTime> expression = (RelationalExpression<IValidTime>) operator.getPredicate();
			TemporalRelationalExpression<IValidTime> temporalExpression = new TemporalRelationalExpression<IValidTime>(
					expression);
			TemporalSelectPO<Tuple<IValidTime>> temporalSelect = new TemporalSelectPO<>(temporalExpression);
			this.defaultExecute(operator, temporalSelect, config, true, true);
		}
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && expressionContainsTemporalAttribute(operator.getPredicate());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	private boolean expressionContainsTemporalAttribute(IPredicate<?> predicate) {
		for (SDFAttribute attribute : predicate.getAttributes()) {
			if (attribute.getDatatype() instanceof TemporalDatatype) {
				return true;
			}
		}
		return false;
	}

}
