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
package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticDiscreteJoinTISweepArea;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticJoinTIPO;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.TIMergeFunction;
import de.uniol.inf.is.odysseus.server.intervalapproach.TimeIntervalInlineMetadataMergeFunction;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for relational Join operator for discrete probabilistic
 * values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticDiscreteJoinAOSetSARule extends AbstractTransformationRule<JoinTIPO<IProbabilisticTimeInterval, ProbabilisticTuple<IProbabilisticTimeInterval>>> {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public final void execute(final JoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(transformConfig);
        final ProbabilisticDiscreteJoinTISweepArea<?, ?>[] areas = new ProbabilisticDiscreteJoinTISweepArea[2];

        final IDataMergeFunction<Tuple<IProbabilisticTimeInterval>, IProbabilisticTimeInterval> dataMerge = new ProbabilisticMergeFunction<Tuple<IProbabilisticTimeInterval>, IProbabilisticTimeInterval>(
                operator.getOutputSchema().size());
        IMetadataMergeFunction<?> metadataMerge;
        if (transformConfig.getMetaTypes().size() > 1) {
            final CombinedMergeFunction<IProbabilisticTimeInterval> combinedMetadataMerge = new CombinedMergeFunction<IProbabilisticTimeInterval>();
            combinedMetadataMerge.add(new TimeIntervalInlineMetadataMergeFunction());
            metadataMerge = combinedMetadataMerge;
        }
        else {
            metadataMerge = TIMergeFunction.getInstance();
        }
        final List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
        if (operator.getPredicate() != null) {
            attributes.addAll(SchemaUtils.getProbabilisticAttributes(operator.getPredicate().getAttributes()));
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
            areas[port] = new ProbabilisticDiscreteJoinTISweepArea(leftProbabilisticAttributePos, rightProbabilisticAttributePos, dataMerge, metadataMerge);
        }

        operator.setAreas(areas);

    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(@SuppressWarnings("rawtypes") final JoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(transformConfig);
        if (operator.getAreas() == null) {
            if (operator instanceof ProbabilisticJoinTIPO) {
                final IPredicate<?> predicate = operator.getPredicate();
                final Set<SDFAttribute> attributes = PredicateUtils.getAttributes(predicate);
//                IF (SCHEMAUTILS.CONTAINSDISCRETEPROBABILISTICATTRIBUTES(ATTRIBUTES)) {
//                    RETURN TRUE;
//                }
            }
        }
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public final String getName() {
        return "ProbabilisticDiscreteJoinPO set SweepArea";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public final IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
     */
    @Override
    public final Class<? super JoinTIPO<?, ?>> getConditionClass() {
        return JoinTIPO.class;
    }

}
