package de.uniol.inf.is.odysseus.objecttracking.transform.rules.join;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.ObjectTrackingJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TObjectTrackingJoinAOSweepAreasRule extends AbstractTransformationRule<ObjectTrackingJoinPO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public void execute(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		System.out.println("INIT Join Areas");
		operator.initDefaultAreas();
		System.out.println("INIT Join Areas finished.");
	}

	@Override
	public boolean isExecutable(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.getAreas() == null && operator.getRangePredicates() != null){
			return true;
		}
		
		return false;
		
//		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		// the join predicate must be set first, since this will be set in the SweepAreas now
//		joinPO : ObjectTrackingJoinPO(areas == null && rangePredicates != null)
	}

	@Override
	public String getName() {
		return "ObjectTrackingJoinPO set default SweepAreas";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
