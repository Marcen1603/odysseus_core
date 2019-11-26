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
package de.uniol.inf.is.odysseus.transform.rules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.OutputConnectorAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.OutputConnectorPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Marco Grawunder
 */
public class TOutputConnectorAORule extends AbstractTransformationRule<OutputConnectorAO> {
	
	static Logger LOG = LoggerFactory.getLogger(TOutputConnectorAORule.class);
	static InfoService infoService = InfoServiceFactory.getInfoService(TOutputConnectorAORule.class);


	@SuppressWarnings("rawtypes")
	@Override
	public void execute(OutputConnectorAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new OutputConnectorPO(operator.getPort()), config, true, true);
	}

	@Override
	public boolean isExecutable(OutputConnectorAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super OutputConnectorAO> getConditionClass() {
		return OutputConnectorAO.class;
	}

}
