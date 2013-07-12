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
package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticAO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticFactory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisticAORule extends AbstractTransformationRule<ProbabilisticAO> {

	@Override
	public final int getPriority() {
		return 1;
	}

	@Override
	public final void execute(final ProbabilisticAO operator, final TransformationConfiguration config) {
		// FIXME Why do I do that?? (CK)
		// Maybe I wanted to transfer the mean and covariance as vector/matrix in the payload? (CK)
		final int[] pos = SchemaUtils.getAttributePos(operator.getInputSchema(), operator.getAttributes());
		final ProbabilisticFactory mUpdater = new ProbabilisticFactory(pos);
		final MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<IProbabilistic, Tuple<? extends IProbabilistic>>(mUpdater);
		this.defaultExecute(operator, po, config, true, true);

	}

	@Override
	public final boolean isExecutable(final ProbabilisticAO operator, final TransformationConfiguration config) {
		if (operator.isAllPhysicalInputSet()) {	if (config.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final String getName() {
		return "ProbabilisticAO -> MetadataUpdatePO(probabilistic)";
	}

	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public final Class<? super ProbabilisticAO> getConditionClass() {
		return ProbabilisticAO.class;
	}
}
