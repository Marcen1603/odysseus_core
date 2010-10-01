package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisSelectionAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.HypothesisSelectionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class THypothesisSelectionAORule extends AbstractTransformationRule<HypothesisSelectionAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(HypothesisSelectionAO operator,
			TransformationConfiguration config) {
		
		System.out.println("CREATE HypothesisSelectionPO");
		HypothesisSelectionPO selePO = new HypothesisSelectionPO();
		selePO.setOldObjListPath(operator.getOldObjListPath());
		selePO.setNewObjListPath(operator.getNewObjListPath());
		selePO.setOutputSchema(operator.getOutputSchema());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, selePO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(selePO);
		retract(operator);
		System.out.println("CREATE HypothesisSelectionPO finished.");
		
	}

	@Override
	public boolean isExecutable(HypothesisSelectionAO operator,
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
//		$seleAO : HypothesisSelectionAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "HypothesisSelectionAO -> HypothesisSelectionPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
