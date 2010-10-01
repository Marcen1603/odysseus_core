package de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ObjectTrackingProjectAO;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.ObjectTrackingProjectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObjectTrackingPredictionProjectAORule extends AbstractTransformationRule<ObjectTrackingProjectAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(ObjectTrackingProjectAO operator,
			TransformationConfiguration config) {
	 	System.out.println("CREATE ObjectTrackingProjectionPO.");
		ObjectTrackingProjectPO projectPO = new ObjectTrackingProjectPO(operator);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, projectPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(projectPO);
		retract(operator);
		System.out.println("CREATE ObjectTrackingProjectionPO finished.");
	}

	@Override
	public boolean isExecutable(ObjectTrackingProjectAO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.isAllPhysicalInputSet()){
			return true;
		}
		return false;
		
		// DRL-Code
//		$trafo : TransformationConfiguration(
//				metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey"
//			)
//			projectAO : ObjectTrackingProjectAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "ProjectAO -> ProjectPredictionMVPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
