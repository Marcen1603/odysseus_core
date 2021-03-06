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
package de.uniol.inf.is.odysseus.scheduler.strategy.factory.simplestratfactory.impl;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.scheduler.strategy.AbstractExecListScheduling;

/**
 * Implements round robin scheduling of operators.
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RoundRobinScheduling extends AbstractExecListScheduling {

	public RoundRobinScheduling(IPhysicalQuery operators) {
		super(operators);
	}

	@Override
	protected List<IIterableSource<?>> calculateExecutionList(IPhysicalQuery operators) {
		return new LinkedList<IIterableSource<?>>(operators.getIterableSources());
	}
	
	@Override
	public void applyChangedPlan()
	{
//		System.out.println("RoundRobinStrategy: Updating changed Plan");
		this.executionList = this.calculateExecutionList(this.getPlan());
		this.iterator = this.executionList.iterator();
	}

}
