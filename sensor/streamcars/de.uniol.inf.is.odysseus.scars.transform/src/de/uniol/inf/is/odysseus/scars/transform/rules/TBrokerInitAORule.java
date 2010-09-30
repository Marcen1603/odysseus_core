package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.infs.is.odysseus.scars.operator.brokerinit.BrokerInitAO;
import de.uniol.infs.is.odysseus.scars.operator.brokerinit.BrokerInitPO;

public class TBrokerInitAORule extends AbstractTransformationRule<BrokerInitAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(BrokerInitAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE BrokerInitPO.");
		BrokerInitPO po = new BrokerInitPO();
		
		po.setOutputSchema(operator.getOutputSchema());
		po.setSize(operator.getSize());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE BrokerInitPO finished.");
		
	}

	@Override
	public boolean isExecutable(BrokerInitAO operator,
			TransformationConfiguration config) {
		if(operator.isAllPhysicalInputSet()){
			return true;
		}
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration()
//		$ao : BrokerInitAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "BrokerInitAO -> BrokerInitPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
