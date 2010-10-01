package de.uniol.inf.is.odysseus.objecttracking.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TMetadataInit_Latency_Probability_Rule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TObjectTrackingPredictionAssignAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.TObjectTrackingPunctuationAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.access.TAccessAOSILABRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.access.TAccessMVPORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAODataMergeRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOMetadataMergeRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOSweepAreasRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.join.TObjectTrackingJoinAOTransferFunctionRule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection.TObjectTrackingPredictionProjectAORule;
import de.uniol.inf.is.odysseus.objecttracking.transform.rules.projection.TObjectTrackingProjectAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;


public class ObjectTrackingTransformationRuleProvider implements ITransformRuleProvider {

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();
		rules.add(new TAccessMVPORule());
		rules.add(new TAccessAOSILABRule());
		rules.add(new TObjectTrackingJoinAORule());
		rules.add(new TObjectTrackingJoinAOSweepAreasRule());
		rules.add(new TObjectTrackingJoinAODataMergeRule());
		rules.add(new TObjectTrackingJoinAOMetadataMergeRule());
		rules.add(new TObjectTrackingJoinAOTransferFunctionRule());
		rules.add(new TMetadataInit_Latency_Probability_Rule());
		rules.add(new TObjectTrackingPredictionAssignAORule());
		rules.add(new TObjectTrackingProjectAORule());
		rules.add(new TObjectTrackingPredictionProjectAORule());
		rules.add(new TObjectTrackingPunctuationAORule());
		return null;
	}

}
