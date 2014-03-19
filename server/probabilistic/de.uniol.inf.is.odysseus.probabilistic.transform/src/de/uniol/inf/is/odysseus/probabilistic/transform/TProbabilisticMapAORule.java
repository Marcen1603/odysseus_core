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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.relational.transform.TMapAORule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticMapAORule extends TMapAORule {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(final MapAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(config);

        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                boolean isProbabilisticExpression = false;
                for (final SDFExpression expr : operator.getExpressions()) {
                    if (SchemaUtils.containsProbabilisticAttributes(expr.getAllAttributes())) {
                        isProbabilisticExpression = true;
                    }
                    if (expr.getType() instanceof SDFProbabilisticDatatype) {
                        isProbabilisticExpression = true;
                    }
                }
                if (!isProbabilisticExpression) {
                    return true;
                }
            }
        }
        return super.isExecutable(operator, config);
    }

}
