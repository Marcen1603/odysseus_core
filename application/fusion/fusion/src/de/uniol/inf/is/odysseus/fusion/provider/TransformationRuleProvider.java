package de.uniol.inf.is.odysseus.fusion.provider;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.fusion.transform.TContextStoreAORule;
import de.uniol.inf.is.odysseus.fusion.transform.TExtPolygonSinkAORule;
import de.uniol.inf.is.odysseus.fusion.transform.TSpatialAssociationAORule;
import de.uniol.inf.is.odysseus.fusion.transform.TSpatialFilterAORule;
import de.uniol.inf.is.odysseus.fusion.transform.TSpatialPredictionAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * @author Kai Pancratz <kai@pancratz.net>
 */
public class TransformationRuleProvider implements ITransformRuleProvider {
    private static Logger LOG = LoggerFactory.getLogger(TransformationRuleProvider.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider#getRules()
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
        rules.add(new TExtPolygonSinkAORule());
        
        rules.add(new TContextStoreAORule());
        rules.add(new TSpatialPredictionAORule());
        rules.add(new TSpatialAssociationAORule());
        rules.add(new TSpatialFilterAORule());
        
        if (LOG.isDebugEnabled()) {
            for (IRule<?, ?> rule : rules) {
                LOG.debug(String.format("Register rule: %s", rule));
            }
        }
        return rules;
    }
}