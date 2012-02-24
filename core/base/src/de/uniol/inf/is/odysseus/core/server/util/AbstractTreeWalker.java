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
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;

/**
 * Should only be used on graphs
 *  
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class AbstractTreeWalker {
	
	public static <R,T extends ISubscriber, H extends ISubscription<T>> R prefixWalk(ISubscriber<T, H> node, INodeVisitor<ISubscriber<T, H>, R> visitor) {
		visitor.nodeAction(node);
//		if (!(node instanceof ISubscriber)){
//			return null;
//		}
		for (H s:node.getSubscribedToSource()){
			visitor.descendAction(s.getTarget());
			prefixWalk(s.getTarget(), visitor);
			visitor.ascendAction(node);
		}
		return visitor.getResult();
	}
	
	public static <R> R prefixWalk2(IPhysicalOperator node, INodeVisitor<IPhysicalOperator,R> visitor) {
		visitor.nodeAction(node);
		if (!node.isSink()) {
			return null;
		}
		for (PhysicalSubscription<?> s : ((ISink<?>)node).getSubscribedToSource()){
			IPhysicalOperator t = (IPhysicalOperator) s.getTarget();
			visitor.descendAction(t);
			prefixWalk2(t, visitor);
			visitor.ascendAction(node);
		}
		return visitor.getResult();
	}

//	public static <T> T postfixWalk(ILogicalOperator op, INodeVisitor<ILogicalOperator, T> visitor) {
//		for (int i= 0; i < op.getNumberOfInputs(); ++ i) {
//			ILogicalOperator inputAO = op.getInputAO(i);
//			visitor.descend(inputAO);
//			prefixWalk(inputAO, visitor);
//			visitor.ascend(op);
//		}
//		visitor.node(op);
//		return visitor.getResult();
//	}
}
