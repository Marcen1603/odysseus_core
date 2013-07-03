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

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.DiscreteProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for probabilistic Select operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TSelectAORule extends AbstractTransformationRule<SelectAO> {
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public final int getPriority() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void execute(final SelectAO selectAO, final TransformationConfiguration transformConfig) {
		IPhysicalOperator selectPO;
		if (this.isProbabilistic(selectAO)) {
			final SDFProbabilisticExpression expression = new SDFProbabilisticExpression(((RelationalPredicate) selectAO.getPredicate()).getExpression());
			final ProbabilisticPredicate predicate = new ProbabilisticPredicate(expression);
			selectPO = new DiscreteProbabilisticSelectPO(predicate);
			if (selectAO.getHeartbeatRate() > 0) {
				((DiscreteProbabilisticSelectPO<?>) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(selectAO.getHeartbeatRate()));
			}
		} else {
			selectPO = new SelectPO(selectAO.getPredicate());
			if (selectAO.getHeartbeatRate() > 0) {
				((SelectPO) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(selectAO.getHeartbeatRate()));
			}
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
		if (transformConfig.getDataTypes().contains(SchemaUtils.DATATYPE)) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
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
		return "SelectAO -> ProbabilisticSelectPO";
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

	/**
	 * Checks whether at least one attribute in the select predicate is a probabilistic attribute.
	 * 
	 * @param selectAO
	 *            The select operator
	 * @return <code>true</code> iff at least one attribute in the select predicate is a probabilistic attribute
	 */
	private boolean isProbabilistic(final SelectAO selectAO) {
		final List<SDFAttribute> attributes = selectAO.getPredicate().getAttributes();

		boolean isProbabilistic = false;
		for (final SDFAttribute attribute : attributes) {
			if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
				isProbabilistic = true;
			}
		}
		return isProbabilistic;
	}
}
