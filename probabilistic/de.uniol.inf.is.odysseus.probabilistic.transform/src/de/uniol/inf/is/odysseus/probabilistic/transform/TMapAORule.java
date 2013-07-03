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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.ProbabilisticContinuousMapPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Transformation rule for probabilistic Map operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TMapAORule extends AbstractTransformationRule<MapAO> {
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
	@Override
	public final void execute(final MapAO mapAO, final TransformationConfiguration transformConfig) {
		final SDFProbabilisticExpression[] expressions = new SDFProbabilisticExpression[mapAO.getExpressions().size()];
		for (int i = 0; i < expressions.length; i++) {
			expressions[i] = new SDFProbabilisticExpression(mapAO.getExpressions().get(i));
		}
		final IPhysicalOperator mapPO = new ProbabilisticContinuousMapPO<IMetaAttribute>(mapAO.getInputSchema(), expressions);

		this.defaultExecute(mapAO, mapPO, transformConfig, true, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang.Object, java.lang.Object)
	 */
	@Override
	public final boolean isExecutable(final MapAO operator, final TransformationConfiguration transformConfig) {
		if ((transformConfig.getDataTypes().contains(SchemaUtils.DATATYPE)) && (this.isProbabilistic(operator))) {
			if (operator.getPhysSubscriptionTo() != null) {
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
		return "MapAO -> ProbabilisticMapPO";
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
	public final Class<? super MapAO> getConditionClass() {
		return MapAO.class;
	}

	/**
	 * Checks whether at least one attribute in the expressions is a probabilistic attribute.
	 * 
	 * @param mapAO
	 *            The map operator
	 * @return <code>true</code> iff at least one attribute in the expressions is a probabilistic attribute
	 */
	private boolean isProbabilistic(final MapAO mapAO) {
		boolean isProbabilistic = false;
		for (final SDFExpression expression : mapAO.getExpressions()) {
			final List<SDFAttribute> attributes = expression.getAllAttributes();

			for (final SDFAttribute attribute : attributes) {
				if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
					isProbabilistic = true;
				}
			}
		}
		return isProbabilistic;

	}
}
