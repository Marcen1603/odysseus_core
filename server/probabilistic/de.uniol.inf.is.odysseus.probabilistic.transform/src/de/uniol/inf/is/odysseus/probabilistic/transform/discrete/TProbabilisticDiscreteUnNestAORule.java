/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnNestAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteUnNestPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.relational.transform.TUnnestAORule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticDiscreteUnNestAORule extends TUnnestAORule {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void execute(final UnNestAO operator, final TransformationConfiguration config) {
        final ProbabilisticDiscreteUnNestPO<?> po = new ProbabilisticDiscreteUnNestPO<ITimeIntervalProbabilistic>(operator.getAttributePosition());
        this.defaultExecute(operator, po, config, true, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "UnNestAO -> ProbabilisticDiscreteUnNestPO";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final UnNestAO operator, final TransformationConfiguration config) {
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                return SchemaUtils.isDiscreteProbabilisticAttribute(operator.getInputSchema().get(operator.getAttributePosition()));
            }
        }
        return false;
    }

}
