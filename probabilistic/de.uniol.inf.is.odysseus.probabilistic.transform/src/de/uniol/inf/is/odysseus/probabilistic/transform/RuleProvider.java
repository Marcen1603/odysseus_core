/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.transform.flow.ITransformRuleProvider;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RuleProvider implements ITransformRuleProvider {

    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<IRule<?, ?>>();
        rules.add(new TProbabilisticAORule());

        rules.add(new TProbabilisticValidatorRule());

        // Probabilistic Aggregation Functions
        rules.add(new TAggregateProbabilisticRule());

        // Select AO -> PO Rule
        rules.add(new TSelectAORule());
        // Project AO -> PO Rule
        rules.add(new TProjectAORule());
        // Map AO -> PO Rule
        rules.add(new TMapAORule());
        // Join AO -> PO Rule for discrete probabilistic values
        rules.add(new TDiscreteJoinAORule());
        // Equi Join
        // Join AO -> PO Rule
        rules.add(new TContinuousEquiJoinAORule());
        // Set Join PO Sweep Areas Rule
        rules.add(new TContinuousEquiJoinAOSetSARule());
        // Set Join PO Data Merge Rule
        rules.add(new TContinuousEquiJoinAOSetDMRule());

        // LinearRegression AO -> PO Rule
        rules.add(new TLinearRegressionAORule());
        // LinearRegressionMerge AO -> PO Rule
        rules.add(new TLinearRegressionMergeAORule());

        // EM AO -> PO Rule
        rules.add(new TEMAORule());
        return rules;
    }

}
