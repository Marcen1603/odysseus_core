package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticMapPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class TMapAORule extends AbstractTransformationRule<MapAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(MapAO mapAO, TransformationConfiguration transformConfig) {
		ProbabilisticMapPO<?> mapPO = new ProbabilisticMapPO<IMetaAttribute>(
				mapAO.getInputSchema(), mapAO.getExpressions().toArray(
						new SDFExpression[0]));
		defaultExecute(mapAO, mapPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(MapAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("probabilistic")) {
			if (operator.getPhysSubscriptionTo() != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "MapAO -> ProbabilisticMapPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super MapAO> getConditionClass() {
		return MapAO.class;
	}

}
