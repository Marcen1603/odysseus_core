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

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TProbabilisticJoinAOSetMMRule extends AbstractTransformationRule<JoinTIPO<?, ?>> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void execute(final JoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getMetadataMerge());
        Objects.requireNonNull(transformConfig);
        Objects.requireNonNull(transformConfig.getMetaTypes());
        
        if (transformConfig.getMetaTypes().size() > 1) {
            ((CombinedMergeFunction) operator.getMetadataMerge()).add(new ProbabilisticMetadataMergeFunction());
        }
        else {
            operator.setMetadataMerge(TIMergeFunction.getInstance());
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(final JoinTIPO<?, ?> operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(transformConfig);
        Objects.requireNonNull(transformConfig.getMetaTypes());
        
        if (transformConfig.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
            if (operator.getMetadataMerge() != null) {
                if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "JoinTIPO add MetadataMerge (IProbabilistic)";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Class<? super JoinTIPO<?, ?>> getConditionClass() {
        return JoinTIPO.class;
    }

}
