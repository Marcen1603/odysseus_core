package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.function.TemporalFunction;
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
			TemporalSelectPO<Tuple<IValidTimes>> temporalSelect = null;
			if (expression.getExpression().getMEPExpression() instanceof TemporalFunction) {
				temporalSelect = new TemporalSelectPO<>(expression);
			} else {
				TemporalRelationalExpression<IValidTimes> temporalExpression = new TemporalRelationalExpression<IValidTimes>(
						expression);
				temporalSelect = new TemporalSelectPO<>(temporalExpression);
			}
			this.defaultExecute(operator, temporalSelect, config, true, true);
		}
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration config) {
		/*
		 * Only use this rule if there is at least one temporal attribute in the
		 * predicate.
		 */
		return operator.isAllPhysicalInputSet() && predicateContainsTemporalAttribute(operator);
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
	 * Checks if the output schema contains a temporal
	 * 
	 * @param operator
	 * @return
	 */
	private boolean predicateContainsTemporalAttribute(SelectAO operator) {

		/*
		 * Loop through all attributes of the predicates to compare them to the input
		 * schema. The input schema contains the info about temporal attributes.
		 */
		for (SDFAttribute attribute : operator.getPredicate().getAttributes()) {

			/*
			 * Maybe the attribute in the predicate already tells us if its temporal, but
			 * probably the info cannot be found there.
			 */
			if (TemporalDatatype.isTemporalAttribute(attribute)) {
				return true;
			}

			/*
			 * So let us check if our attributes in the input schema are temporal. If the
			 * URI of our attribute and one attribute from the input schema are equal, we
			 * have a match.
			 */
			List<SDFAttribute> attributes = operator.getInputSchema().getAttributes().stream()
					.filter(e -> (e.getURI().equals(attribute.getURI()))).collect(Collectors.toList());

			// Check for all matches if they are temporal
			for (SDFAttribute attr : attributes) {
				if (TemporalDatatype.isTemporalAttribute(attr)) {
					return true;
				}
			}
		}
		return false;
	}

}
