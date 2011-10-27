package de.uniol.inf.is.odysseus.salsa.transform;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RuleProvider implements ITransformRuleProvider {
    private static Logger LOG = LoggerFactory.getLogger(RuleProvider.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider#getRules()
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
        rules.add(new TVisualSinkAORule());
        rules.add(new TPunctuationAORule());
        return rules;
    }
}
