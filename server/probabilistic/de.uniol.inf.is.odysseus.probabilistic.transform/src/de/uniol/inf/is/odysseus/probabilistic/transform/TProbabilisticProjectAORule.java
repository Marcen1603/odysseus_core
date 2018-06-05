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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.relational.transform.TProjectAORule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticProjectAORule extends TProjectAORule {
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
    public final void execute(final ProjectAO operator, final TransformationConfiguration config) throws RuleException {
        // The probabilistic tuple overrides the restrict function of the
        // relational tuple.
        super.execute(operator, config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final ProjectAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(config);

        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                return true;
            }
        }
        return super.isExecutable(operator, config);
    }
}
