package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TSetTemporalConstraintsOnMapAORule extends TTemporalMapAORule {

	@Override
	public void execute(MapAO operator, TransformationConfiguration config) throws RuleException {
		/*
		 * Set the output schema constraints so that they contain the temporal
		 * constraint
		 */

		for (NamedExpression expression : operator.getExpressions()) {
			if (containsTemporalAttribute(expression.expression.getAllAttributes())) {
				List<SDFAttribute> attributes = operator.getOutputSchema().getAttributes().stream()
						.filter(e -> e.getAttributeName().equals(expression.name)).collect(Collectors.toList());
				for (SDFAttribute attribute : attributes) {
					for (SDFConstraint constraintToAdd : TemporalDatatype.getTemporalConstraint()) {
						attribute.addConstraint(constraintToAdd);
					}
				}
			}
		}
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration config) {
		// Only use this rule if the map has temporal expressions
		boolean hasValidTime = operator.getInputSchema().hasMetatype(IValidTime.class);
		boolean hasTemporalExpression = this.containsExpressionWithTemporalAttribute(operator.getExpressionList());
		return hasValidTime && hasTemporalExpression;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	protected boolean containsTemporalAttribute(List<SDFAttribute> attributes) {
		for (SDFAttribute attribute : attributes) {
			if (TemporalDatatype.isTemporalAttribute(attribute)) {
				return true;
			}
		}
		return false;
	}

}
