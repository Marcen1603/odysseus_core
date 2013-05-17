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
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.EMAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.EMPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TEMAORule extends AbstractTransformationRule<EMAO> {



	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(EMAO operator, TransformationConfiguration config) {
		IPhysicalOperator emPO = new EMPO<ITimeInterval>(operator.determineAttributesList(), operator.getMixtures());
		this.defaultExecute(operator, emPO, config, true, true);
	}

	@Override
	public boolean isExecutable(EMAO operator,
			TransformationConfiguration config) {
		if ((config.getDataTypes().contains(SchemaUtils.DATATYPE))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "EMAO -> EMPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super EMAO> getConditionClass() {
		return EMAO.class;
	}

}
