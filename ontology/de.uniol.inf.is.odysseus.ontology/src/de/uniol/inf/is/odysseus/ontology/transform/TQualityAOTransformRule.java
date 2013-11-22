/**
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
package de.uniol.inf.is.odysseus.ontology.transform;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ontology.logicaloperator.QualityAO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TQualityAOTransformRule extends
		AbstractTransformationRule<QualityAO> {
	/** The Logger. */
	private static final Logger LOG = LoggerFactory
			.getLogger(TQualityAOTransformRule.class);

	@Override
	public final int getPriority() {
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void execute(final QualityAO operator,
			final TransformationConfiguration transformConfig) {

		IPhysicalOperator mapPO;
		LOG.debug("Transform QualityAO to Map operator with expressions: "
				+ Arrays.toString(operator.getExpressions()));
		mapPO = new RelationalMapPO<IMetaAttribute>(operator.getInputSchema(),
				operator.getExpressions(), false, false);

		this.defaultExecute(operator, mapPO, transformConfig, true, true);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isExecutable(final QualityAO operator,
			final TransformationConfiguration transformConfig) {
		if (operator.isAllPhysicalInputSet()) {
			// if (operator.getInputSchema().getType() ==
			// ProbabilisticTuple.class) {
			return true;
			// }
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return "QualityAO -> QualityPO(probabilistic)";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
