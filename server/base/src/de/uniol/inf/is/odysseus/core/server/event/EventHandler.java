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
package de.uniol.inf.is.odysseus.core.server.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;

/**
 * This class is a delegate and can handle event dispatching for
 * any source that registers IEventListener. A thread pool is used
 * to allow asynchronous dispatching
 * 
 * @author Marco Grawunder
 *
 */
public class EventHandler {

	Logger logger = LoggerFactory.getLogger(EventHandler.class);

	final Map<Object, Map<IEventType, ArrayList<IEventListener>>> eventListener = new HashMap<Object, Map<IEventType, ArrayList<IEventListener>>>();
	final Map<Object, List<IEventListener>> genericEventListener = new HashMap<Object, List<IEventListener>>();
	EventDispatcher[] dispatcherPool = null;
	final private Map<Object, EventDispatcher> dispatcherMap = new HashMap<Object, EventDispatcher>();
	static private EventHandler handler;

	/**
	 * Get an instance of the EventHandler and register caller as one distributor
	 * @param caller
	 * @return
	 */
	public synchronized static EventHandler getInstance(Object caller) {

		if (handler == null) {
			handler = new EventHandler();
		}

		Map<IEventType, ArrayList<IEventListener>> el = handler.eventListener
				.get(caller);
		if (el == null) {
			el = new HashMap<IEventType, ArrayList<IEventListener>>();
			handler.eventListener.put(caller, el);
		}

		// Round robin assignment of Dispatcher Threads, if not already assigned
		if (!handler.dispatcherMap.containsKey(caller)) {
			EventDispatcher dispatcher = handler.dispatcherPool[handler.dispatcherMap
					.size() % handler.dispatcherPool.length];
			handler.dispatcherMap.put(caller, dispatcher);
			if (!dispatcher.isAlive()) {
				dispatcher.start();
			}
		}

		return handler;
	}

	/**
	 * Stops ALL dispatching. This should only be called before the hole instance closes 
	 * because all threads are stopped.
	 */
	public static synchronized void stopDispatching() {
		for (EventDispatcher dispatcher : handler.dispatcherPool) {
			dispatcher.interrupt();
		}
		handler = null;
	}
	
	private EventHandler() {
		initThreadPool((int) Math.max(1, OdysseusConfiguration.getLong("EventHandlerDispatcherPoolSize", 10)));
	}

	private void initThreadPool(int size) {
		dispatcherPool = new EventDispatcher[size];
		for (int i = 0; i < size; i++) {
			dispatcherPool[i] = new EventDispatcher(this);
		}
	}

	/**
	 * Register a new listener at caller for event type
	 * @param caller
	 * @param listener
	 * @param type
	 */
	public void subscribe(Object caller, IEventListener listener,
			IEventType type) {
		synchronized (this.eventListener) {
			Map<IEventType, ArrayList<IEventListener>> el = eventListener
					.get(caller);
			ArrayList<IEventListener> curEventListener = el.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<IEventListener>();
				el.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
	}
	
	/**
	 * Remove a listener from caller for event type
	 * @param caller
	 * @param listener
	 * @param type
	 */
	public void unsubscribe(Object caller, IEventListener listener,
			IEventType type) {
		synchronized (this.eventListener) {
			Map<IEventType, ArrayList<IEventListener>> el = eventListener
					.get(caller);
			ArrayList<IEventListener> curEventListener = el.get(type);
			curEventListener.remove(listener);
		}
	}


	/**
	 * Register a listener for all events a caller provides
	 * @param caller
	 * @param listener
	 */
	public void subscribeToAll(Object caller, IEventListener listener) {

		synchronized (this.genericEventListener) {
			List<IEventListener> list = genericEventListener.get(caller);
			if (list == null) {
				list = new ArrayList<IEventListener>();
				this.genericEventListener.put(caller, list);
			}
			list.add(listener);
		}
	}

	/**
	 * Remove a listener from all caller if it was registered for all events. Does not removed any single
	 * event registration! 
	 * @param caller
	 * @param listener
	 */
	public void unSubscribeFromAll(Object caller, IEventListener listener) {
		synchronized (this.genericEventListener) {
			List<IEventListener> list = genericEventListener.get(caller);
			if (list != null) {
				list.remove(caller);
			}
		}
	}

	/**
	 * Deliver event from caller
	 * @param caller
	 * @param event
	 */
	final public void fire(Object caller, IEvent<?, ?> event) {
		long curTime = System.nanoTime();
		EventDispatcher eventDispatcher = dispatcherMap.get(caller);
		if( eventDispatcher != null ) {
			eventDispatcher.addEvent(caller, event, curTime);
		}
	}

}

/**
 * Dispatcher Class that does the dispatching of events. The Dispatcher has a
 * queue of events that should be dispatched to the listener. Each event has a
 * caller, a time stamp and a type. The EventHandler is asked to the listener
 * that should be informed
 * 
 * @author Marco Grawunder
 *
 */
class EventDispatcher extends Thread {

	static Logger logger = LoggerFactory.getLogger(EventDispatcher.class);

	LinkedList<IEvent<?, ?>> eventQueue = new LinkedList<IEvent<?, ?>>();
	LinkedList<Long> eventTimestamps = new LinkedList<Long>();
	LinkedList<Object> callerList = new LinkedList<Object>();
	final EventHandler handler;
	private boolean interrupt;
	static private int count = 0;

	public EventDispatcher(EventHandler handler) {
		this.handler = handler;
		this.setName("Event Dispatcher " + (count++));
	}

	public void addEvent(Object caller, IEvent<?, ?> event, long eventTime) {
		synchronized (eventQueue) {
			eventQueue.addLast(event);
			eventTimestamps.addLast(eventTime);
			callerList.addLast(caller);
			eventQueue.notifyAll();
		}
	}

	@Override
	public void run() {
		IEvent<?, ?> eventToFire = null;
		Long timeStamp = null;
		Object caller = null;
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
				caller = callerList.removeFirst();
			}

			synchronized (handler.eventListener) {
				Map<IEventType, ArrayList<IEventListener>> el = handler.eventListener
						.get(caller);
				if (el != null) {
					ArrayList<IEventListener> list = el.get(eventToFire
							.getEventType());
					if (list != null) {
						synchronized (list) {
							for (IEventListener listener : list) {
								listener.eventOccured(eventToFire, timeStamp);
							}
						}
					}
				}
			}
			synchronized (handler.genericEventListener) {
				if (handler.genericEventListener.get(caller) != null) {
					for (IEventListener listener : handler.genericEventListener
							.get(caller)) {
						listener.eventOccured(eventToFire, timeStamp);
					}
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
