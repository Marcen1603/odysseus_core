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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.base.physicaloperator.SourcePO;
import de.uniol.inf.is.odysseus.wrapper.base.pool.SourcePool;

public class TAccessAO2SourceRule extends AbstractTransformationRule<AccessAO> {
	private static Logger LOG = LoggerFactory
			.getLogger(TAccessAO2SourceRule.class);

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(final AccessAO operator,
			final TransformationConfiguration config) {
		try {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			SourcePO<?> po = new SourcePO(operator.getOutputSchema(),
					operator.getWrapper(), operator.getOptionsMap());

			SourcePool.registerSource(operator.getWrapper(), po,
					operator.getOptionsMap());
			defaultExecute(operator, po, config, true, true);
		} catch (final Exception e) {
			TAccessAO2SourceRule.LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean isExecutable(final AccessAO operator,
			final TransformationConfiguration config) {
		// TODO: Remove hard coded google
		return (operator.getWrapper() != null && !"GoogleProtoBuf"
				.equalsIgnoreCase(operator.getWrapper()));
	}

	@Override
	public String getName() {
		return "AccessAO -> SourcePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
