package de.uniol.inf.is.odysseus.condition.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

/**
 * Rule provider for condition monitoring operators
 * 
 * @author Tobias Brandt
 *
 */
public class RuleProvider implements IRuleProvider {

	/**
	 * Returns all rules.
	 */
	@Override
	public List<IRule<?, ?>> getRules() {
		List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
		rules.add(new ValueAreaAnomalyDetectionAOTransformRule());
		rules.add(new DeviationLearnAOTransformRule());
		rules.add(new DeviationAnomalyDetectionAOTransformRule());
		rules.add(new RarePatternPOTransformRule());
		rules.add(new FrequencyAnalysisAOTransformRule());
		rules.add(new LOFAnomalyDetectionAOTransformRule());
		return rules;
	}
}
