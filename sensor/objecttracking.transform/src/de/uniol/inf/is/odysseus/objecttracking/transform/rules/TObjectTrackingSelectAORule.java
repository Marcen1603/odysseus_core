package de.uniol.inf.is.odysseus.objecttracking.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingSelectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.ObjectTrackingSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObjectTrackingSelectAORule extends AbstractTransformationRule<ObjectTrackingSelectAO>{

	@Override
	public int getPriority() {
		return 15;
	}

	@Override
	public void execute(ObjectTrackingSelectAO operator,
			TransformationConfiguration config) {
	 	System.out.println("CREATE ObjectTrackingSelectPO.");
		ObjectTrackingSelectPO selectPO = new ObjectTrackingSelectPO(operator.getRangePredicates());
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, selectPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(selectPO);
		retract(operator);
		System.out.println("CREATE ObjectTrackingSelectPO finished.");		
	}

	@Override
	public boolean isExecutable(ObjectTrackingSelectAO operator,
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
//		selectAO : ObjectTrackingSelectAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "ObjectTrackingSelectAO -> ObjectTrackingSelectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
