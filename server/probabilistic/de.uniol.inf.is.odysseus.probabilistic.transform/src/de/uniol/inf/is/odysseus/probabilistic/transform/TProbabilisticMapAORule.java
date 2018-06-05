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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.transform.TMapAORule;

/**
 * Transformation rule for probabilistic Map operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
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

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void execute(final MapAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getExpressionList());
        Objects.requireNonNull(config);
        IPhysicalOperator mapPO;

        final SDFProbabilisticExpression[] expressions = new SDFProbabilisticExpression[operator.getExpressionList().size()];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = new SDFProbabilisticExpression(operator.getExpressionList().get(i));
        }
        mapPO = new ProbabilisticMapPO<IProbabilistic>(operator.getInputSchema(), expressions, false);

        this.defaultExecute(operator, mapPO, config, true, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final MapAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(operator.getExpressionList());
        Objects.requireNonNull(config);
        if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
            if (operator.getPhysSubscriptionTo() != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "MapAO -> ProbabilisticMapPO";
    }

}
