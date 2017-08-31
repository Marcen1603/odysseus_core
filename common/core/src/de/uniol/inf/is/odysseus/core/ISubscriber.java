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


public interface ISubscriber<I, S extends ISubscription<I,?>> {
	
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
	void subscribeToSource(I source, int sinkInPort, int sourceOutPort, SDFSchema schema);
	void subscribeToSource(S Subscription);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	void unsubscribeFromSource(S subscription);
	
	/**
	 * Unsubscribes from alle Subscribed Sources
	 */
	void unsubscribeFromAllSources();
	
	/**
	 * Removes a subscription installed by {@link ISubscriber#subscribeTo(Object, int, int)}.
	 */
	void unsubscribeFromSource(I source, int sinkInPort, int sourceOutPort, SDFSchema schema);
	Collection<S> getSubscribedToSource();
	S getSubscribedToSource(int i);
}
