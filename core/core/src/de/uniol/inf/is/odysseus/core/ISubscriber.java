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
package de.uniol.inf.is.odysseus.core;

/**
 * @author Marco Grawunder
 */
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public interface ISubscriber<T, S extends ISubscription<T>> {
	
	/**
	 * SubscribeTo will be called at a sink operator. The first
	 * parameter of this method call will be the source.
	 * 
	 * So, if B is the following operator of A, then
	 * B.subscribeTo(A) will be called.
	 * 
	 * A -> B
	 *
	 */
	public void subscribeToSource(T source, int sinkInPort, int sourceOutPort, SDFSchema schema);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribeFromSource(S subscription);
	
	/**
	 * Unsubscribes from alle Subscribed Sources
	 */
	public void unsubscribeFromAllSources();
	
	/**
	 * Removes a subscription installed by {@link ISubscriber#subscribeTo(Object, int, int)}.
	 */
	public void unsubscribeFromSource(T source, int sinkInPort, int sourceOutPort, SDFSchema schema);
	public Collection<S> getSubscribedToSource();
	public S getSubscribedToSource(int i);
}
