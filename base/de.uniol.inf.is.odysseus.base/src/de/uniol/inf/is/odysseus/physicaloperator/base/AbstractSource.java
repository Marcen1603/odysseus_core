package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSource<T> extends AbstractMonitoringDataProvider
		implements ISource<T> {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ISource.class);
	final protected ArrayList<Subscription<ISink<? super T>>> subscriptions = new ArrayList<Subscription<ISink<? super T>>>();;
	final protected Map<POEventType, ArrayList<POEventListener>> eventListener = new HashMap<POEventType, ArrayList<POEventListener>>();
	final protected ArrayList<POEventListener> genericEventListener = new ArrayList<POEventListener>();
	private boolean hasSingleConsumer;
	private AtomicBoolean open = new AtomicBoolean(false);
	private String name;

	// Events
	final private POEvent doneEvent = new POEvent(this, POEventType.Done);
	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);
	final private POEvent pushInitEvent = new POEvent(this,
			POEventType.PushInit);
	final private POEvent pushDoneEvent = new POEvent(this,
			POEventType.PushDone);

	protected List<IOperatorOwner> owners = new Vector<IOperatorOwner>();

	@Override
	public boolean isSink() {
		return false;
	}

	@Override
	public boolean isSource() {
		return true;
	}

	@Override
	public boolean isPipe() {
		return isSink() && isSource();
	}

	public boolean isOpen() {
		return open.get();
	}

	public void close() {
		open.set(false);
	};

	@Override
	public synchronized void open() throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			open.set(true);
		}
	}

	protected abstract void process_open() throws OpenFailedException;

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	};

	@Override
	final public void subscribe(ISink<? super T> sink, int port) {
		Subscription<ISink<? super T>> sub = new Subscription<ISink<? super T>>(
				sink, port);
		synchronized (this.subscriptions) {
			if (!this.subscriptions.contains(sub)) {
				this.subscriptions.add(sub);
				sink.subscribeTo(this, port);
				this.hasSingleConsumer = this.subscriptions.size() == 1;
			}
		}
	}

	@Override
	public void transfer(T object) {
		fire(this.pushInitEvent);
		process_transfer(object);
		// DEBUG
		// try{
		// if(((MVRelationalTuple)object).getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		// }catch(Exception e){
		// // falls es das attribut nicht gibt, tue nichts.
		// }
		fire(this.pushDoneEvent);
	}

	@Override
	public void transfer(Collection<T> object) {
		// TODO events erzeugen und verschicken, bzw. ein spezielles
		// transferbatchevent
		synchronized (this.subscriptions) {
			for (Subscription<ISink<? super T>> sink : this.subscriptions) {
				sink.target.process(object, sink.port, isTransferExclusive());
			}
		}
	}

	/**
	 * states if the next Operator can change the transfer object oder has to
	 * make a copy
	 * 
	 * @return
	 */
	protected boolean isTransferExclusive() {
		return hasSingleConsumer;
	}

	final protected void fire(POEvent event) {
		ArrayList<POEventListener> list = this.eventListener.get(event
				.getPOEventType());
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

	protected void process_transfer(T object) {
		synchronized (this.subscriptions) {
			for (Subscription<ISink<? super T>> sink : this.subscriptions) {
				sink.target.process(object, sink.port, !this.hasSingleConsumer);
			}
		}
	}

	@Override
	final public void unsubscribe(ISink<? super T> sink, int port) {
		synchronized (this.subscriptions) {
			if (this.subscriptions.remove(new Subscription<ISink<? super T>>(
					sink, port))) {
				sink.unsubscribeSubscriptionTo(this, port);
			}
			this.hasSingleConsumer = this.subscriptions.size() == 1;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	final public List<Subscription<ISink<? super T>>> getSubscribtions() {
		synchronized (this.subscriptions) {
			return (List<Subscription<ISink<? super T>>>) this.subscriptions
					.clone();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractSource<T> clone() {
		AbstractSource<T> obj = null;
		try {
			obj = (AbstractSource<T>) super.clone();
			for (Subscription<ISink<? super T>> sink : this.subscriptions) {
				obj.subscribe(sink.target, sink.port);
			}
		} catch (CloneNotSupportedException e) {
		}
		return obj;
	}

	final protected void propagateDone() {
		fire(this.doneEvent);
		this.process_done();
		synchronized (subscriptions) {
			for (Subscription<ISink<? super T>> sub : subscriptions) {
				sub.target.done(sub.port);
			}
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPOEventSender#subscribe(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@SuppressWarnings("unchecked")
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
		if (listener instanceof IMonitoringData) {
			IMonitoringData<?> mItem = (IMonitoringData<?>) listener;
			// nur dann registrieren, falls es noch nicht registriert ist
			// es kann z.B. sein, dass ein MetadataItem f�r zwei Events
			// registriert wird, aber nat�rlich trotzdem nur einmal
			// als MetadataItem registriert sein muss
			try {
				this.addMonitoringData(mItem.getType(), mItem);
			} catch (IllegalArgumentException e) {
			}
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

	final protected boolean hasSingleConsumer() {
		return this.hasSingleConsumer;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		synchronized (this.subscriptions) {
			for (Subscription<? extends ISink<?>> sub : this.subscriptions) {
				sub.target.processPunctuation(punctuation);
			}
		}
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
		// synchronized (this.deactivateRequestControls) {
		// this.deactivateRequestControls.remove(owner);
		// }
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owners.contains(owner);
	}

	@Override
	public boolean hasOwner() {
		return !this.owners.isEmpty();
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return Collections.unmodifiableList(this.owners);
	}

}
