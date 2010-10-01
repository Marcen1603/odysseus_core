package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.wrapper.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.base.DummyAccessMVPO;
import de.uniol.inf.is.odysseus.scars.base.JDVEAccessMVPO;
import de.uniol.inf.is.odysseus.scars.operator.testdata.TestdataProviderPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TJDVEAccessMVPOAsListRule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		String accessPOName = operator.getSource().getURI(false);
		ISource accessPO = null;
		System.out.println("Host = " + operator.getHost());
		if( "127.0.0.1".equals(operator.getHost())) {
			if (operator.getPort() == 5001) {
				accessPO = new DummyAccessMVPO();
				System.out.println("DummyAccessMVPO created");
			} else if (operator.getPort() == 5002) {
				accessPO = new TestdataProviderPO();
				((TestdataProviderPO)accessPO).setSourceName(accessPOName);
				System.out.println("TestdataProviderPO created");
			}
		} else {
			accessPO = new JDVEAccessMVPO(operator.getPort());
			System.out.println("JDVEAccessMVPO created");
		}
		accessPO.setOutputSchema(operator.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(operator);
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		if(operator.getSourceType().equals("JDVEAccessMVPO") &&
				WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null){
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO -> JDVEAccessMVPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.ACCESS;
	}

}
