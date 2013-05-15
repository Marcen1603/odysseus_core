/*
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

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TSelectAORule extends AbstractTransformationRule<SelectAO> {

    @Override
    public int getPriority() {
        return 1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void execute(final SelectAO selectAO, final TransformationConfiguration transformConfig) {
        IPhysicalOperator selectPO;
        if (this.isProbabilistic(selectAO)) {
            SDFProbabilisticExpression expression = new SDFProbabilisticExpression(((RelationalPredicate) selectAO.getPredicate()).getExpression());
            ProbabilisticPredicate predicate = new ProbabilisticPredicate(expression);
            selectPO = new ProbabilisticSelectPO(predicate);
            if (selectAO.getHeartbeatRate() > 0) {
                ((ProbabilisticSelectPO<ProbabilisticTuple<?>>) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(selectAO.getHeartbeatRate()));
            }
        } else {
            selectPO = new SelectPO(selectAO.getPredicate());
            if (selectAO.getHeartbeatRate() > 0) {
                ((SelectPO) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(selectAO.getHeartbeatRate()));
            }
        }
        this.defaultExecute(selectAO, selectPO, transformConfig, true, true);
    }

    @Override
    public boolean isExecutable(final SelectAO operator, final TransformationConfiguration transformConfig) {
        if (transformConfig.getDataTypes().contains(TransformUtil.DATATYPE)) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "SelectAO -> ProbabilisticSelectPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super SelectAO> getConditionClass() {
        return SelectAO.class;
    }

    private boolean isProbabilistic(final SelectAO selectAO) {
        final List<SDFAttribute> attributes = selectAO.getPredicate().getAttributes();

        boolean isProbabilistic = false;
        for (final SDFAttribute attribute : attributes) {
            if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
                isProbabilistic = true;
            }
        }
        return isProbabilistic;
    }
}
