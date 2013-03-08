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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticProjectPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TProjectAORule extends AbstractTransformationRule<ProjectAO> {
	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(final ProjectAO projectAO,
			final TransformationConfiguration transformConfig) {
		IPhysicalOperator projectPO = new ProbabilisticProjectPO<IMetaAttribute>(
				projectAO.getInputSchema(), projectAO.determineRestrictList());
		this.defaultExecute(projectAO, projectPO, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(final ProjectAO operator,
			final TransformationConfiguration transformConfig) {
		if ((transformConfig.getDataTypes().contains(TransformUtil.DATATYPE))
				&& (isContinuous(operator))) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "ProjectAO -> ProbabilisticProjectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ProjectAO> getConditionClass() {
		return ProjectAO.class;
	}

	private boolean isContinuous(final ProjectAO projectAO) {
		final SDFSchema schema = projectAO.getInputSchema();

		final int[] restrictList = projectAO.determineRestrictList();
		boolean isContinuous = false;
		for (final int index : restrictList) {
			final SDFAttribute attribute = schema.getAttribute(index);
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				if (((SDFProbabilisticDatatype) attribute.getDatatype())
						.isContinuous()) {
					isContinuous = true;
				}
			}
		}
		return isContinuous;
	}
}
