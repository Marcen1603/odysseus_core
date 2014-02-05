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
package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.ProbabilisticContinuousJoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TProbabilisticContinuousJoinAOSetSARule extends AbstractTransformationRule<JoinTIPO> {

    @Override
    public int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    @Override
    public void execute(final JoinTIPO operator, final TransformationConfiguration transformConfig) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(transformConfig);
        final ProbabilisticContinuousJoinTISweepArea<?, ?>[] areas = new ProbabilisticContinuousJoinTISweepArea[2];

        final IDataMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic> dataMerge = new ProbabilisticMergeFunction<Tuple<ITimeIntervalProbabilistic>, ITimeIntervalProbabilistic>(
                operator.getOutputSchema().size());
        IMetadataMergeFunction<?> metadataMerge;
        if (transformConfig.getMetaTypes().size() > 1) {
            final CombinedMergeFunction<ITimeIntervalProbabilistic> combinedMetadataMerge = new CombinedMergeFunction<ITimeIntervalProbabilistic>();
            combinedMetadataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
            metadataMerge = combinedMetadataMerge;
        }
        else {
            metadataMerge = TIMergeFunction.getInstance();
        }
        final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        if (operator.getPredicate() != null) {
            attributes.addAll(SchemaUtils.getContinuousProbabilisticAttributes(operator.getPredicate().getAttributes()));
        }

        final SDFSchema leftSchema = operator.getSubscribedToSource(0).getSchema();
        final SDFSchema rightSchema = operator.getSubscribedToSource(1).getSchema();

        final List<SDFAttribute> leftAttributes = new ArrayList<SDFAttribute>(leftSchema.getAttributes());
        leftAttributes.retainAll(attributes);

        final List<SDFAttribute> rightAttributes = new ArrayList<SDFAttribute>(rightSchema.getAttributes());
        rightAttributes.retainAll(attributes);
        rightAttributes.removeAll(leftAttributes);

        final int[] rightProbabilisticAttributePos = SchemaUtils.getAttributePos(rightSchema, rightAttributes);
        final int[] leftProbabilisticAttributePos = SchemaUtils.getAttributePos(leftSchema, leftAttributes);

        for (int port = 0; port < 2; port++) {
            areas[port] = new ProbabilisticContinuousJoinTISweepArea(leftProbabilisticAttributePos, rightProbabilisticAttributePos, dataMerge, metadataMerge);
        }

        operator.setAreas(areas);
    }

    @Override
    public boolean isExecutable(final JoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        if ((operator.getOutputSchema().getType() == ProbabilisticTuple.class) && transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
            if (operator.getAreas() == null) {
                final IPredicate<?> predicate = operator.getPredicate();
                final Set<SDFAttribute> attributes = PredicateUtils.getAttributes(predicate);
                if (SchemaUtils.containsContinuousProbabilisticAttributes(attributes)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "JoinTIPO set SweepArea";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super JoinTIPO> getConditionClass() {
        return JoinTIPO.class;
    }
}
