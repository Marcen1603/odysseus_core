package de.uniol.inf.is.odysseus.scars.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TBrokerInitAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TDistanceObjectSelectorAOAndreRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TDistanceObjectSelectorAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TEvaluateAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterCovarianceAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterEstimateAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TFilterGainAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisEvaluationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisExpressionEvaluationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisExpressionGatingAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisGenarationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.THypothesisSelectionAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TInitializationAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TJDVEAccessMVPOAsListRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TJDVESinkAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TMetadataInitLatencyProbabilityStreamCarsRule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TMetadataObjectRelationalCreationPORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TPredictionAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TPredictionAssignPORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TScarsXMLProfilerAORule;
import de.uniol.inf.is.odysseus.scars.transform.rules.TTemporaryDataBouncerAORule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

public class StreamCarsRuleProvider implements ITransformRuleProvider{

	@Override
	public List<IRule<?, ?>> getRules() {
		ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new TBrokerInitAORule());
		rules.add(new TDistanceObjectSelectorAORule());
		rules.add(new TEvaluateAORule());
		rules.add(new TFilterAORule());
		rules.add(new TFilterCovarianceAORule());
		rules.add(new TFilterEstimateAORule());
		rules.add(new TFilterGainAORule());
		rules.add(new THypothesisEvaluationAORule());
		rules.add(new THypothesisGenarationAORule());
		rules.add(new THypothesisSelectionAORule());
		rules.add(new TInitializationAORule());
		rules.add(new TJDVESinkAORule());
		rules.add(new TJDVEAccessMVPOAsListRule());
		rules.add(new TMetadataInitLatencyProbabilityStreamCarsRule());
		rules.add(new TMetadataObjectRelationalCreationPORule());
		rules.add(new TPredictionAORule());
		rules.add(new TPredictionAssignPORule());
		rules.add(new TScarsXMLProfilerAORule());
		rules.add(new TTemporaryDataBouncerAORule());
		rules.add(new TDistanceObjectSelectorAOAndreRule());
		rules.add(new THypothesisExpressionEvaluationAORule());
		rules.add(new THypothesisExpressionGatingAORule());
		
		return rules;
	}

}
