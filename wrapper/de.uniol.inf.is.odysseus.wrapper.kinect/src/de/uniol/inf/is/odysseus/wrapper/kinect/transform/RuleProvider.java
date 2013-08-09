package de.uniol.inf.is.odysseus.wrapper.kinect.transform;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * Rule provider will be registered via OSGI.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class RuleProvider implements ITransformRuleProvider {
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(RuleProvider.class);

    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
        rules.add(new TVisualKinectSinkAORule());
        if (RuleProvider.log.isDebugEnabled()) {
            for (final IRule<?, ?> rule : rules) {
                RuleProvider.log
                        .debug(String.format("Register rule: %s", rule));
            }
        }
        return rules;
    }
}
