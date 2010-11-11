package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator.PredictionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPredictionAORule extends AbstractTransformationRule<PredictionAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(PredictionAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE PredictionPO.");
		PredictionPO po = new PredictionPO();
		
		po.setPredictionFunctions(operator.getPredictionFunctions());
		po.setObjectListPath(operator.getObjListPath());
		po.setOutputSchema(operator.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE PredictionPO finished.");
	}

	@Override
	public boolean isExecutable(PredictionAO operator,
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
//		$ao : PredictionAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "PredictionAO -> PredictionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
