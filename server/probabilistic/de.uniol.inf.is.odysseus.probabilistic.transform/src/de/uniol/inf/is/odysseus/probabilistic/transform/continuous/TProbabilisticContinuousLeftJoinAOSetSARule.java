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

import java.util.Objects;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTISweepArea;
import de.uniol.inf.is.odysseus.server.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class TProbabilisticContinuousLeftJoinAOSetSARule extends AbstractTransformationRule<LeftJoinTIPO> {

    @Override
    public int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    @Override
    public void execute(final LeftJoinTIPO operator, final TransformationConfiguration transformConfig) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        final ITimeIntervalSweepArea[] areas = new ITimeIntervalSweepArea[2];

        areas[0] = new JoinTISweepArea();
        areas[1] = new JoinTISweepArea();

        operator.setAreas(areas);
    }

    @Override
    public boolean isExecutable(final LeftJoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        if ((operator.getOutputSchema().getType() == ProbabilisticTuple.class) && operator.getInputSchema(0).hasMetatype(ITimeInterval.class)
        		&& operator.getInputSchema(1).hasMetatype(ITimeInterval.class)) {
           return !operator.isAreasSet();
        }
        return false;
    }

    @Override
    public String getName() {
        return "LeftJoinTIPO set SweepArea";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super LeftJoinTIPO> getConditionClass() {
        return LeftJoinTIPO.class;
    }

}
