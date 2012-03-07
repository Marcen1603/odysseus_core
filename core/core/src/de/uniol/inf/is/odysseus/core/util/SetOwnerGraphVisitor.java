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
package de.uniol.inf.is.odysseus.core.util;

import java.util.List;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;

public class SetOwnerGraphVisitor<T extends IOwnedOperator> implements
		IGraphNodeVisitor<T, T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	List<T> visited;

	/**
	 * This is the first node, this visitor has ever seen. This is used to
	 * return as result, the same root in the copy of the plan as passed before
	 * copying.
	 */
	T root;

	private IOperatorOwner owner;

	public SetOwnerGraphVisitor(IOperatorOwner owner) {
		this.visited = new IdentityArrayList<T>();
		this.owner = owner;
	}

	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
	}

	@Override
	public T getResult() {
		return null;
	}

	@Override
	public void nodeAction(T op) {
		if (!this.visited.contains(op)) {
			if (owner != null){
				op.addOwner(owner);
			}else{
				op.removeAllOwners();
			}
			this.visited.add(op);
		}
	}

}
