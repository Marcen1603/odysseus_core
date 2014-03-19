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
package de.uniol.inf.is.odysseus.ontology.transform.rules;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.metadata.IQualityTimeInterval;
import de.uniol.inf.is.odysseus.ontology.metadata.QualityTimeIntervalMetadataMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TAggregateQualityTimeIntervalRule extends AbstractTransformationRule<AggregateTIPO<?, ?, ?>> {

    @Override
    public int getPriority() {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(AggregateTIPO<?, ?, ?> operator, TransformationConfiguration config) throws RuleException {
        ((CombinedMergeFunction) operator.getMetadataMerge()).add(new QualityTimeIntervalMetadataMergeFunction());

    }

    @Override
    public boolean isExecutable(AggregateTIPO<?, ?, ?> operator, TransformationConfiguration config) {
        if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
            if (config.getMetaTypes().contains(IQualityTimeInterval.class.getCanonicalName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "AggregateTIPO add MetadataMerge (IQualityTimeInterval)";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super AggregateTIPO<?, ?, ?>> getConditionClass() {
        return AggregateTIPO.class;
    }

}
