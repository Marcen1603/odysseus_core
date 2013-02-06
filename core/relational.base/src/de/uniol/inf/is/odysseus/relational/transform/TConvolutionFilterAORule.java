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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ConvolutionFilterAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.ConvolutionFilterPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.relational.base.Relational;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TConvolutionFilterAORule extends AbstractTransformationRule<ConvolutionFilterAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(ConvolutionFilterAO operator, TransformationConfiguration transformConfig) {
		ConvolutionFilterPO<?> filter = new ConvolutionFilterPO<>(operator.getExpression(), operator.getAttributes(), operator.getSize());
		if (operator.getGroupingAttributes().size() > 0) {
			@SuppressWarnings("rawtypes")
			IGroupProcessor r = new RelationalGroupProcessor<>(operator.getOutputSchema(), operator.getOutputSchema(), operator.getGroupingAttributes(), null);
			filter.setGroupProcessor(r);
		}
		defaultExecute(operator, filter, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(ConvolutionFilterAO operator, TransformationConfiguration transformConfig) {
		if (transformConfig.getDataTypes().contains(Relational.RELATIONAL)) {
			return operator.isAllPhysicalInputSet();
		}
		return false;
	}

	@Override
	public String getName() {
		return "ConvolutionFilterAO -> ConvolutionFilterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super ConvolutionFilterAO> getConditionClass() {
		return ConvolutionFilterAO.class;
	}

}
