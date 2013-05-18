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

import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticValidatorRule extends AbstractTransformationRule<IHasMetadataMergeFunction<?>> {

	@Override
	public final int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void execute(final IHasMetadataMergeFunction<?> operator, final TransformationConfiguration config) {
		if (!((CombinedMergeFunction) operator.getMetadataMerge()).providesMergeFunctionFor(IProbabilistic.class)) {
			// TODO: Make logger
			System.err.println(this + " WARN: No Probabilistic merge function set for " + operator);
		}
	}

	@Override
	public final boolean isExecutable(final IHasMetadataMergeFunction<?> operator, final TransformationConfiguration config) {
		if (operator.getMetadataMerge() instanceof CombinedMergeFunction) {
			if (config.getMetaTypes().contains(IProbabilistic.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final String getName() {
		return "Probabilistic Validation";
	}

	@Override
	public final IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.VALIDATE;
	}

}
