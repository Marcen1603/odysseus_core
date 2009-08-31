package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSink<T> extends AbstractMonitoringDataProvider
		implements ISink<T> {
	final protected ArrayList<Subscription<ISource<? extends T>>> subscribedTo = new ArrayList<Subscription<ISource<? extends T>>>();;
	final protected Map<POEventType, ArrayList<POEventListener>> eventListener = new HashMap<POEventType, ArrayList<POEventListener>>();
	final protected ArrayList<POEventListener> genericEventListener = new ArrayList<POEventListener>();;

	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);

	private POEvent[] processInitEvent = null;
	private POEvent[] processDoneEvent = null;

	protected int noInputPorts = -1;
	
	private String name;

	protected ArrayList<IOperatorOwner> owner = new ArrayList<IOperatorOwner>();
	protected ArrayList<IOperatorControl> deactivateRequestControls = new ArrayList<IOperatorControl>();
	
	@Override
	public boolean isSink() {
		return true;
	}

	@Override
	public boolean isSource() {
		return false;
	}

	@Override
	public boolean isPipe() {
		return isSink() && isSource();
	}

	public void setNoOfInputPort(int ports) {
		if (ports > noInputPorts) {
			this.noInputPorts = ports;
			processInitEvent = new POEvent[ports];
			processDoneEvent = new POEvent[ports];
			for (int i = 0; i < ports; i++) {
				processInitEvent[i] = new POPortEvent(this,
						POEventType.ProcessInit, i);
				processDoneEvent[i] = new POPortEvent(this,
						POEventType.ProcessDone, i);
			}
		}
	}

	public void close() {
	};

	@Override
	final public void open() throws OpenFailedException {
		if (isActive()) {
			fire(openInitEvent);
			process_open();
			synchronized (this.subscribedTo) {
				for (Subscription<ISource<? extends T>> sub : this.subscribedTo) {
					sub.done = false;
					sub.target.open();
				}
			}
			fire(openDoneEvent);
		}
	}

	@Override
	final public void process(T object, int port, boolean isReadOnly) {
		fire(processInitEvent[port]);
		process_next(object, port, isReadOnly);
		fire(processDoneEvent[port]);
	}

	@Override
	public void process(Collection<? extends T> object, int port,
			boolean isReadOnly) {
		for (T cur : object) {
			process(cur, port, isReadOnly);
		}
	}

	protected abstract void process_next(T object, int port, boolean isReadOnly);

	protected void process_close() {
		// Empty Default Implementation
	}

	protected void process_open() throws OpenFailedException {
		// Empty Default Implementation
	}

	protected void process_done(int port) {
		// Empty default Implementation
	}

	@Override
	final public void done(int port) {
		process_done(port);
		synchronized (this.subscribedTo) {
			for (Subscription<ISource<? extends T>> sub : this.subscribedTo) {
				if (sub.port == port) {
					sub.done = true;
					return;
				}
			}
		}
	}

	@Override
	public void subscribeTo(ISource<? extends T> source, int port) {
		if (port >= this.noInputPorts) {
			setNoOfInputPort(port + 1);
		}
		Subscription<ISource<? extends T>> sub = new Subscription<ISource<? extends T>>(
				source, port);
		synchronized (this.subscribedTo) {
			if (!this.subscribedTo.contains(sub)) {
				this.subscribedTo.add(sub);
				source.subscribe(this, port);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	final public List<Subscription<ISource<? extends T>>> getSubscribedTo() {
		synchronized (this.subscribedTo) {
			return (List<Subscription<ISource<? extends T>>>) this.subscribedTo
					.clone();
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPOEventSender#subscribe(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@Override
	public void subscribe(POEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<POEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<POEventListener>();
				this.eventListener.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
	}

	@Override
	public void unsubscribe(POEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<POEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}

	@Override
	public void unsubscribeSubscriptionTo(ISource<? extends T> source, int port) {
		if (this.subscribedTo.remove(new Subscription<ISource<? extends T>>(
				source, port))) {
			source.unsubscribe(this, port);
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPOEventSender#subscribeToAll(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(POEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(POEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}

	final protected void fire(POEvent event) {
		ArrayList<POEventListener> list = this.eventListener.get(event);
		if (list != null) {
			synchronized (list) {
				for (POEventListener listener : list) {
					listener.poEventOccured(event);
				}
			}
		}
		synchronized (this.eventListener) {
			for (POEventListener listener : this.genericEventListener) {
				listener.poEventOccured(event);
			}
		}
	}

	// TODO remove methode fuer subscribtions
	@SuppressWarnings("unchecked")
	@Override
	public AbstractSink<T> clone() {
		synchronized (this.subscribedTo) {
			try {
				AbstractSink<T> sink = (AbstractSink<T>) super.clone();
				for (Subscription<ISource<? extends T>> sub : this.subscribedTo) {
					sub.target.subscribe(this, sub.port);
				}
				return sink;
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void processPunctuation(PointInTime timestamp) {
	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName() + "(" + this.hashCode()
					+ ")";
		}
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		synchronized (this.owner) {
			this.owner.add(owner);
		}
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		synchronized (this.owner) {
			this.owner.remove(owner);
		}
		synchronized (this.deactivateRequestControls) {
			this.deactivateRequestControls.remove(owner);
		}
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		synchronized (this.owner) {
			return this.owner.contains(owner);
		}
	}

	@Override
	public boolean hasOwner() {
		synchronized (this.owner) {
			return this.owner.size() > 0;
		}
	}

	@Override
	public ArrayList<IOperatorOwner> getOwner() {
		synchronized (this.owner) {
			return this.owner;
		}
	}

	@Override
	public void activateRequest(IOperatorControl operatorControl) {
		synchronized (this.deactivateRequestControls) {
			this.deactivateRequestControls.add(operatorControl);
		}
	}

	@Override
	public void deactivateRequest(IOperatorControl operatorControl) {
		synchronized (this.deactivateRequestControls) {
			this.deactivateRequestControls.remove(operatorControl);
		}
	}

	@Override
	public boolean deactivateRequestedBy(IOperatorControl operatorControl) {
		synchronized (this.deactivateRequestControls) {
			return this.deactivateRequestControls.contains(operatorControl);
		}
	}

	@Override
	public synchronized boolean isActive() {
		int own = this.owner.size();
		int deac = this.deactivateRequestControls.size();

		if (own < 1 || own <= deac) {
			return false;
		}
		return true;
	}
}
