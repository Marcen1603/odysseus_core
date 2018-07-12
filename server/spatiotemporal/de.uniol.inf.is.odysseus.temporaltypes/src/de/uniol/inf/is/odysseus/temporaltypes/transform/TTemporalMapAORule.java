package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTimes;
import de.uniol.inf.is.odysseus.temporaltypes.physicaloperator.TemporalRelationalMapPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalDatatype;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalMapAORule extends AbstractTransformationRule<MapAO> {

	@Override
	public void execute(MapAO mapAO, TransformationConfiguration config) throws RuleException {
		int[] restrictList = SDFSchema.calcRestrictList(mapAO.getInputSchema(), mapAO.getRemoveAttributes());
		TemporalRelationalMapPO<IValidTimes> mapPO = new TemporalRelationalMapPO<>(mapAO.getInputSchema(),
				mapAO.getExpressionList().toArray(new SDFExpression[0]), mapAO.isAllowNullValue(),
				mapAO.isEvaluateOnPunctuation(), mapAO.isExpressionsUpdateable(), mapAO.isSuppressErrors(),
				mapAO.isKeepInput(), restrictList);
		defaultExecute(mapAO, mapPO, config, true, true);
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration config) {
		/*
		 * Only use this rule if there is at least one expression with a temporal
		 * attribute.
		 */
		return operator.isAllPhysicalInputSet() && this
				.containsExpressionWithTemporalAttribute(operator.getExpressionList(), operator.getInputSchema());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		// The priority needs to be higher than the priority of the normal rule.
		return 1;
	}

	/**
	 * Checks if at least one expression has a temporal attribute.
	 * 
	 * @param expressions
	 *            The expressions to test
	 * @return True, if at least one expression has a temporal attribute, false
	 *         otherwise
	 */
	protected boolean containsExpressionWithTemporalAttribute(List<SDFExpression> expressions, SDFSchema inputSchema) {
		for (SDFExpression expression : expressions) {
			if (TemporalDatatype.expressionHasTemporalAttribute(expression, inputSchema)) {
				return true;
			}
		}
		return false;
	}

}
