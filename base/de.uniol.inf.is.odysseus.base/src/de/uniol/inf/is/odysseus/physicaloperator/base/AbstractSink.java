package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractSink<T> extends AbstractMonitoringDataProvider
		implements ISink<T> {

	private static Logger logger = null;

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(AbstractSink.class);
		}
		return logger;
	}

	final private List<PhysicalSubscription<ISource<? extends T>>> subscribedToSource = new CopyOnWriteArrayList<PhysicalSubscription<ISource<? extends T>>>();
	// Monitoring Data
	final protected Map<POEventType, ArrayList<IPOEventListener>> eventListener = new HashMap<POEventType, ArrayList<IPOEventListener>>();
	final protected ArrayList<IPOEventListener> genericEventListener = new ArrayList<IPOEventListener>();;

	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);

	final private AtomicBoolean isOpen = new AtomicBoolean(false);

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	protected int noInputPorts = -1;

	private String name;
	private SDFAttributeList outputSchema;

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
		process_close();
		stopMonitoring();
	};

	@Override
	final public void open() throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			this.isOpen.set(true);
			for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
				sub.getTarget().open();
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
		this.allInputsDone = true;
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() == port) {
				sub.setDone(true);
			}
			if (!sub.isDone()) {
				this.allInputsDone = false;
			}
		}
	}

	final public boolean isDone() {
		return this.allInputsDone;
	}

	@Override
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		if (sinkInPort >= this.noInputPorts) {
			setInputPortCount(sinkInPort + 1);
		}
		PhysicalSubscription<ISource<? extends T>> sub = new PhysicalSubscription<ISource<? extends T>>(
				source, sinkInPort, sourceOutPort, schema);
		if (!this.subscribedToSource.contains(sub)) {
			getLogger().debug(
					this.getInstance() + " Subscribe To Source " + source
							+ " to " + sinkInPort + " from " + sourceOutPort);
			this.subscribedToSource.add(sub);
			source.subscribeSink(getInstance(), sinkInPort, sourceOutPort,
					schema);
		}
	}

	// "delegatable this", used for the delegate sink
	protected ISink<T> getInstance() {
		return this;
	}

	@Override
	final public List<PhysicalSubscription<ISource<? extends T>>> getSubscribedToSource() {
		return Collections.unmodifiableList(this.subscribedToSource);
	}

	@Override
	public void unsubscribeFromSource(
			PhysicalSubscription<ISource<? extends T>> subscription) {
		getLogger()
				.debug("Unsubscribe from Source " + subscription.getTarget());
		if (this.subscribedToSource.remove(subscription)) {
			subscription.getTarget().unsubscribeSink(this.getInstance(),
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	@Override
	public void unsubscribeFromSource(ISource<? extends T> source,
			int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		unsubscribeFromSource(new PhysicalSubscription<ISource<? extends T>>(
				source, sinkInPort, sourceOutPort, schema));
	}
	
	@Override
	public void unsubscribeFromAllSources() {
		while (!subscribedToSource.isEmpty()) {
			PhysicalSubscription<ISource<? extends T>> subscription = subscribedToSource
					.remove(0);
			getLogger().debug(
					"Unsubscribe from Source " + subscription.getTarget());
			subscription.getTarget().unsubscribeSink(this.getInstance(),
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
			getLogger().debug(
					"Unsubscribe from Source " + subscription.getTarget()
							+ " done.");

		}
	}

	final public PhysicalSubscription<ISource<? extends T>> getSubscribedToSource(
			int port) {
		return this.subscribedToSource.get(port);
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@Override
	public void subscribe(IPOEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IPOEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<IPOEventListener>();
				this.eventListener.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
	}

	@Override
	public void unsubscribe(IPOEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IPOEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}



	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(IPOEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(IPOEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}

	final protected void fire(POEvent event) {
		synchronized (eventListener) {
			
		ArrayList<IPOEventListener> list = this.eventListener.get(event
				.getPOEventType());
		if (list != null) {
			synchronized (list) {
				for (IPOEventListener listener : list) {
					listener.poEventOccured(event);
				}
			}
		}
			for (IPOEventListener listener : this.genericEventListener) {
				listener.poEventOccured(event);
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
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
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		this.owners.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		// remove all occurrences
		while (this.owners.remove(owner)) {
		}
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

	@Override
	abstract public AbstractSink<T> clone();

}
