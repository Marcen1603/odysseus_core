package de.uniol.inf.is.odysseus.objecttracking.transform.rules.access;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AtomicDataInputStreamAccessMVPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessMVPORule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		System.out.println("CREATE AccessMVPO: " + operator); 
		String sourceName = operator.getSource().getURI(false);
		ISource accessPO = new AtomicDataInputStreamAccessMVPO(operator.getHost(), operator.getPort(), operator.getOutputSchema());
		accessPO.setOutputSchema(operator.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(sourceName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, accessPO);

		for(ILogicalOperator o: toUpdate){
			update(o);
		}		

		retract(operator);
		insert(accessPO);
		System.out.println("CREATE AccessMVPO finished.");
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		if(operator.getSourceType().equals("RelationalAtomicDataInputStreamAccessMVPO") &&
				WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		$accessAO : AccessAO( sourceType == "RelationalAtomicDataInputStreamAccessMVPO" )
//		$trafo: TransformationConfiguration()
//		eval(WrapperPlanFactory.getAccessPlan($accessAO.getSource().getURI()) == null)
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AccessAO (AtomicData MV) -> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.ACCESS;
	}

}
