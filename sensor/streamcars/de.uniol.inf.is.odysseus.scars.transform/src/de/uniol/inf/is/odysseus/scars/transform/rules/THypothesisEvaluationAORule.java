package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisEvaluationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.AbstractHypothesisEvaluationPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.MahalanobisDistanceEvaluationPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.MultiDistanceEvaluationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class THypothesisEvaluationAORule extends AbstractTransformationRule<HypothesisEvaluationAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(HypothesisEvaluationAO operator,
			TransformationConfiguration config) {
		
		System.out.println("CREATE AbstractHypothesisEvaluationPO");
		AbstractHypothesisEvaluationPO evaPO = null;
		String functionID = operator.getFunctionID();
		if (functionID.equals("MAHA")) {
			evaPO = new MahalanobisDistanceEvaluationPO();
		} else if (functionID.equals("MULTIDISTANCE")) {
			evaPO = new MultiDistanceEvaluationPO();
		} else {
			//Create default po to avoid crashes
			System.out.println("Cannot resolve '" + functionID + "' to a physical operator: Creating default HypothesisEvaluationPO!");
			evaPO = new MultiDistanceEvaluationPO();
		}
		evaPO.setOldObjListPath(operator.getOldObjListPath());
		evaPO.setNewObjListPath(operator.getNewObjListPath());
		evaPO.setAlgorithmParameter(operator.getAlgorithmParameter());
		evaPO.setMeasurementPairs(operator.getMeasurementPairs());
		evaPO.initAlgorithmParameter();
		evaPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, evaPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}

		insert(evaPO);
		retract(operator);
		System.out.println("CREATE AbstractHypothesisEvaluationPO finished.");
		
	}

	@Override
	public boolean isExecutable(HypothesisEvaluationAO operator,
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
//		$evaAO : HypothesisEvaluationAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "HypothesisEvaluationAO -> HypothesisEvaluationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
