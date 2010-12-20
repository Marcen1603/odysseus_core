package de.uniol.inf.is.odysseus.kdds.frequent.transform;

import de.uniol.inf.is.odysseus.kdds.frequent.logical.FrequentItemAO;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.LossyCountingFrequentItem;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.SimpleFrequentItemPO;
import de.uniol.inf.is.odysseus.kdds.frequent.physical.SpaceSavingFrequentItem;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFrequentAORule extends AbstractTransformationRule<FrequentItemAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(FrequentItemAO operator, TransformationConfiguration config) {
		IPhysicalOperator po = null;
		switch (operator.getStrategy()) {
		case Simple:
			po = new SimpleFrequentItemPO<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		case LossyCounting:
			po = new LossyCountingFrequentItem<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		case SpaceSaving:
			po = new SpaceSavingFrequentItem<RelationalTuple<?>>(operator.getRestrictList(), operator.getSize());
			break;
		default:
			break;
		}
		if(po!=null){
			po.setOutputSchema(operator.getOutputSchema());
			replace(operator, po, config);
			retract(operator);
		}
		
	}

	@Override
	public boolean isExecutable(FrequentItemAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "FrequentItemAO -> *Strategy*FrequentItemPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
