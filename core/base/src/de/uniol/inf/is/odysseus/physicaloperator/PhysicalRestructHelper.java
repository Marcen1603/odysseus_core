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
package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.List;


/**
 * {@link PhysicalRestructHelper} provides methods to append, remove and replace
 * {@link IPhysicalOperator}s in a physical plan.
 * 
 * @author Tobias Witt
 * 
 */
@SuppressWarnings("unchecked")
public class PhysicalRestructHelper {
	
	public static void appendBinaryOperator(IPhysicalOperator binaryOp, IPhysicalOperator child1, IPhysicalOperator child2) {
		((ISink<?>)binaryOp).subscribeToSource((ISource)child1, 0, 0, child1.getOutputSchema());
		((ISink<?>)binaryOp).subscribeToSource((ISource)child2, 1, 0, child2.getOutputSchema());
	}
	
	public static void appendOperator(IPhysicalOperator parent, IPhysicalOperator child) {
		((ISink<?>)parent).subscribeToSource((ISource)child, 0, 0, child.getOutputSchema());
	}
	
	public static PhysicalSubscription<?> removeSubscription(IPhysicalOperator parent, IPhysicalOperator child) {
		for (PhysicalSubscription<?> sub : ((ISink<?>)parent).getSubscribedToSource()) {
			if (sub.getTarget() == child) {
				((ISink<?>)parent).unsubscribeFromSource((PhysicalSubscription)sub);
				return sub;
			}
		}
		return null;
	}
	
	public static void replaceChild(IPhysicalOperator parent, IPhysicalOperator child, IPhysicalOperator newChild) {
		PhysicalSubscription<?> sub = removeSubscription(parent, child);
		((ISink<?>)parent).subscribeToSource((ISource)newChild, sub.getSinkInPort(), sub.getSourceOutPort(),
				newChild.getOutputSchema());
	}
	
	public static void atomicReplaceSink(ISource<?> source, List<PhysicalSubscription<?>> remove, ISink<?> sink) {
		((ISource)source).atomicReplaceSink(remove, sink, 0, 0, source.getOutputSchema());
	}
	
	public static void atomicReplaceSink(ISource<?> source, PhysicalSubscription<?> remove, List<ISink<?>> sinks) {
		((ISource)source).atomicReplaceSink(remove, sinks, 0, 0, source.getOutputSchema());
	}
	
	public static void atomicReplaceSink(ISource<?> source, IPhysicalOperator remove, List<ISink<?>> sinks) {
		PhysicalSubscription<?> removeSub = null;
		for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
			if (sub.getTarget() == remove) {
				removeSub = sub;
				break;
			}
		}
		atomicReplaceSink(source, removeSub, sinks);
	}
	
}
