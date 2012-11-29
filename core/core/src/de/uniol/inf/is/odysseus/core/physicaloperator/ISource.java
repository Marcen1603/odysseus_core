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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.metadata.IHasMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * Interface for data sources in a query graph.
 * 
 * It provides data to subscribed {@link ISink sinks}. Subscriptions are managed
 * via {@link #subscribe(ISink, int)} and {@link #unsubscribe(ISink, int)}.
 * Subscriptions can be added/removed at any time (before or after
 * {@link #open()} was called).
 * 
 * @author Jonas Jacobi, Tobias Witt
 */
public interface ISource<T> extends IPhysicalOperator,
		ISubscribable<ISink<? super T>, PhysicalSubscription<ISink<? super T>>>, IHasMetaAttribute {
	/**
	 * Gets called initially once from every subscribed sink. Setup work should
	 * be done in here.
	 * caller: who called open
	 * sourcePort: on which output port of the source 
	 * sinkPort: on which inport port of the sink
	 * callPath: Is needed to cope with cycles in the graph
	 * 
	 * @throws OpenFailedException
	 *             if the source can't be initialised e.g. because some needed
	 *             resources like socket connections can't be allocated.
	 */
	public void open(ISink<? super T> caller, int sourcePort, int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath) throws OpenFailedException;

	/**
	 * Calls {@link ISink#process(T)} on all subscribed {@link ISink sinks}.
	 * 
	 * @param object
	 *            the parameter for processNext.
	 */
	public void transfer(T object, int sourceOutPort);

	public void transfer(T object);

	/**
	 * Same as above, but for transfering a batch of elements.
	 */
	public void transfer(Collection<T> object, int sourceOutPort);

	public void transfer(Collection<T> object);

	/**
	 * Close down the connection/do not read any more data
	 */
	public void close(ISink<? super T> caller, int sourcePort, int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath);

	public void sendPunctuation(IPunctuation punctuation);

	public void sendPunctuation(IPunctuation punctuation, int outPort);

	/**
	 * Removes several subscriptions in remove list to this source and
	 * subscribes a sink in one 'atomic' step, so that no transfer() can be
	 * executed between these steps.
	 * 
	 * @param remove
	 *            {@link List} of {@link PhysicalSubscription}s to remove.
	 * @param sink
	 *            new {@link ISink} to subscribe.
	 * @param sinkInPort
	 *            sinkInPort.
	 * @param sourceOutPort
	 *            sourceOutPort.
	 * @param schema
	 *            Output schema of source.
	 */
	public void atomicReplaceSink(
			List<PhysicalSubscription<ISink<? super T>>> remove,
			ISink<? super T> sink, int sinkInPort, int sourceOutPort,
			SDFSchema schema);

	/**
	 * Removes a subscription to this source and subscribes several sinks in the
	 * sinks list in one 'atomic' step, so that no transfer() can be executed
	 * between these steps.
	 * 
	 * @param remove
	 *            {@link PhysicalSubscription} to remove.
	 * @param sink
	 *            {@link List} of new {@link ISink}s to subscribe.
	 * @param sinkInPort
	 *            sinkInPort.
	 * @param sourceOutPort
	 *            sourceOutPort.
	 * @param schema
	 *            Output schema of source.
	 */
	public void atomicReplaceSink(
			PhysicalSubscription<ISink<? super T>> remove,
			List<ISink<? super T>> sinks, int sinkInPort, int sourceOutPort,
			SDFSchema schema);

	/**
	 * Methods to mark (!) Operator as blocked. Can be used by scheduler
	 */
	void unblock();
	void block();
	boolean isBlocked();

	public void unsubscribeFromAllSinks();

}
