package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IEvent;
import de.uniol.inf.is.odysseus.base.IEventHandler;
import de.uniol.inf.is.odysseus.base.EventHandler;
import de.uniol.inf.is.odysseus.base.IEventListener;
import de.uniol.inf.is.odysseus.base.IEventType;
import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POPortEvent;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractSink<T> extends AbstractMonitoringDataProvider
		implements ISink<T> {

	final private List<PhysicalSubscription<ISource<? extends T>>> subscribedToSource = new CopyOnWriteArrayList<PhysicalSubscription<ISource<? extends T>>>();
	protected int noInputPorts = -1;

	private String name;
	private SDFAttributeList outputSchema;

	protected Vector<IOperatorOwner> owners = new Vector<IOperatorOwner>();

	private volatile boolean allInputsDone = false;
	
	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------
	private static Logger logger = null;

	private static Logger getLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(AbstractSink.class);
		}
		return logger;
	}


	// ------------------------------------------------------------------
	// Eventhandling
	// ------------------------------------------------------------------
	
	private IEventHandler eventHandler = new EventHandler();
	

	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(listener, type);
	}

	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(listener, type);
	}

	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(listener);
	}

	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(listener);
	}

	public void fire(IEvent<?, ?> event) {
		eventHandler.fire(event);
	}

	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);

	final private AtomicBoolean isOpen = new AtomicBoolean(false);

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	// ------------------------------------------------------------------
	
	public AbstractSink() {
	}

	public AbstractSink(AbstractSink<T> other){
		noInputPorts = other.noInputPorts;
		name = other.name;
		outputSchema = new SDFAttributeList(other.outputSchema);
		owners = new Vector<IOperatorOwner>(other.owners);
		allInputsDone = false;
	}
	

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
		close(this);
	}

	protected void close(ISink<T> sink) {
		this.isOpen.set(false);
		process_close();
		stopMonitoring();
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			sub.getTarget().close(sink, sub.getSourceOutPort());
		}
	};

	protected void open(ISink<T> sink) throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			this.isOpen.set(true);
			for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
				sub.getTarget().open(sink, sub.getSourceOutPort());
			}
		}
	}
	
	@Override
	final public void open() throws OpenFailedException {
		open(this);		
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

	protected void process_open() throws OpenFailedException{
		// Empty Default Implementation
	}

	protected void process_done(int port) {
		// Empty default Implementation
	}

	@Override
	final synchronized public void done(int port) {
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

	final synchronized public boolean isDone() {
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
