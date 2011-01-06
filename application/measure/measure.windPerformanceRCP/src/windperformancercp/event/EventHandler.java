package windperformancercp.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventHandler implements IEventHandler{
	
	final private Map<IEventType, ArrayList<IEventListener>> eventListener = new HashMap<IEventType, ArrayList<IEventListener>>();
	final private ArrayList<IEventListener> genericEventListener = new ArrayList<IEventListener>();

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
	final public void fire(IEvent<?,?> event) {
		synchronized (eventListener) {

			ArrayList<IEventListener> list = this.eventListener.get(event
					.getEventType());
			if (list != null) {
				synchronized (list) {
					for (IEventListener listener : list) {
						listener.eventOccured(event);
					}
				}
			}
			for (IEventListener listener : this.genericEventListener) {
				listener.eventOccured(event);
			}
		}
	}
	
}
