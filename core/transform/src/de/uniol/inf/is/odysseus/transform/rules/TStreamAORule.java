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
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.ITransformation;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.engine.TransformationExecutor;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStreamAORule extends AbstractTransformationRule<StreamAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(StreamAO operator,
			TransformationConfiguration transformConfig) {
		ISource<?> source = null;
		// check, if this stream is not transformed yet into a physical or not
		if (getDataDictionary().getAccessPlan(operator.getStreamname()) == null) {
			// ok, we have to transform the stream operator!
			ILogicalOperator logicalPlan = getDataDictionary()
					.getStreamForTransformation(operator.getStreamname(),
							getCaller());
			// start a new sub-transformation
			ITransformation transformation = new TransformationExecutor();
			ArrayList<IPhysicalOperator> roots = transformation.transform(
					logicalPlan, transformConfig, getCaller(),
					getDataDictionary());
			// we don't need the subscriptions anymore
			logicalPlan.clearPhysicalSubscriptions();
			// get the first root, since this is the physical operator for the
			// passed plan
			// and this will be the connection to the current plan.
			if (roots.get(0).isSource()) {
				source = (ISource<?>) roots.get(0);
				// insert the new source for further transformations
				insert(source);
				// and add this to the accessplan
				if (!transformConfig.isVirtualTransformation()) {
					getDataDictionary().putAccessPlan(operator.getStreamname(),
							source);
				}
			} else {
				throw new RuntimeException(
						"Cannot transform view: Root of view plan is no source. 0="
								+ roots.get(0) + " list=" + roots);
			}
		}
		// finally, there must be a physical transformed stream in the data
		// dictionary,
		// so use this and transform the operator!
		ISource<?> po = null;
		if (transformConfig.isVirtualTransformation()) {
			po = source;
		} else {
			po = getDataDictionary().getAccessPlan(operator.getStreamname());
		}
		defaultExecute(operator, po, transformConfig, true, true, false);
		// po.setName(operator.getStreamname());

	}

	@Override
	public boolean isExecutable(StreamAO operator,
			TransformationConfiguration transformConfig) {
		Resource name = operator.getStreamname();
		return getDataDictionary().containsViewOrStream(name, getCaller());
	}

	@Override
	public String getName() {
		return "Transform View or Stream";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<? super StreamAO> getConditionClass() {
		return StreamAO.class;
	}

}
