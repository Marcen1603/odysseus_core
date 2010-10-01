package de.uniol.inf.is.odysseus.scars.test.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.test.ao.TestAO;
import de.uniol.inf.is.odysseus.scars.operator.test.po.TestPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TTestAORule extends AbstractTransformationRule<TestAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(TestAO operator, TransformationConfiguration config) {
		TestPO testPO = new TestPO(operator.getName());
		testPO.setOutputSchema(operator.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, testPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(config);
	}

	@Override
	public boolean isExecutable(TestAO operator,
			TransformationConfiguration config) {
		if(config.getDataType().equals("relational") && 
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		//DRL-Code
//		$testAO : TestAO( allPhysicalInputSet == true )
//		$trafo : TransformationConfiguration(dataType == "relational")
	}

	@Override
	public String getName() {
		return "TestAO -> TestPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
