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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.prototyping.logicaloperator.GroovyAO;
import de.uniol.inf.is.odysseus.prototyping.physicaloperator.ScriptPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TGroovyAORule extends AbstractTransformationRule<GroovyAO> {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(GroovyAO.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(final GroovyAO ao, final TransformationConfiguration config) throws RuleException {
        final ScriptPO<IMetaAttribute, Tuple<IMetaAttribute>> po = new ScriptPO<>(ao);
        this.defaultExecute(ao, po, config, true, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(final GroovyAO operator, final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<? super GroovyAO> getConditionClass() {
        return GroovyAO.class;
    }
}
