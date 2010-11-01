package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.logicaloperator.DistanceObjectSelectorAOAndre;
import de.uniol.inf.is.odysseus.scars.operator.objectselector.physicaloperator.DistanceObjectSelectionPO_Andre;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDistanceObjectSelectorAOAndreRule extends AbstractTransformationRule<DistanceObjectSelectorAOAndre>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(DistanceObjectSelectorAOAndre operator,
			TransformationConfiguration config) {
		System.out.println("CREATE DistanceObjectSelectorPOAndre.");
		DistanceObjectSelectionPO_Andre po = new DistanceObjectSelectionPO_Andre();
		po.setTrackedObjectList(operator.getTrackedObjectList());
		po.setSolutions(operator.getSolutions());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE DistanceObjectSelectorPOAndre finished.");
	}

	@Override
	public boolean isExecutable(DistanceObjectSelectorAOAndre operator,
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
//		$ao : DistanceObjectSelectorAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "DistanceObjectSelectorAOAndre -> DistanceObjectSelectorPOAndre";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
