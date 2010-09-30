package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.initialization.logicaloperator.InitializationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.initialization.physicaloperator.InitializationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TInitializationAORule extends AbstractTransformationRule<InitializationAO>{

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public void execute(InitializationAO operator,
			TransformationConfiguration config) {
		
		System.out.print("CREATE Initialization PO...");

		InitializationPO initializationPO = new InitializationPO();
		
		initializationPO.setOutputSchema(operator.getOutputSchema());
		initializationPO.setNewObjListPath( operator.getNewObjListPath());
		initializationPO.setOldObjListPath( operator.getOldObjListPath());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, initializationPO);
			for (ILogicalOperator o:toUpdate)
			{
				update(o);
			}
			insert(initializationPO);
			
		retract(operator);
		
		System.out.println("CREATE Initialization PO finished.");
		
	}

	@Override
	public boolean isExecutable(InitializationAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		$initializationAO : InitializationAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "InitializationAO -> InitializationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
