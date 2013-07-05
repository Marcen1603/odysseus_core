/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.relational.base.Relational;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TProjectAORule extends AbstractTransformationRule<ProjectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ProjectAO projectAO, TransformationConfiguration transformConfig) {
		RelationalProjectPO<?> projectPO = new RelationalProjectPO<IMetaAttribute>(projectAO.determineRestrictList());
		defaultExecute(projectAO, projectPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(ProjectAO operator, TransformationConfiguration transformConfig) {
		if (operator.isAllPhysicalInputSet()) {
			if (transformConfig.getDataTypes().contains(Relational.RELATIONAL)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProjectAO -> RelationalProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ProjectAO> getConditionClass() {
		return ProjectAO.class;
	}
}
