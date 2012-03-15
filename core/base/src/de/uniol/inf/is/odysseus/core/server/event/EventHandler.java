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
package de.uniol.inf.is.odysseus.core.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventHandler;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;

public class EventHandler implements IEventHandler {

	Logger logger = LoggerFactory.getLogger(EventHandler.class);

	final Map<IEventType, ArrayList<IEventListener>> eventListener = new HashMap<IEventType, ArrayList<IEventListener>>();
	final ArrayList<IEventListener> genericEventListener = new ArrayList<IEventListener>();
	EventDispatcher dispatcher;
	private Object caller;

	public EventHandler(Object caller) {
		this.caller = caller;
	}

	@Override
	public void startEventDispatcher() {
		if (isEventDispatcherRunning()) {
			stopEventDispatcher();
		}
		dispatcher = new EventDispatcher(this);
		dispatcher.start();
	}

	@Override
	public void stopEventDispatcher() {
		logger.debug("Trying to stop Event Dispatcher for " + caller);
		dispatcher.interrupt();
		dispatcher = null;
	}

	@Override
	public boolean isEventDispatcherRunning() {
		return dispatcher != null && dispatcher.isAlive();
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.core.server.IPOEventListener.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.core.server.monitor.queryexecution.event.POEventType)
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
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.core.server.IPOEventListener.queryexecution.event.POEventListener)
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
		try {
			long curTime = System.nanoTime();
			dispatcher.addEvent(event, curTime);
		} catch (NullPointerException e) {
			logger.warn("Event fired on closed handler from "+caller);
		}

	}

	Object getCaller() {
		return caller;
	}

}

class EventDispatcher extends Thread {

	static Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

	LinkedList<IEvent<?, ?>> eventQueue = new LinkedList<IEvent<?, ?>>();
	LinkedList<Long> eventTimestamps = new LinkedList<Long>();
	final EventHandler handler;
	private boolean interrupt;

	public EventDispatcher(EventHandler handler) {
		this.handler = handler;
		this.setName("Event Dispatcher " + handler + " for "
				+ handler.getCaller());
	}

	public void addEvent(IEvent<?, ?> event, long eventTime) {
		synchronized (eventQueue) {
			eventQueue.addLast(event);
			eventTimestamps.addLast(eventTime);
			eventQueue.notifyAll();
		}
	}

	@Override
	public void run() {
		IEvent<?, ?> eventToFire = null;
		Long timeStamp = null;
		while (!isInterrupted()) {
			synchronized (eventQueue) {
				while (!isInterrupted() && eventQueue.isEmpty()
						&& eventTimestamps.isEmpty()) {
					try {
						eventQueue.wait(1000);
					} catch (InterruptedException e) {
					}
				}
				if (isInterrupted()) {
					logger.debug("INTERRUPTED " + getName());
					continue;
				}
				eventToFire = eventQueue.removeFirst();
				timeStamp = eventTimestamps.removeFirst();
			}

			synchronized (handler.eventListener) {
				ArrayList<IEventListener> list = handler.eventListener
						.get(eventToFire.getEventType());
				if (list != null) {
					synchronized (list) {
						for (IEventListener listener : list) {
							listener.eventOccured(eventToFire, timeStamp);
						}
					}
				}

			}
			synchronized (handler.genericEventListener) {
				for (IEventListener listener : handler.genericEventListener) {
					listener.eventOccured(eventToFire, timeStamp);
				}
			}
		}

		logger.debug("Event Dispatcher terminated: " + getName());
	}

	@Override
	public void interrupt() {
		this.interrupt = true;
		super.interrupt();
	}

	@Override
    public boolean isInterrupted() {
		return super.isInterrupted() || interrupt;
	}

}
