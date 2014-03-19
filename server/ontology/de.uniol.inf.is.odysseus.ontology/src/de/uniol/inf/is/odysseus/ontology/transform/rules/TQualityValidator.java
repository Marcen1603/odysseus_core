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

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.metadata.IQuality;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityValidator extends AbstractTransformationRule<IHasMetadataMergeFunction<?>> {
    private static final Logger LOG = LoggerFactory.getLogger(TQualityValidator.class);

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final IHasMetadataMergeFunction<?> operator, final TransformationConfiguration config) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getMetadataMerge());
        if (!((CombinedMergeFunction<?>) operator.getMetadataMerge()).providesMergeFunctionFor(IQuality.class)) {
            TQualityValidator.LOG.warn("No Quality merge function set for " + operator);
        }
    }

    @Override
    public boolean isExecutable(final IHasMetadataMergeFunction<?> operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getMetadataMerge());
        Objects.requireNonNull(config);
        Objects.requireNonNull(config.getMetaTypes());
        if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
            if (config.getMetaTypes().contains(IQuality.class.getCanonicalName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Quality Validation";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.VALIDATE;
    }
}
