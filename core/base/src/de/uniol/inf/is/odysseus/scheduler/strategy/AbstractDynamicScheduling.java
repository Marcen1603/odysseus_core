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
package de.uniol.inf.is.odysseus.scheduler.strategy;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.IIterableSource;
import de.uniol.inf.is.odysseus.planmanagement.plan.IPartialPlan;

abstract public class AbstractDynamicScheduling extends
		AbstractScheduling {

	protected List<IIterableSource<?>> operators;
	
	public AbstractDynamicScheduling(IPartialPlan plan) {
		super(plan);
		operators = new LinkedList<IIterableSource<?>>(plan.getIterableSources());
	}
	
	@Override
	public boolean isDone() {
		return operators.isEmpty();
	}

	@Override
	public void sourceDone(IIterableSource<?> source) {
		operators.remove(source);
	}


}
