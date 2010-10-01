package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.physicaloperator.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalTupleGroupingHelper;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAggregateAORule extends AbstractTransformationRule<AggregatePO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AggregatePO aggregatePO, TransformationConfiguration transformConfig) {
		aggregatePO.setGroupingHelper(new RelationalTupleGroupingHelper(aggregatePO.getInputSchema(), aggregatePO.getOutputSchema(), aggregatePO.getGroupingAttribute(),
				aggregatePO.getAggregations()));
		update(aggregatePO);

	}

	@Override
	public boolean isExecutable(AggregatePO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("relational")) {
			if (operator.getGroupingHelper() == null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Insert RelationalTupleGroupingHelper";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
