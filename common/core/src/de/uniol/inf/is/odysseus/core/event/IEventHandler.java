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
package de.uniol.inf.is.odysseus.core.event;

/**
 * This interface provides all methods to handle eventlistener
 * 
 * @author Marco Grawunder
 *
 */
public interface IEventHandler {

	/**
	 * Add a new listener for a specific event type
	 * @param listener The listener that should be informed
	 * @param type The types of events the listener is interested in
	 */
	public void subscribe(IEventListener listener, IEventType type);

	/**
	 * Remove a listener for a specific event type
	 * @param listener The listener that should be removed
	 * @param type The type of event for which the listener should
	 *             be removed
	 */
	public void unsubscribe(IEventListener listener, IEventType type);

	/**
	 * Add a new listener for all events that could be fires
	 * @param listener The listener that should be informed
	 */
	public void subscribeToAll(IEventListener listener);

	/**
	 * Remove a listener that was previously subscribed for all events
	 * This does not remove a listener that was registered with a
	 * specific event type
	 * @param listener
	 */
	public void unSubscribeFromAll(IEventListener listener);

	/**
	 * Fire a specific event to all EventListner
	 * @param event The event that will be fired
	 */
	public void fire(IEvent<?, ?> event);

}
