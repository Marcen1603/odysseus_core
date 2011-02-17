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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;
import de.uniol.inf.is.odysseus.scheduler.strategy.AbstractExecListScheduling;

public class AuroraMinCost extends AbstractExecListScheduling {

	public AuroraMinCost(IPartialPlan plan) {
		super(plan);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPartialPlan plan) {
		List<IIterableSource<?>> execList = new LinkedList<IIterableSource<?>>();
		for (IPhysicalOperator curRoot : plan.getRoots()) {
			if(curRoot.isSink()){
				postOrderAdd((ISink<?>)curRoot, execList);
			}
		}
		return execList;
	}
	
	@Override
	public void applyChangedPlan()
	{
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

	private void postOrderAdd(ISink<?> sink, List<IIterableSource<?>> execList) {
		for (PhysicalSubscription<? extends ISource<?>> sub : sink.getSubscribedToSource()) {
			if (execList.contains(sub.getTarget())) {
				continue;
			}
			if (!(sub.getTarget() instanceof ISink<?>)) {
				continue;
			} else {
				postOrderAdd((ISink<?>) sub.getTarget(), execList);
			}
		}
		if (sink instanceof IIterableSource<?> && this.getPlan().getIterableSources().contains(sink)) {
			execList.add((IIterableSource<?>) sink);
		}
	}
}
