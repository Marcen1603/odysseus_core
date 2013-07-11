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

package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.probabilistic.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.ProbabilisticContinuousSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteSelectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for Select operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisiticContinuousSelectAORule extends AbstractTransformationRule<SelectAO> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return 11;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void execute(final SelectAO selectAO, final TransformationConfiguration transformConfig) {
		final IPhysicalOperator selectPO = new ProbabilisticContinuousSelectPO(selectAO.getInputSchema(), selectAO.getPredicate());
		if (selectAO.getHeartbeatRate() > 0) {
			((ProbabilisticDiscreteSelectPO<?>) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(selectAO.getHeartbeatRate()));
		}
		this.defaultExecute(selectAO, selectPO, transformConfig, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final SelectAO operator, final TransformationConfiguration transformConfig) {
		if (operator.isAllPhysicalInputSet()) {
			if (transformConfig.getDataTypes().contains(SchemaUtils.DATATYPE)) {
				if (SchemaUtils.containsContinuousProbabilisticAttributes(PredicateUtils.getAttributes(operator.getPredicate()))) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public final String getName() {
		return "SelectAO -> ProbabilisticDiscreteSelectPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
	 */
	@Override
	public final Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}

}
