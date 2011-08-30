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
package de.uniol.inf.is.odysseus.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventHandler implements IEventHandler {

	final Map<IEventType, ArrayList<IEventListener>> eventListener = new HashMap<IEventType, ArrayList<IEventListener>>();
	final ArrayList<IEventListener> genericEventListener = new ArrayList<IEventListener>();
	final EventDispatcher dispatcher = new EventDispatcher(this);
	
	public EventHandler() {
		dispatcher.start();
	}
	
	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<IEventListener>();
				this.eventListener.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(IEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}

	@Override
	final public void fire(IEvent<?, ?> event) {
		dispatcher.addEvent(event);
	}

}

class EventDispatcher extends Thread {
	List<IEvent<?, ?>> eventQueue = new CopyOnWriteArrayList<IEvent<?, ?>>();
	final EventHandler handler;

	public EventDispatcher(EventHandler handler) {
		this.handler = handler;
	}
	
	public void addEvent(IEvent<?, ?> event){
		synchronized (this) {
			eventQueue.add(event);
			this.notifyAll();
		}
	}
	
	@Override
	public void run() {
		while (!interrupted()) {
			synchronized (this) {
				while (eventQueue.isEmpty()){
					try {
						this.wait(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (IEvent<?, ?> eventToFire : eventQueue) {
					//System.err.println("Fire Event "+eventToFire);

					synchronized (handler.eventListener) {
						ArrayList<IEventListener> list = handler.eventListener
								.get(eventToFire.getEventType());
						if (list != null) {
							synchronized (list) {
								for (IEventListener listener : list) {
									listener.eventOccured(eventToFire);
								}
							}
						}

					}
					synchronized (handler.genericEventListener) {
						for (IEventListener listener : handler.genericEventListener) {
							listener.eventOccured(eventToFire);
						}
					}
				}
				eventQueue.clear();
			}
		}
	}

}
