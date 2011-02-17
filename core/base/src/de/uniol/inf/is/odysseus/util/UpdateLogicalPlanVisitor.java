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
package de.uniol.inf.is.odysseus.util;

import java.util.Map;

import de.uniol.inf.is.odysseus.ISubscriber;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;

public class UpdateLogicalPlanVisitor
		implements
		INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, ILogicalOperator> {
	
	private Map<ILogicalOperator, ILogicalOperator> replaced;

	public UpdateLogicalPlanVisitor(
			Map<ILogicalOperator, ILogicalOperator> replaced) {
		this.replaced = replaced;
	}

	@Override
	public void ascendAction(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		if (to != null ) {
			((ILogicalOperator) to).updateAfterClone(replaced);
		}
	}

	@Override
	public void descendAction(ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		if (to != null ) {
			((ILogicalOperator) to).updateAfterClone(replaced);
		}
	}

	@Override
	public ILogicalOperator getResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> op) {
		// TODO Auto-generated method stub

	}

}
