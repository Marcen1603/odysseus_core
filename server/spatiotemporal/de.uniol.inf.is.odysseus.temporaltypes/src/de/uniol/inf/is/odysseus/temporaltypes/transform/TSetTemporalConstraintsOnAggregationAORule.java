package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.logicaloperator.AggregationAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.aggregationfunctions.TemporalIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * Sets the temporal constraints to the output schema of an aggregation
 * operation if necessary.
 * 
 * @author Tobias Brandt
 *
 */
public class TSetTemporalConstraintsOnAggregationAORule extends TTemporalAggregationAORule {

	@Override
	public void execute(final AggregationAO operator, final TransformationConfiguration config) throws RuleException {
		updateOutputSchema(operator);
	}

	protected void updateOutputSchema(AggregationAO operator) {

		List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> temporalIncrementalFunction = getIncrementalFunction(
				operator.getAggregations());
		List<Integer> attributesToChange = collectAttributesToAddTemporalConstraint(temporalIncrementalFunction);

		// Add the temporal constraint
		List<SDFAttribute> newAttributes = createNewAttributes(attributesToChange, operator);

		// Create and set the new schema
		SDFSchema newSchema = SDFSchemaFactory.createNewWithAttributes(operator.getOutputSchema(), newAttributes);
		operator.setOutputSchema(newSchema);
	}

	/**
	 * Creates new attributes if a temporal constraint needs to be added or uses the
	 * old one if not. Can be used for a new schema.
	 */
	private List<SDFAttribute> createNewAttributes(List<Integer> attributesToChange, AggregationAO operator) {
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

			if (attributesToChange.contains(outputSchemaPosition)) {
				SDFAttribute newAttribute = addTemporalConstraintToAttribute(currentOutputAttribute);
				newAttributes.add(newAttribute);
			} else {
				// In the case that no constraints are added, simply use the old attribute
				newAttributes.add(operator.getOutputSchema().get(outputSchemaPosition));
			}
		}
		return newAttributes;
	}

	/**
	 * Collects the attributes which are affected by a temporal function
	 */
	private List<Integer> collectAttributesToAddTemporalConstraint(
			List<IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>>> temporalIncrementalFunction) {
		List<Integer> attributesToChange = new ArrayList<>();

		for (IIncrementalAggregationFunction<ITimeInterval, Tuple<ITimeInterval>> incrementalFunction : temporalIncrementalFunction) {
			/*
			 * In case that this is a temporal function, a temporal constraint needs to be
			 * added.
			 */
			if (incrementalFunction instanceof TemporalIncrementalAggregationFunction) {
				int[] temporalOutputAttributeIndices = incrementalFunction.getOutputAttributeIndices();
				for (int i = 0; i < temporalOutputAttributeIndices.length; i++) {
					// The temporal function affects certain output attributes -> remember these
					if (!attributesToChange.contains(temporalOutputAttributeIndices[i])) {
						attributesToChange.add(temporalOutputAttributeIndices[i]);
					}
				}
			}
		}
		return attributesToChange;
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

	@Override
	public boolean isExecutable(AggregationAO operator, TransformationConfiguration config) {
		// Only use this rule if the map has temporal expressions
		boolean hasValidTime = operator.getInputSchema().hasMetatype(IValidTimes.class);
		boolean inputContainsTemporal = inputSchemaContainsTemporalAttribute(operator.getInputSchema());
		return hasValidTime && inputContainsTemporal;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

}
