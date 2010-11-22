package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisExpressionEvaluationAO;
import de.uniol.inf.is.odysseus.scars.objecttracking.association.physicaloperator.HypothesisExpressionEvaluationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings("rawtypes")
public class THypothesisExpressionEvaluationAORule extends AbstractTransformationRule<HypothesisExpressionEvaluationAO> {

	@Override
	public int getPriority() {
		return 20;
	}

	@Override
	public void execute(HypothesisExpressionEvaluationAO operator,
			TransformationConfiguration config) {

		System.out.println("CREATE HypothesisExpressionEvaluationPO");
		HypothesisExpressionEvaluationPO evaPO = new HypothesisExpressionEvaluationPO();

		evaPO.setPredictedObjectListPath(operator.getPredObjListPath());
		evaPO.setScannedObjectListPath(operator.getScanObjListPath());
		evaPO.setExpressionString(operator.getExpressionString());
		
		evaPO.setOutputSchema(operator.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, evaPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}

		insert(evaPO);
		retract(operator);
		System.out.println("CREATE HypothesisExpressionEvaluationPO finished.");

	}

	@Override
	public boolean isExecutable(HypothesisExpressionEvaluationAO operator,
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
		return "HypothesisExpressionEvaluationAO -> HypothesisExpressionEvaluationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
