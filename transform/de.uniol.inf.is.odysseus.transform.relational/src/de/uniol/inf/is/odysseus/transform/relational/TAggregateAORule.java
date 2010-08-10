package de.uniol.inf.is.odysseus.transform.relational;

import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.AggregatePO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalTupleGroupingHelper;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAggregateAORule extends AbstractTransformationRule<AggregatePO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void transform(AggregatePO aggregatePO, TransformationConfiguration transformConfig) {
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

}
