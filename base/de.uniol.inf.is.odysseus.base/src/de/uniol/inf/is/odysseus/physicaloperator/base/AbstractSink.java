package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
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
	final protected Vector<Subscription<ISource<? extends T>>> subscribedTo = new Vector<Subscription<ISource<? extends T>>>();
	final protected Map<POEventType, ArrayList<POEventListener>> eventListener = new HashMap<POEventType, ArrayList<POEventListener>>();
	final protected ArrayList<POEventListener> genericEventListener = new ArrayList<POEventListener>();;

	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);

	final private AtomicBoolean isOpen = new AtomicBoolean(false);

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	protected int noInputPorts = -1;

	private String name;

	protected Vector<IOperatorOwner> owners = new Vector<IOperatorOwner>();

	private volatile boolean allInputsDone = false;

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

	protected void setInputPortCount(int ports) {
		if (ports > noInputPorts) {
			this.noInputPorts = ports;
			processInitEvent = new POEvent[noInputPorts];
			processDoneEvent = new POEvent[noInputPorts];
			for (int i = 0; i < noInputPorts; i++) {
				processInitEvent[i] = new POPortEvent(getInstance(),
						POEventType.ProcessInit, i);
				processDoneEvent[i] = new POPortEvent(getInstance(),
						POEventType.ProcessDone, i);
			}
		}
	}

	public int getInputPortCount() {
		return this.noInputPorts;
	}

	public void close() {
		this.isOpen.set(false);
	};

	@Override
	final public void open() throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			this.isOpen.set(true);
			//FIXME subscribedTo richtig locken
			for (Subscription<ISource<? extends T>> sub : this.subscribedTo) {
				sub.target.open();
			}
		}
	}

	final public boolean isOpen() {
		return this.isOpen.get();
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
			this.allInputsDone = true;
			for (Subscription<ISource<? extends T>> sub : this.subscribedTo) {
				if (sub.port == port) {
					sub.done = true;
				}
				if (!sub.done) {
					this.allInputsDone = false;
				}
			}
		}
	}

	final public boolean isDone() {
		synchronized (this.subscribedTo) {
			return this.allInputsDone;
		}
	}

	@Override
	public void subscribeTo(ISource<? extends T> source, int port) {
		if (port >= this.noInputPorts) {
			setInputPortCount(port + 1);
		}
		Subscription<ISource<? extends T>> sub = new Subscription<ISource<? extends T>>(
				source, port);
		synchronized (this.subscribedTo) {
			if (!this.subscribedTo.contains(sub)) {
				this.subscribedTo.add(sub);
				source.subscribe(getInstance(), port);
			}
		}
	}

	//"polymorphic this", used for the delegate sink
	protected ISink<T> getInstance() {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	final public List<Subscription<ISource<? extends T>>> getSubscribedTo() {
		return (List<Subscription<ISource<? extends T>>>) this.subscribedTo.clone();
	}

	final public Subscription<ISource<? extends T>> getSubscribedTo(int port) {
		return this.subscribedTo.get(port);
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener,
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
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener)
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
		this.owners.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		this.owners.remove(owner);
	}

	@Override
	final public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owners.contains(owner);
	}

	@Override
	final public boolean hasOwner() {
		return !this.owners.isEmpty();
	}

	@Override
	final public List<IOperatorOwner> getOwner() {
		return Collections.unmodifiableList(this.owners);
	}
}
