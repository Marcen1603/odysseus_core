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

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;

/**
 * Interface for data sinks in a query graph.
 * 
 * New data get pushed into a sink from the {@link ISource sources} it is
 * subscribed to by calling {@link #process(Object, int)}. If a source has no
 * more data (and therefore wont call {@link #process(Object, int)} again)
 * {@link #done(int)} gets called. Setup and cleanup work is done in
 * {@link #open()} and {@link #close()} respectively.
 * 
 * @author Jonas Jacobi, Marco Grawunder
 */
public interface ISink<T extends IStreamObject<?>>
		extends IPhysicalOperator, ISubscriber<ISource<IStreamObject<?>>, AbstractPhysicalSubscription<ISource<IStreamObject<?>>,?>> {

	/**
	 * Process an element.
	 * 
	 * @param object
	 *            the element to process
	 * @param port
	 *            the input port, the element came from
	 */
	void process(T object, int port);

	/**
	 * Indicates that a source wont call process again.
	 * 
	 * @param port
	 *            the input port the source is connected to.
	 */
	void done(int port);

	/**
	 * Process a punctuation
	 * 
	 * @param punctuation
	 * @param port
	 */
	void processPunctuation(IPunctuation punctuation, int port);

	/**
	 * Shared Sink can be opened for different owners (queries)
	 * 
	 * @param owner
	 * @return
	 */
	boolean isOpenFor(IOperatorOwner owner);

	/**
	 * Call suspend for a specific owner
	 * 
	 * @param id
	 */
	void suspend(IOperatorOwner id);

	/**
	 * Call resume for a suspended owner
	 * 
	 * @param id
	 */
	void resume(IOperatorOwner id);

	void partial(IOperatorOwner id, int sheddingFactor);

	/**
	 * How many input ports are available
	 * 
	 * @return
	 */
	int getInputPortCount();

	/**
	 * Allow to set available input ports
	 * @param count
	 */
	void setInputPortCount(int count);
}
