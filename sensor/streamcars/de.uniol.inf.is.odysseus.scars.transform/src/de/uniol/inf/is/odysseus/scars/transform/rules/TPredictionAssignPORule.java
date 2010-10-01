package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.logicaloperator.PredictionAssignAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.prediction.physicaloperator.PredictionAssignPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPredictionAssignPORule extends AbstractTransformationRule<PredictionAssignAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(PredictionAssignAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE PredictionAssignPO.");
		PredictionAssignPO predictionPO = new PredictionAssignPO();
		predictionPO.init(operator.getPathToList(), operator.getPredictionFunctions());
		predictionPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, predictionPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(predictionPO);
		retract(operator);
		System.out.println("CREATE PredictionAssignPO finished.");
	}

	@Override
	public boolean isExecutable(PredictionAssignAO operator,
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
//		$predictionAO : PredictionAssignAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "PredictionAssignAO -> PredictionAssignPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
