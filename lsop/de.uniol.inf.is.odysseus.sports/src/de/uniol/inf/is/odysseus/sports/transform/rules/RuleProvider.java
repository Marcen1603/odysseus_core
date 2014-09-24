package de.uniol.inf.is.odysseus.sports.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;
/**
 * Rule Provider for Sports Operators.
 * @author Carsten Cordes
 *
 */
public class RuleProvider implements IRuleProvider {
    @Override
    /**
     * Returns all rules.
     */
    public List<IRule<?, ?>> getRules() {
        List<IRule<?, ?>> rules = new ArrayList<IRule<?,?>>();      
        rules.add(new TReduceLoadAORule());
        return rules;
    }
}