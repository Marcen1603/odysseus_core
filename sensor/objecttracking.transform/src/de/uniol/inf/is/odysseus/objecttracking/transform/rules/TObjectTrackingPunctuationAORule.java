package de.uniol.inf.is.odysseus.objecttracking.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.PunctuationAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.PunctuationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObjectTrackingPunctuationAORule extends AbstractTransformationRule<PunctuationAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(PunctuationAO operator,
			TransformationConfiguration config) {
	 	System.out.println("CREATE PunctuationPO.");
		PunctuationPO puncPO = new PunctuationPO(operator.getPunctuationCount());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, puncPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(puncPO);
		retract(operator);
		System.out.println("CREATE PunctuationPO finished.");
		
	}

	@Override
	public boolean isExecutable(PunctuationAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		puncAO : PunctuationAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "PunctuationAO -> PunctuationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
