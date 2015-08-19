/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.ontology.transform.rules;

import java.util.Objects;

import cc.kuka.odysseus.ontology.logicaloperator.QualityIndicatorAO;
import cc.kuka.odysseus.ontology.metadata.IQuality;
import cc.kuka.odysseus.ontology.metadata.QualityFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TQualityIndicatorAORule extends AbstractTransformationRule<QualityIndicatorAO> {

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
                mUpdater = new QualityFactory<>(completenessPos, consistencyPos, frequencyPos);

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
            if (operator.isAllPhysicalInputSet()) {
                return true;
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
