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
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;


/**
 * {@link PhysicalRestructHelper} provides methods to append, remove and replace
 * {@link IPhysicalOperator}s in a physical plan.
 * 
 * @author Tobias Witt, Merlin Wasmann, Timo Michelsen
 * 
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PhysicalRestructHelper {
	
	/**
	 * Appends the binaryOp to both childs with input ports 0 -> child1, 1 -> child2.
	 * Happens via subscribeToSource.
	 * 
	 * @param binaryOp
	 * @param child1
	 * @param child2
	 */
	public static void appendBinaryOperator(IPhysicalOperator binaryOp, ISource child1, ISource child2) {
		((ISink<?>)binaryOp).subscribeToSource(child1, 0, 0, child1.getOutputSchema());
		((ISink<?>)binaryOp).subscribeToSource(child2, 1, 0, child2.getOutputSchema());
	}
	
	/**
	 * Appends the parent to the child via subscribeToSource.
	 * 
	 * @param parent
	 * @param child
	 */
	public static void appendOperator(IPhysicalOperator parent, ISource child) {
		((ISink<?>)parent).subscribeToSource(child, 0, 0, child.getOutputSchema());		
	}
	
	/**
	 * Unsubscribes the the parent from the child and returns the subscription.
	 * 
	 * @param parent
	 * @param child
	 * @return Subscription parent -> child
	 */
	public static AbstractPhysicalSubscription<?> removeSubscription(IPhysicalOperator parent, IPhysicalOperator child) {
		for (AbstractPhysicalSubscription<?> sub : ((ISink<?>)parent).getSubscribedToSource()) {
			if (sub.getTarget().equals(child)) {
				((ISink<?>)parent).unsubscribeFromSource((AbstractPhysicalSubscription)sub);
				return sub;
			}
		}
		return null;
	}

	/**
	 * Remove the operator which is between source and sinks.
	 * @param source
	 * @param sourceOutPort
	 * @param sinks
	 * @param sinkInPorts
	 * @param buffer
	 */
	public static <T extends IPipe> void removeOperator(ISource source,
			int sourceOutPort, List<ISink> sinks, List<Integer> sinkInPorts,
			T buffer) {
		if (sinks.size() != sinkInPorts.size()) {
			throw new IllegalArgumentException(
					"Amount of sinks and sinkinports must be equal");
		}

		// disconnect old connections
		for (int i = 0; i < sinks.size(); i++) {
			buffer.unsubscribeSink(sinks.get(i), sinkInPorts.get(i), 0, buffer.getOutputSchema());
		}
		
		for (int i = 0; i < sinks.size(); i++) {
			source.subscribeSink(sinks.get(i), sinkInPorts.get(i),
					sourceOutPort, source.getOutputSchema());
		}
		
		Set<AbstractPhysicalSubscription<?>> toSinks = new HashSet<AbstractPhysicalSubscription<?>>();
		Set<AbstractPhysicalSubscription<?>> toBuffer = new HashSet<AbstractPhysicalSubscription<?>>();
		for(AbstractPhysicalSubscription<?> sub : ((ISource<?>)source).getSubscriptions()) {
			if(sub.getTarget().equals(buffer)) {
				toBuffer.add(sub);
				continue;
			}
			if(sinks.contains(sub.getTarget())) {
				toSinks.add(sub);
			}
		}
		((AbstractSource) source).replaceActiveSubscriptions(toBuffer, toSinks);
		
		source.unsubscribeSink(buffer, 0, sourceOutPort, source.getOutputSchema());
	}
	
	public static void replaceChild(IPhysicalOperator parent, IPhysicalOperator child, IPhysicalOperator newChild) {
		AbstractPhysicalSubscription<?> sub = removeSubscription(parent, child);
		((ISink<?>)parent).subscribeToSource((ISource)newChild, sub.getSinkInPort(), sub.getSourceOutPort(),
				newChild.getOutputSchema());
	}
	
//	public static void atomicReplaceSink(ISource<?> source, List<PhysicalSubscription<?>> remove, ISink<?> sink) {
//		((ISource)source).atomicReplaceSink(remove, sink, 0, 0, source.getOutputSchema());
//	}
//	
//	public static void atomicReplaceSink(ISource<?> source, PhysicalSubscription<?> remove, List<ISink<?>> sinks) {
//		((ISource)source).atomicReplaceSink(remove, sinks, 0, 0, source.getOutputSchema());
//	}
//	
//	public static void atomicReplaceSink(ISource<?> source, IPhysicalOperator remove, List<ISink<?>> sinks) {
//		PhysicalSubscription<?> removeSub = null;
//		for (PhysicalSubscription<?> sub : source.getSubscriptions()) {
//			if (sub.getTarget() == remove) {
//				removeSub = sub;
//				break;
//			}
//		}
//		atomicReplaceSink(source, removeSub, sinks);
//	}
	
}
