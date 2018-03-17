package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * This rule checks if there are temporal attributes in an expression. If so,
 * the output of the expression is a temporal type, too. Creates a new output
 * schema to add the constraints needed to mark attributes as temporal.
 * 
 * @author Tobias Brandt
 *
 */
public class TSetTemporalConstraintsOnMapAORule extends TTemporalMapAORule {

	@Override
	public void execute(MapAO operator, TransformationConfiguration config) throws RuleException {
		/*
		 * Create a new output schema so that the attributes contain the temporal
		 * constraint if at least one input attribute of an expression also has a
		 * temporal constraint (is a temporal type).
		 */

		// The new attributes for the new output schema
		List<SDFAttribute> newAttributes = new ArrayList<>();

		/*
		 * We need to keep track at which position we are to insert attributes which are
		 * not changed at the right position.
		 */

		for (int outputSchemaPosition = 0; outputSchemaPosition < operator.getOutputSchema()
				.size(); outputSchemaPosition++) {

			// Is this attribute made by an expression and if so, is it temporal?
			SDFAttribute currentOutputAttribute = operator.getOutputSchema().getAttribute(outputSchemaPosition);
			List<NamedExpression> expressionForAttribute = operator.getExpressions().stream()
					.filter(e -> e.name.equals(currentOutputAttribute.getAttributeName())).collect(Collectors.toList());
			if (expressionForAttribute.size() == 1
					&& containsTemporalAttribute(expressionForAttribute.get(0).expression.getAllAttributes())) {
				SDFAttribute newAttribute = addTemporalConstraintToAttribute(currentOutputAttribute);
				newAttributes.add(newAttribute);
			} else {
				// In the case that no constraints are added, simply use the old attribute
				newAttributes.add(operator.getOutputSchema().get(outputSchemaPosition));
			}
		}

		// Create and set the new schema
		SDFSchema newSchema = SDFSchemaFactory.createNewWithAttributes(operator.getOutputSchema(), newAttributes);
		operator.setOutputSchema(newSchema);
	}

	private SDFAttribute addTemporalConstraintToAttribute(SDFAttribute attribute) {
		// Copy the existing constraints from that output attribute
		List<SDFConstraint> newConstraints = new LinkedList<>(attribute.getDtConstraints());

		// Add the temporal constraints to the new constraints
		for (SDFConstraint constraintToAdd : TemporalDatatype.getTemporalConstraint()) {
			newConstraints.add(constraintToAdd);
		}
		// Create a new attribute from the old and add the new constraints
		return new SDFAttribute(attribute, newConstraints);
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

	/**
	 * Checks if at least one attribute is temporal
	 * 
	 * @param attributes
	 *            The list of attributes to check
	 * @return true, if at least one attribute is temporal, false otherwise
	 */
	protected boolean containsTemporalAttribute(List<SDFAttribute> attributes) {
		for (SDFAttribute attribute : attributes) {
			if (TemporalDatatype.isTemporalAttribute(attribute)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
