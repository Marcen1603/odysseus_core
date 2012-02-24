/** Copyright [2011] [The Odysseus Team]
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

package de.uniol.inf.is.odysseus.spatial.grid.transform;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.grid.logicaloperator.VisualGridSinkAO;
import de.uniol.inf.is.odysseus.spatial.grid.physicaloperator.VisualGridSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TVisualGridSinkAORule extends
		AbstractTransformationRule<VisualGridSinkAO> {
	private static Logger LOG = LoggerFactory
			.getLogger(TVisualGridSinkAORule.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	public void execute(final VisualGridSinkAO visualGridSinkAO,
			final TransformationConfiguration transformConfig) {
		try {
			final VisualGridSinkPO visualGridSinkPO = new VisualGridSinkPO(
					visualGridSinkAO.getOutputSchema());
			visualGridSinkPO
					.setOutputSchema(visualGridSinkAO.getOutputSchema());

			Collection<ILogicalOperator> toUpdate = transformConfig
					.getTransformationHelper().replace(visualGridSinkAO,
							visualGridSinkPO);
			for (ILogicalOperator o : toUpdate) {
				update(o);
			}
			retract(visualGridSinkAO);
		} catch (final Exception e) {
			TVisualGridSinkAORule.LOG.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
	 */
	@Override
	public String getName() {
		return "VisualGridSinkAO -> VisualGridSinkPO";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
	 */
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean isExecutable(VisualGridSinkAO operator,
			TransformationConfiguration transformConfig) {
		if (transformConfig.getDataType().equals("relational")) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Class<?> getConditionClass() {
		return VisualGridSinkAO.class;
	}
}
