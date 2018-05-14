package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
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
		 * Loop through the output schema. In case that there is an expression with
		 * temporal output (has temporal input), copy it to the output attribute.
		 * Attention: it is necessary to loop through the output schema, not the
		 * expressions, because "keepInput" could lead to a different output schema than
		 * the only expressions would create.
		 */
		for (int outputSchemaPosition = 0; outputSchemaPosition < operator.getOutputSchema()
				.size(); outputSchemaPosition++) {

			// Is this attribute made by an expression and if so, is it temporal?
			SDFAttribute currentOutputAttribute = operator.getOutputSchema().getAttribute(outputSchemaPosition);
			List<NamedExpression> expressionForAttribute = new ArrayList<>();
			for (NamedExpression namedExpression : operator.getExpressions()) {
				String name = namedExpression.name;
				if (name.isEmpty()) {
					name = namedExpression.expression.getExpressionString();
				}
				if (currentOutputAttribute.getAttributeName().equals(name)) {
					expressionForAttribute.add(namedExpression);
					break;
				}
			}

			// Names should be unique, so at maximum one result
			if (expressionForAttribute.size() == 1 && containsTemporalAttribute(
					expressionForAttribute.get(0).expression.getAllAttributes(), operator.getInputSchema())) {
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

	/**
	 * Adds a temporal constraint to the given attribute
	 * 
	 * @param attribute
	 *            The attribute to add the temporal constraint to
	 * @return A new attribute with the temporal constraint
	 */
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

	/**
	 * Checks if at least one attribute is temporal
	 * 
	 * @param attributes
	 *            The list of attributes to check
	 * @return true, if at least one attribute is temporal, false otherwise
	 */
	protected boolean containsTemporalAttribute(List<SDFAttribute> attributes, SDFSchema inputSchema) {
		for (SDFAttribute attribute : attributes) {

			SDFAttribute attributeFromSchema = getAttributeFromSchema(inputSchema, attribute);
			if (TemporalDatatype.isTemporalAttribute(attributeFromSchema)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The search needs to be done on the input schema, because the temporal
	 * constraints are added to the input schema, not to the attributes in the
	 * expression. That's why this method searches for the same attribute in the
	 * schema.
	 */
	protected SDFAttribute getAttributeFromSchema(SDFSchema inputSchema, SDFAttribute attributeToSearch) {
		for (SDFAttribute attribute : inputSchema.getAttributes()) {
			if (attribute.getAttributeName().equals(attributeToSearch.getAttributeName())) {
				return attribute;
			}
		}
		return attributeToSearch;
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration config) {
		// Only use this rule if the map has temporal expressions
		boolean hasValidTime = operator.getInputSchema().hasMetatype(IValidTimes.class);
		boolean hasTemporalExpression = this.containsExpressionWithTemporalAttribute(operator.getExpressionList(),
				operator.getInputSchema());
		return hasValidTime && hasTemporalExpression;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public int getPriority() {
		return 1;
	}

}
