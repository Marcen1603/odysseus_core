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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;

public class CopyLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	Map<T, T> nodeCopies;

	/**
	 * This is the first node, this visitor has ever seen. This is used to
	 * return as result, the same root in the copy of the plan as passed before
	 * copying.
	 */
	T root;

	private IOperatorOwner owner;

	public CopyLogicalGraphVisitor(IOperatorOwner owner) {
		this.nodeCopies = new IdentityHashMap<T, T>();
		this.owner = owner;
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public T getResult() {
		// copy the logical plan
		for (Entry<T, T> entry : nodeCopies.entrySet()) {
			T original = entry.getKey();
			T copy = entry.getValue();

			// it should be enough, to copy only
			// the incoming subscriptions, since
			// for each incoming subscription there
			// also exists an outgoing in the preceding
			// operator.
			for (LogicalSubscription sub : original.getSubscribedToSource()) {
				// get the copy of each target
				T targetCopy = this.nodeCopies.get(sub.getTarget());
				// subscribe the target copy to the node copy
				// if its in the same plan, add copy
				if (targetCopy != null) {
					copy.subscribeToSource(targetCopy, sub.getSinkInPort(),
							sub.getSourceOutPort(), sub.getSchema());			
				}
			}
		}

		return this.nodeCopies.get(this.root);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nodeAction(T op) {
		// Copy Plan only for owners (if available)
		if (!op.hasOwner() || op.isOwnedBy(owner)) {
			if (this.root == null) {
				this.root = op;
			}
			if (!this.nodeCopies.containsKey(op)) {
				T opCopy = (T) op.clone();
				opCopy.clearPhysicalSubscriptions();
				this.nodeCopies.put(op, opCopy);
			}
		}
	}

}
