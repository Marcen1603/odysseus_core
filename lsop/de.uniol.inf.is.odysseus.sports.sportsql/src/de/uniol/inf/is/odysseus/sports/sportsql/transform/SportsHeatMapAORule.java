package de.uniol.inf.is.odysseus.sports.sportsql.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sports.sportsql.logicaloperator.SportsHeatMapAO;
import de.uniol.inf.is.odysseus.sports.sportsql.physicaloperator.SportsHeatMapPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class SportsHeatMapAORule extends AbstractTransformationRule<SportsHeatMapAO> {

	@Override
	public void execute(SportsHeatMapAO operator, TransformationConfiguration config) throws RuleException {

		SportsHeatMapPO<Tuple<?>> heatmap = new SportsHeatMapPO<Tuple<?>>();
		defaultExecute(operator, heatmap, config, true, true);
	}

	@Override
	public boolean isExecutable(SportsHeatMapAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
