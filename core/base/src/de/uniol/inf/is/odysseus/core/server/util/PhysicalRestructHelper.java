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
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
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
	
	public static void appendBinaryOperator(IPhysicalOperator binaryOp, ISource child1, ISource child2) {
		((ISink<?>)binaryOp).subscribeToSource((ISource)child1, 0, 0, child1.getOutputSchema());
		((ISink<?>)binaryOp).subscribeToSource((ISource)child2, 1, 0, child2.getOutputSchema());
		
//		child1.connectSink(binaryOp, 0, 0, child1.getOutputSchema());
//		child2.connectSink(binaryOp, 1, 0, child2.getOutputSchema());
	}
	
	public static void appendOperator(IPhysicalOperator parent, ISource child) {
		// first unsubscribe from original source and then subscribe to new source. and new source must subscribe to original source.
		((ISink<?>)parent).subscribeToSource((ISource)child, 0, 0, child.getOutputSchema());
		
//		child.connectSink(parent, 0, 0, child.getOutputSchema());
	}
	
	public static PhysicalSubscription<?> removeSubscription(IPhysicalOperator parent, IPhysicalOperator child) {
		for (PhysicalSubscription<?> sub : ((ISink<?>)parent).getSubscribedToSource()) {
			if (sub.getTarget().equals(child)) {
				((ISink<?>)parent).unsubscribeFromSource((PhysicalSubscription)sub);
				return sub;
			}
		}
		return null;
	}
	
	/**
	 * Insert the Operator buffer between source and sinks.
	 * @param source
	 * @param sourceOutPort
	 * @param sinks list of appended sinks to the source.
	 * @param sinkInPorts list of sinkinports according to each sink.
	 * @param buffer
	 */
	@Deprecated
	public static <T extends IPipe> void insertOperator(ISource source, int sourceOutPort, List<ISink> sinks, List<Integer> sinkInPorts, T buffer) {
		if(sinks.size() != sinkInPorts.size()) {
			throw new IllegalArgumentException("Amount of sinks and sinkinports must be equal");
		}
		for(int i = 0; i < sinks.size(); i++) {
			// remove old connections
			source.disconnectSink(sinks.get(i), sinkInPorts.get(i), sourceOutPort, source.getOutputSchema());
		}
		// create new connections
		source.connectSink(buffer, 0, sourceOutPort, source.getOutputSchema());
		for(int i = 0; i < sinks.size(); i++) {
			buffer.connectSink(sinks.get(i), sinkInPorts.get(i), 0, buffer.getOutputSchema());
		}
	}
	
	/**
	 * Insert the operator buffer between source and sink.
	 * @param source
	 * @param sourceOutPort
	 * @param sink
	 * @param sinkInPort
	 * @param buffer
	 */
	@Deprecated
	public static <T extends IPipe> void insertOperator(ISource source, int sourceOutPort, ISink sink, int sinkInPort, T buffer) {
		// Alte Verbindung entfernen
		source.disconnectSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema());

		// Neue Verbindung erstellen
		source.connectSink(buffer, 0, sourceOutPort, source.getOutputSchema());
		buffer.connectSink(sink, sinkInPort, 0, buffer.getOutputSchema());
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
		
		Set<PhysicalSubscription<?>> toSinks = new HashSet<PhysicalSubscription<?>>();
		Set<PhysicalSubscription<?>> toBuffer = new HashSet<PhysicalSubscription<?>>();
		for(PhysicalSubscription<?> sub : ((ISource<?>)source).getSubscriptions()) {
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
	
	/**
	 * Remove the operator which is between source and sink.
	 * @param source
	 * @param sourceOutPort
	 * @param sink
	 * @param sinkInPort
	 * @param buffer
	 */
	public static <T extends IPipe> void removeOperator(ISource source, int sourceOutPort, ISink sink, int sinkInPort, T buffer) {
		// Alte Verbindungen trennen
		buffer.disconnectSink(sink, sinkInPort, 0, buffer.getOutputSchema());
		source.disconnectSink(buffer, 0, sourceOutPort, source.getOutputSchema());

		// Neue (alte) Verbindung herstellen
		source.connectSink(sink, sinkInPort, sourceOutPort, source.getOutputSchema());
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
