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
package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.transform.rules.TSelectAORule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticSelectAORule extends TSelectAORule {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void execute(final SelectAO operator, final TransformationConfiguration config) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getPredicate());
        if (!SchemaUtils.containsProbabilisticAttributes(PredicateUtils.getAttributes(operator.getPredicate()))) {
            super.execute(operator, config);
        }
        else {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            final IPhysicalOperator selectPO = new ProbabilisticSelectPO(operator.getInputSchema(), operator.getPredicate());
            this.defaultExecute(operator, selectPO, config, true, true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final SelectAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getPredicate());
        Objects.requireNonNull(config);
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                //if (SchemaUtils.containsProbabilisticAttributes(PredicateUtils.getAttributes(operator.getPredicate()))) {
                    return true;
                //}
            }
        }
        return false;
    }
}
