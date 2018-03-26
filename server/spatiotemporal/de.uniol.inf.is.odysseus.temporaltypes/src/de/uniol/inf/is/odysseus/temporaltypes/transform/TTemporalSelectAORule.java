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
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalSelectPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalSelectAORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public void execute(SelectAO operator, TransformationConfiguration config) throws RuleException {

		/*
		 * The SelectAO returns a predicate, but due to the implementation history, it
		 * contains an expression. We can use this and make a temporal expression from
		 * it if necessary.
		 */
		if (operator.getPredicate() instanceof RelationalExpression) {
			@SuppressWarnings("unchecked")
			RelationalExpression<IValidTimes> expression = (RelationalExpression<IValidTimes>) operator.getPredicate();
			TemporalRelationalExpression<IValidTimes> temporalExpression = new TemporalRelationalExpression<IValidTimes>(
					expression);
			TemporalSelectPO<Tuple<IValidTimes>> temporalSelect = new TemporalSelectPO<>(temporalExpression);
			this.defaultExecute(operator, temporalSelect, config, true, true);
		}
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		/*
		 * Only use this rule if there is at least one temporal attribute in the
		 * predicate.
		 */
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

	/**
	 * Checks if the expression contains at least one temporal attribute
	 * 
	 * @param predicate
	 *            The expression / predicate to test
	 * @return True, if it contains at least one temporal attribute, false otherwise
	 */
	private boolean expressionContainsTemporalAttribute(IPredicate<?> predicate) {
		for (SDFAttribute attribute : predicate.getAttributes()) {
			if (attribute.getDatatype() instanceof TemporalDatatype) {
				return true;
			}
		}
		return false;
	}

}
