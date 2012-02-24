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
package de.uniol.inf.is.odysseus.core.server.util;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;

public class SetOwnerVisitor implements INodeVisitor<ISubscriber<ILogicalOperator, LogicalSubscription>, Void> {

	private IOperatorOwner owner;

	public SetOwnerVisitor(IOperatorOwner owner) {
		this.owner = owner;
	}
	
	@Override
	public void ascendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void descendAction(
			ISubscriber<ILogicalOperator, LogicalSubscription> to) {
		
	}

	@Override
	public void nodeAction(ISubscriber<ILogicalOperator, LogicalSubscription> op) {
		((IOwnedOperator)op).addOwner(owner);
	}

	@Override
	public Void getResult() {
		return null;
	}

}
