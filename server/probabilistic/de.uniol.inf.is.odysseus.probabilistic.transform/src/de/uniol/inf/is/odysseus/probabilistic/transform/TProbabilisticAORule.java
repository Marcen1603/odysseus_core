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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticAO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisticAORule extends AbstractTransformationRule<ProbabilisticAO> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return 0;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void execute(final ProbabilisticAO operator, final TransformationConfiguration config) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        @SuppressWarnings("rawtypes")
        IMetadataUpdater mUpdater;
        if (Tuple.class.isAssignableFrom(operator.getInputSchema().getType())) {
            final SDFSchema schema = operator.getInputSchema();
            final int pos = schema.indexOf(operator.getAttribute());
            mUpdater = new ProbabilisticFactory<IProbabilistic, Tuple<IProbabilistic>>(pos);
        }
        else {
            throw new TransformationException("Cannot set probability with " + operator.getInputSchema().getType());
        }

        @SuppressWarnings("unchecked")
        final MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<IProbabilistic, Tuple<? extends IProbabilistic>>(mUpdater);
        this.defaultExecute(operator, po, config, true, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final ProbabilisticAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(config);
        if (operator.getInputSchema().hasMetatype(IProbabilistic.class)) {
            if (operator.isAllPhysicalInputSet()) {
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
        return "ProbabilisticAO -> MetadataUpdatePO(IProbabilistic)";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Class<? super ProbabilisticAO> getConditionClass() {
        return ProbabilisticAO.class;
    }
}
