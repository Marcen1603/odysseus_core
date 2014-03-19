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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityIndicatorAO;
import de.uniol.inf.is.odysseus.ontology.metadata.IQuality;
import de.uniol.inf.is.odysseus.ontology.metadata.IQualityTimeInterval;
import de.uniol.inf.is.odysseus.ontology.metadata.QualityFactory;
import de.uniol.inf.is.odysseus.ontology.metadata.QualityTimeIntervalFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityIndicatorAORule extends AbstractTransformationRule<QualityIndicatorAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final QualityIndicatorAO operator, final TransformationConfiguration transformConfig) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        @SuppressWarnings("rawtypes")
        IMetadataUpdater mUpdater;
        if (Tuple.class.isAssignableFrom(operator.getInputSchema().getType())) {
            final SDFSchema schema = operator.getInputSchema();
            final int completenessPos = schema.indexOf(operator.getCompleteness());
            final int consistencyPos = schema.indexOf(operator.getConsistency());
            final int frequencyPos = schema.indexOf(operator.getFrequency());
            if (frequencyPos < 0) {
                mUpdater = new QualityFactory<IQuality, Tuple<IQuality>>(completenessPos, consistencyPos);
            }
            else {
                mUpdater = new QualityTimeIntervalFactory<IQualityTimeInterval, Tuple<IQualityTimeInterval>>(completenessPos, consistencyPos, frequencyPos);
            }
        }
        else {
            throw new TransformationException("Cannot set Time with " + operator.getInputSchema().getType());
        }

        @SuppressWarnings("unchecked")
        final MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<IQuality, Tuple<? extends IQuality>>(mUpdater);
        this.defaultExecute(operator, po, transformConfig, true, true);
    }

    @Override
    public boolean isExecutable(final QualityIndicatorAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(config);
        Objects.requireNonNull(config.getMetaTypes());
        if (config.getMetaTypes().contains(IQuality.class.getCanonicalName())) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "QualityIndicatorAO -> MetadataUpdatePO()";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super QualityIndicatorAO> getConditionClass() {
        return QualityIndicatorAO.class;
    }

}
