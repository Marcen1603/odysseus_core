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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.aurorafactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.AbstractExecListScheduling;

public class AuroraMinCost extends AbstractExecListScheduling {

	public AuroraMinCost(IPhysicalQuery plan) {
		super(plan);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPhysicalQuery plan) {
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
		for (AbstractPhysicalSubscription<? extends ISource<?>,?> sub : sink.getSubscribedToSource()) {
			if (execList.contains(sub.getSource())) {
				continue;
			}
			if (!(sub.getSource().isSink())) {
				continue;
			}
            postOrderAdd((ISink<?>) sub.getSource(), execList);
		}
		if (sink instanceof IIterableSource<?> && this.getPlan().getIterableSources().contains(sink)) {
			execList.add((IIterableSource<?>) sink);
		}
	}
}
