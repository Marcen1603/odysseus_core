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
package de.uniol.inf.is.odysseus;

import java.util.Collection;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


public interface ISubscribable<T, S extends ISubscription<T>> {
	/**
	 * SubscribeSink will be called at a source operator. The first
	 * parameter of this method call will be the sink.
	 * 
	 * So, if B is the following operator of A, then
	 * A.subscribe(B) will be called.
	 * 
	 * A -> B
	 * 
	 * Subscription is initial inactive, call open to active, close to deaktivate
	 *
	 */
	public void subscribeSink(T sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	
	/**
	 * Removes a subscription installed by {@link ISubscribable#subscribe(Object, int, int)}
	 */
	public void unsubscribeSink(T sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	
	/**
	 * Removes a subscription installed by the methods
	 * {@link ISubscriber#subscribeTo(Object, int, int)} or
	 * {@link ISubscribable#subscribe(Object, int, int)}
	 * @param subscription
	 */
	public void unsubscribeSink(S subscription);
	public Collection<S> getSubscriptions();
	
	/**
	 * Same as subscribeSink but needs no open. Especially for terminal sinks, that connect at runtime
	 * to specific operators
	 * @param sink
	 * @param sinkInPort
	 * @param sourceOutPort
	 * @param schema
	 */
	public void connectSink(T sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	public void disconnectSink(T sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema);
	
}
