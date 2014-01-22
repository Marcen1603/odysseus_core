/**
 * 
 */
package de.uniol.inf.is.odysseus.ontology;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ontology.rewrite.rules.RInsertSensingDevicesRule;
import de.uniol.inf.is.odysseus.rewrite.flow.IRewriteRuleProvider;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RewriteRuleProvider implements IRewriteRuleProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final ArrayList<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
        rules.add(new RInsertSensingDevicesRule());

        return rules;
    }

}
