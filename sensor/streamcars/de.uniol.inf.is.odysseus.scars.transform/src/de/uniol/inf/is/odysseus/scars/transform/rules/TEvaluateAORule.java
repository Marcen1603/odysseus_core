package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.logicaloperator.EvaluationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.physicaloperator.EvaluationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TEvaluateAORule extends AbstractTransformationRule<EvaluationAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(EvaluationAO operator,
			TransformationConfiguration config) {
		
		System.out.println("CREATE EvaluationPO.");
		EvaluationPO po = new EvaluationPO();
		
		po.setAssociationObjListPath(operator.getAssociationObjListPath());
		po.setFilteringObjListPath(operator.getFilteringObjListPath());
		po.setBrokerObjListPath(operator.getBrokerObjListPath());
		po.setOutputSchema(operator.getOutputSchema());
		po.setThreshold(operator.getThreshold());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE EvaluationPO finished.");
		
	}

	@Override
	public boolean isExecutable(EvaluationAO operator,
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
//		$ao : EvaluationAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "EvaluationAO -> EvaluationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
