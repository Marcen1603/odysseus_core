package de.uniol.inf.is.odysseus.objecttracking.transform.rules.join;

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.latency.LatencyMergeFunction;
import de.uniol.inf.is.odysseus.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.ObjectTrackingJoinPO;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge.PredictionKeyMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.merge.ProbabilityMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;


public class TObjectTrackingJoinAOMetadataMergeRule extends AbstractTransformationRule<ObjectTrackingJoinPO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public void execute(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		System.out.println("SET ObjectTrackingJoinPO MetadataMergeFunction.");
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new TimeIntervalInlineMetadataMergeFunction());
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new LatencyMergeFunction());
		
		// TODO Andre: Covariance zwischen zwei Strömen auslesen und dem Join schon bei der Transformation
		// von logisch nach physisch übergeben
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new ProbabilityMetadataMergeFunction(null));
		((CombinedMergeFunction)operator.getMetadataMerge()).add(new PredictionKeyMetadataMergeFunction());
		
		// no update or retract
		// see JoinAO.drl in relational plug-in for explanation
		System.out.println("SET ObjectTrackingJoinPO MetadataMergeFunction finished."); 
	}

	@Override
	public boolean isExecutable(ObjectTrackingJoinPO operator,
			TransformationConfiguration config) {
		if(config.getMetaTypes().contains(ILatency.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IProbability.class.getCanonicalName()) &&
				config.getMetaTypes().contains(IPredictionFunctionKey.class.getCanonicalName()) &&
				operator.getMetadataMerge() instanceof CombinedMergeFunction){
			return true;
		}

		return false;
		// DRL-Code
//		$trafo : TransformationConfiguration( metaTypes contains "de.uniol.inf.is.odysseus.latency.ILatency" &&
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability" && 
//				metaTypes contains "de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey")
//		joinPO : ObjectTrackingJoinPO(metadataMerge != null)
//		eval(joinPO.getMetadataMerge() instanceof CombinedMergeFunction)
	}

	@Override
	public String getName() {
		return "ObjectTrackingJoinPO add MetadataMerge";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.METAOBJECTS;
	}

}
