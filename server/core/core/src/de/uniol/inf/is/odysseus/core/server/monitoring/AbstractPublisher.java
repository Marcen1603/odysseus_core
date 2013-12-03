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
package de.uniol.inf.is.odysseus.core.server.monitoring;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.monitoring.IPublisher;
import de.uniol.inf.is.odysseus.core.monitoring.ISubscriber;

public abstract class AbstractPublisher<T> implements IPublisher<T> {

	protected List<ISubscriber<T>> subscribers = new ArrayList<ISubscriber<T>>();

	@Override
	public void subscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.add(subscriber);
		}
	}

	@Override
	public void unsubscribe(ISubscriber<T> subscriber) {
		synchronized (this.subscribers) {
			this.subscribers.remove(subscriber);
		}
	}

	@Override
	public void notifySubscribers(T value) {
		synchronized (subscribers) {
			for (ISubscriber<T> subscriber : this.subscribers) {
				subscriber.update(this, value);
			}	
		}
	}

	@Override
	public int subscribtionCount() {
		return this.subscribers.size();
	}
}
