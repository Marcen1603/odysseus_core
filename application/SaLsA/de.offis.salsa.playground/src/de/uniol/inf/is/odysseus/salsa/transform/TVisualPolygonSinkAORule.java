/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.salsa.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.salsa.logicaloperator.VisualPolygonSinkAO;
import de.uniol.inf.is.odysseus.salsa.physicaloperator.VisualPolygonSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TVisualPolygonSinkAORule extends AbstractTransformationRule<VisualPolygonSinkAO> {
    private static Logger LOG = LoggerFactory.getLogger(TVisualPolygonSinkAORule.class);

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void execute(final VisualPolygonSinkAO operator, final TransformationConfiguration config) {
        try {
            final VisualPolygonSinkPO po = new VisualPolygonSinkPO(operator.getOutputSchema());
            po.setOutputSchema(operator.getOutputSchema());
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            this.replace(operator, po, config);
            this.retract(operator);

        }
        catch (final Exception e) {
            TVisualPolygonSinkAORule.LOG.error(e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public String getName() {
        return "VisualPolygonSinkAO -> VisualPolygonSinkPO";
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /*
     * (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean isExecutable(final VisualPolygonSinkAO operator,
            final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public Class<?> getConditionClass() {
        return VisualPolygonSinkAO.class;
    }
}
