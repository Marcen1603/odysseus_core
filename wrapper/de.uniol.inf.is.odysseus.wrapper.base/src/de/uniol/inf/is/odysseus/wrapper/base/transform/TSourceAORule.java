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
package de.uniol.inf.is.odysseus.wrapper.base.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.logicaloperator.SourceAO;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SourcePO;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class TSourceAORule extends AbstractTransformationRule<SourceAO> {
    private static Logger LOG = LoggerFactory.getLogger(TSourceAORule.class);

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public void execute(final SourceAO operator, final TransformationConfiguration config) {
        try {
            @SuppressWarnings({ "rawtypes", "unchecked" })
			SourcePO<?> po = new SourcePO(operator.getOutputSchema(), operator.getAdapter(),
                    operator.getOptionsMap());
//            if (SourcePool.hasSemanticallyEqualSource(po)) {
//                po = SourcePool.getSemanticallyEqualSource(po);
//            }else {
                SourcePool.registerSource(operator.getAdapter(), po, operator.getOptionsMap());
//            }
            final Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                    operator, po);
            for (final ILogicalOperator o : toUpdate) {
                this.update(o);
            }
            retract(operator);
            insert(po);
        }
        catch (final Exception e) {
            TSourceAORule.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(final SourceAO operator, final TransformationConfiguration config) {
        return true;
    }

    @Override
    public String getName() {
        return "SourceAO -> SourcePO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.ACCESS;
    }

}
