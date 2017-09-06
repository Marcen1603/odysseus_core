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
package de.uniol.inf.is.odysseus.core.util;

import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.collection.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;

//public class AbstractGraphWalker<R, S extends ISubscriber<S, H> & ISubscribable<S, H>, H extends ISubscription<H,S>>{
public class AbstractGraphWalker<R, S extends ISubscriber<S, ISubscription<S, ?>> & ISubscribable<S, ISubscription<?, S>>, H extends ISubscription<H, I>, I extends ISubscription<I, H>> {

	List<S> visited;
	List<IPhysicalOperator> visitedPhysical;

	public AbstractGraphWalker() {
		this.visited = new IdentityArrayList<S>();
		this.visitedPhysical = new IdentityArrayList<IPhysicalOperator>();
	}

	public void clearVisited() {
		this.visited = new IdentityArrayList<S>();
		this.visitedPhysical = new IdentityArrayList<IPhysicalOperator>();
	}

	/**
	 * ascendAction() and descendAction() cannot be used in this graph walker. This
	 * is because, we have a directed graph. Some nodes are reachable only against
	 * data direction and other nodes are only reachable in data data direction.
	 * 
	 * Assume the following graph:
	 * 
	 * A <- B -> C
	 * 
	 * The arrows mark the data direction of the operators. If we only know A, we
	 * can reach B only against data direction and C only in data direction. Have in
	 * mind, that subscriptions can be traversed in both directions, but they have a
	 * semantically determined data direction.
	 * 
	 * @param node
	 * @param visitor
	 * @return
	 */
	public /* <R,T extends ISubscriber, H extends ISubscription<T>> */ void prefixWalk(S node,
			IGraphNodeVisitor<S, R> visitor) {
		if (this.visited.contains(node)) {
			return;
		}

		this.visited.add(node);

		visitor.nodeAction(node);

		for (ISubscription<S, ?> s : node.getSubscribedToSource()) {
			visitor.beforeFromSinkToSourceAction(node, s.getSource());
			prefixWalk(s.getSource(), visitor);
			visitor.afterFromSinkToSourceAction(node, s.getSource());
		}

		for (ISubscription<?, S> s : node.getSubscriptions()) {
			visitor.beforeFromSourceToSinkAction(node, s.getSink());
			prefixWalk(s.getSink(), visitor);
			visitor.afterFromSourceToSinkAction(node, s.getSink());
		}
	}

	public void prefixWalkPhysical(IPhysicalOperator node, IGraphNodeVisitor<IPhysicalOperator, R> visitor) {
		if (this.visitedPhysical.contains(node)) {
			return;
		}

		this.visitedPhysical.add(node);

		visitor.nodeAction(node);

		if (node.isSink()) {
			for (AbstractPhysicalSubscription<?, ?> s : ((ISink<?>) node).getSubscribedToSource()) {
				IPhysicalOperator t = (IPhysicalOperator) s.getSource();
				visitor.beforeFromSinkToSourceAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSinkToSourceAction(node, t);
			}
		}

		if (node.isSource()) {
			for (AbstractPhysicalSubscription<?, ?> s : ((ISource<?>) node).getSubscriptions()) {
				IPhysicalOperator t = (IPhysicalOperator) s.getSink();
				visitor.beforeFromSourceToSinkAction(node, t);
				this.prefixWalkPhysical(t, visitor);
				visitor.afterFromSourceToSinkAction(node, t);
			}
		}
	}

}
