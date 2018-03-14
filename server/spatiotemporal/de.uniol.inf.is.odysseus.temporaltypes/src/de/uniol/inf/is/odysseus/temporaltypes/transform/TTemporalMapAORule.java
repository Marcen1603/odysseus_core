package de.uniol.inf.is.odysseus.temporaltypes.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.metadata.IValidTime;
import de.uniol.inf.is.odysseus.temporaltypes.physicalopertor.TemporalRelationalMapPO;
import de.uniol.inf.is.odysseus.temporaltypes.types.TemporalType;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTemporalMapAORule extends AbstractTransformationRule<MapAO> {

	@Override
	public void execute(MapAO mapAO, TransformationConfiguration config) throws RuleException {
		int[] restrictList = SDFSchema.calcRestrictList(mapAO.getInputSchema(), mapAO.getRemoveAttributes());
		TemporalRelationalMapPO<IValidTime> mapPO = new TemporalRelationalMapPO<>(mapAO.getInputSchema(),
				mapAO.getExpressionList().toArray(new SDFExpression[0]), mapAO.isAllowNullValue(),
				mapAO.isEvaluateOnPunctuation(), mapAO.isExpressionsUpdateable(), mapAO.isSuppressErrors(),
				mapAO.isKeepInput(), restrictList);
		defaultExecute(mapAO, mapPO, config, true, true);
	}

	@Override
	public boolean isExecutable(MapAO operator, TransformationConfiguration config) {
		return this.containsExpressionWithTemporalAttribute(operator.getExpressionList());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public int getPriority() {
		return 1;
	}

	private boolean containsExpressionWithTemporalAttribute(List<SDFExpression> expressions) {
		for (SDFExpression expression : expressions) {
			for (SDFAttribute attribute : expression.getAllAttributes()) {
				if (attribute instanceof TemporalType) {
					return true;
				}
			}
		}
		return false;
	}

}
