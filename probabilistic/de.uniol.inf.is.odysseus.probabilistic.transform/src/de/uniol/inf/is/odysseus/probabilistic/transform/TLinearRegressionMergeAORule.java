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

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.LinearRegressionMergeAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.LinearRegressionMergePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLinearRegressionMergeAORule extends AbstractTransformationRule<LinearRegressionMergeAO> {

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public void execute(LinearRegressionMergeAO operator, TransformationConfiguration config) {
        IPhysicalOperator linearRegressionMergePO = new LinearRegressionMergePO<ITimeInterval>(operator.getInputSchema(), operator.determineDependentList(), operator.determineExplanatoryList(), operator.getRegressionCoefficientsPos());
        this.defaultExecute(operator, linearRegressionMergePO, config, true, true);
    }

    @Override
    public boolean isExecutable(LinearRegressionMergeAO operator, TransformationConfiguration config) {
        if ((config.getDataTypes().contains(TransformUtil.DATATYPE))) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "LinearRegressionMergeAO -> LinearRegressionMergePO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super LinearRegressionMergeAO> getConditionClass() {
        return LinearRegressionMergeAO.class;
    }
}
