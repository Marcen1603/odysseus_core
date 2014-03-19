/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TJoinProbabilisticRule extends AbstractTransformationRule<JoinTIPO<?, ?>> {

    @Override
    public int getPriority() {
        return 0;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute(JoinTIPO<?, ?> operator, TransformationConfiguration config) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getMetadataMerge());
        ((CombinedMergeFunction) operator.getMetadataMerge()).add(new ProbabilisticMetadataMergeFunction());
    }

    @Override
    public boolean isExecutable(JoinTIPO<?, ?> operator, TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getMetadataMerge());
        Objects.requireNonNull(config);
        Objects.requireNonNull(config.getMetaTypes());
        if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
            if (config.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "JoinTIPO add MetadataMerge (IProbabilistic)";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super JoinTIPO<?, ?>> getConditionClass() {
        return JoinTIPO.class;
    }

}
