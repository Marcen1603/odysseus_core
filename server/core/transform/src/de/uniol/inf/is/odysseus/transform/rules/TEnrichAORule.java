/********************************************************************************** 
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
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.EnrichAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.EnrichPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Dennis Geesen
 *
 */
public class TEnrichAORule extends AbstractTransformationRule<EnrichAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(EnrichAO operator, TransformationConfiguration config) {
		EnrichPO<IStreamObject<IMetaAttribute>, IMetaAttribute> po = new EnrichPO<IStreamObject<IMetaAttribute>, IMetaAttribute>(operator.getPredicate(), operator.getMinimumSize());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(EnrichAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "EnrichAO -> EnrichPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super EnrichAO> getConditionClass() {
		return EnrichAO.class;
	}

}
