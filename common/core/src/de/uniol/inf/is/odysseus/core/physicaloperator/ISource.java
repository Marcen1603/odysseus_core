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

import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.metadata.IHasMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;

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
public interface ISource<T extends IStreamObject<?>> extends IPhysicalOperator,
		ISubscribable<ISink<IStreamObject<?>>, AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>,
		IHasMetaAttribute, ITransfer<T> {
	/**
	 * Gets called initially once from every subscribed sink. Setup work should be
	 * done in here. caller: who called open sourcePort: on which output port of the
	 * source sinkPort: on which inport port of the sink callPath: Is needed to cope
	 * with cycles in the graph
	 *
	 * @throws OpenFailedException
	 *             if the source can't be initialised e.g. because some needed
	 *             resources like socket connections can't be allocated.
	 */
	void open(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners)
			throws OpenFailedException;

	void start(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners)
			throws StartFailedException;

	/**
	 * Close down the connection/do not read any more data
	 */
	void close(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners);

	/**
	 * Methods to mark (!) Operator as blocked. Can be used by scheduler
	 */
	void unblock();

	void block();

	boolean isBlocked();

	/**
	 * Suspending a source, when called from all owners the operator will be
	 * suspended
	 * 
	 * @param owner
	 */

	void suspend(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners);

	/**
	 * Resuming a source for this owner
	 * 
	 * @param owner
	 */
	void resume(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners);

}
