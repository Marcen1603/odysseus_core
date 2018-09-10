/*******************************************************************************
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.prototyping.transform.rules;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.IRuleProvider;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class RuleProvider implements IRuleProvider {
    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IRule<?, ?>> getRules() {
        final List<IRule<?, ?>> rules = new ArrayList<>();
        rules.add(new TGroovyAORule());
        rules.add(new TJavaAORule());
        rules.add(new TJavaScriptAORule());
        rules.add(new TPythonAORule());
        rules.add(new TRubyAORule());
        rules.add(new TScriptAORule());
        return rules;
    }

}
