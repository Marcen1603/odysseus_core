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

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.persistentqueries.PersistentTransferArea;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.metadata.DefaultProbabilisticTIDummyDataCreation;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.DiscreteProbabilisticJoinPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TDiscreteJoinAORule extends AbstractTransformationRule<JoinAO> {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public int getPriority() {
        return 1;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(JoinAO operator, TransformationConfiguration config) {

        IPredicate pred = operator.getPredicate();

        DiscreteProbabilisticJoinPO joinPO = new DiscreteProbabilisticJoinPO(TransformUtil.getDiscreteProbabilisticAttributePos(operator.getInputSchema(0)), TransformUtil.getDiscreteProbabilisticAttributePos(operator.getInputSchema(1)));
        joinPO.setJoinPredicate(pred.clone());

        // if in both input paths there is no window, we
        // use a persistent sweep area
        // check the paths
        boolean windowFound = false;
        for (int port = 0; port < 2; port++) {
            if (!JoinTransformationHelper.checkLogicalPath(operator.getSubscribedToSource(port).getTarget())) {
                windowFound = true;
                break;
            }
        }

        if (!windowFound) {
            joinPO.setTransferFunction(new PersistentTransferArea());
        }
        // otherwise we use a LeftJoinTISweepArea
        else {
            joinPO.setTransferFunction(new TITransferArea());
        }

        joinPO.setMetadataMerge(new CombinedMergeFunction());
        joinPO.setCreationFunction(new DefaultProbabilisticTIDummyDataCreation());

        defaultExecute(operator, joinPO, config, true, true);

    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public boolean isExecutable(JoinAO operator, TransformationConfiguration config) {
        IPredicate<?> predicate = operator.getPredicate();
        if (predicate != null) {
            List<SDFAttribute> attributes = predicate.getAttributes();
            if (TransformUtil.containsDiscreteProbabilisticAttributes(attributes)) {
                return true;
            }
        }
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "JoinAO -> discrete probabilistic Join";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
     */
    @Override
    public Class<? super JoinAO> getConditionClass() {
        return JoinAO.class;
    }

}
