package de.uniol.inf.is.odysseus.temporaltypes.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.join.TJoinAORule;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.temporaltypes.expressions.TemporalRelationalExpression;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.sweeparea.TemporalJoinTISweepArea;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;

public class TTemporalJoinAORule extends TJoinAORule {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void setJoinPredicate(JoinTIPO joinPO, JoinAO joinAO) {
		if (joinAO.getPredicate() instanceof RelationalExpression) {
			RelationalExpression<IValidTimes> expression = (RelationalExpression<IValidTimes>) joinAO.getPredicate();
			TemporalRelationalExpression<IValidTimes> temporalExpression = new TemporalRelationalExpression<IValidTimes>(
					expression, joinAO.getBaseTimeUnit());
			joinPO.setJoinPredicate(temporalExpression);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void setSweepArea(JoinTIPO joinPO, JoinAO joinAO) {
		/*
		 * The sweep area is the place where the temporal expression is evaluated. So we
		 * only have to interchange the sweep area, not the join itself.
		 */
		TemporalJoinTISweepArea<Tuple<? extends ITimeInterval>> sweepArea1 = new TemporalJoinTISweepArea<>(
				joinAO.getBaseTimeUnit());
		TemporalJoinTISweepArea<Tuple<? extends ITimeInterval>> sweepArea2 = new TemporalJoinTISweepArea<>(
				joinAO.getBaseTimeUnit());
		ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];
		areas[0] = sweepArea1;
		areas[1] = sweepArea2;
		joinPO.setAreas(areas);
	}

	@Override
	public boolean isExecutable(JoinAO operator, TransformationConfiguration config) {
		/*
		 * Only use this rule if there is at least one temporal attribute in the
		 * predicate.
		 */
		return super.isExecutable(operator, config) && predicateContainsTemporalAttribute(operator);
	}

	/**
	 * Checks if the output schema contains a temporal
	 * 
	 * @param operator
	 * @return
	 */
	private boolean predicateContainsTemporalAttribute(JoinAO operator) {

		if (operator.getPredicate() == null) {
			return false;
		}

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

			// So let us check if our attributes in the input schema are temporal.

			// Left input port
			SDFAttribute attributeFromSchema = TemporalDatatype.getAttributeFromSchema(operator.getInputSchema(0),
					attribute);
			if (TemporalDatatype.isTemporalAttribute(attributeFromSchema)) {
				return true;
			}

			// Right input port
			attributeFromSchema = TemporalDatatype.getAttributeFromSchema(operator.getInputSchema(1), attribute);
			if (TemporalDatatype.isTemporalAttribute(attributeFromSchema)) {
				return true;
			}

		}
		return false;
	}

	@Override
	public int getPriority() {
		// The priority needs to be higher than the priority of the normal rule.
		return 10;
	}

}
