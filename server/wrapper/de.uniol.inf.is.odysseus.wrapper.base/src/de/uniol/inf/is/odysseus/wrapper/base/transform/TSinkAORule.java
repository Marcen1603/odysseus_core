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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.logicaloperator.SinkAO;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SinkPO;

public class TSinkAORule extends AbstractTransformationRule<SinkAO> {
    private static Logger LOG = LoggerFactory.getLogger(TSinkAORule.class);

    @Override
    public int getPriority() {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public void execute(final SinkAO operator, final TransformationConfiguration config) {
        try {
        	final SinkPO<?> po = new SinkPO(operator.getOutputSchema(), operator.getAdapter(),
                    operator.getOptionsMap());
        	defaultExecute(operator, po, config, true, true);
        }
        catch (final Exception e) {
            TSinkAORule.LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean isExecutable(final SinkAO operator, final TransformationConfiguration config) {
        return operator.isAllPhysicalInputSet();
    }

    @Override
    public String getName() {
        return "SinkAO -> SinkPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

}
