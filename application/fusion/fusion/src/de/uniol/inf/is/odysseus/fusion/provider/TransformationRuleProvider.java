/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
