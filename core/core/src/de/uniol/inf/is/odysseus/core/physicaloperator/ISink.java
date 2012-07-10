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

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;

/**
 * Interface for data sinks in a query graph.
 * 
 * New data get pushed into a sink from the {@link ISource sources} it is
 * subscribed to by calling {@link #process(Object, int)}. If a source
 * has no more data (and therefore wont call
 * {@link #process(Object, int)} again) {@link #done(int)} gets called.
 * Setup and cleanup work is done in {@link #open()} and {@link #close()}
 * respectively.
 * 
 * @author Jonas Jacobi
 */
public interface ISink<T> extends IPhysicalOperator, ISubscriber<ISource<? extends T>, PhysicalSubscription<ISource<? extends T>>> {
	
	/**
	 * Process an element.
	 * 
	 * @param object
	 *            the element to process
	 * @param port
	 *            the input port, the element came from
	 */
	public void process(T object, int port);

	/**
	 * Same as above, but for processing of a batch of elements.
	 */
	public void process(Collection<? extends T> object, int port);

	/**
	 * Indicates that a source wont call process again.
	 * 
	 * @param port
	 *            the input port the source is connected to.
	 */
	public void done(int port);

	public void processPunctuation(PointInTime timestamp, int port);
	
	/**
	 * Call open on a Sink. 	
	 *  
	*/
	public void open() throws OpenFailedException;

	/**
	 * Close called on a sink has no parameter 
	 */
	public void close();

}
