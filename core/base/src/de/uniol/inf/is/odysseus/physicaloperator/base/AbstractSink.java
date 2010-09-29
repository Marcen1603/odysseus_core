package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.EventHandler;
import de.uniol.inf.is.odysseus.base.IEvent;
import de.uniol.inf.is.odysseus.base.IEventHandler;
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
	// Marker fuer Rekursion
	final private List<ISink<T>> openCalled = new CopyOnWriteArrayList<ISink<T>>();

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

	final private POEvent openInitEvent;
	final private POEvent openDoneEvent;

	final private AtomicBoolean isOpen = new AtomicBoolean(false);

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	// ------------------------------------------------------------------

	public AbstractSink() {
		openInitEvent = new POEvent(getInstance(), POEventType.OpenInit);
		openDoneEvent = new POEvent(getInstance(), POEventType.OpenDone);
	}

	public AbstractSink(AbstractSink<T> other) {
		openInitEvent = new POEvent(getInstance(), POEventType.OpenInit);
		openDoneEvent = new POEvent(getInstance(), POEventType.OpenDone);
		init(other);
	}

	protected void init(AbstractSink<T> other) {
		noInputPorts = other.noInputPorts;
		name = other.name;
		outputSchema = new SDFAttributeList(other.outputSchema);
		owners = new Vector<IOperatorOwner>(other.owners);
		allInputsDone = false;
	}

	// "delegatable this", used for the delegate sink
	protected ISink<T> getInstance() {
		return this;
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

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------
	@Override
	public void open() throws OpenFailedException {
		open(new ArrayList<PhysicalSubscription<ISink<?>>>());
	}

	protected void open(List<PhysicalSubscription<ISink<?>>> callPath) throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			this.isOpen.set(true);
		}
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			// Check if callPath contains this call to avoid cycles
			if (!containsSubscription(callPath, getInstance(),sub.getSourceOutPort(),sub.getSinkInPort())){
				callPath.add(new PhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(), sub.getSourceOutPort(), null));
				sub.getTarget().open(getInstance(), sub.getSourceOutPort(),	sub.getSinkInPort(), callPath);
			}
		}		
	}
	
	private boolean containsSubscription(List<PhysicalSubscription<ISink<?>>> callPath,
			ISink<? super T> sink, int sourcePort, int sinkPort){
		for (PhysicalSubscription<ISink<?>> sub: callPath){
			if (sub.getTarget() == sink && sub.getSinkInPort() == sinkPort && sub.getSourceOutPort() == sourcePort){
				return true;
			}
		}
		
		return false;
	}
	
	protected void process_open() throws OpenFailedException {
		// Empty Default Implementation
	}

	final public boolean isOpen() {
		return this.isOpen.get();
	}

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------

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

	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	public void close() {
		this.isOpen.set(false);
		process_close();
		stopMonitoring();
		callCloseOnChildren();
	}

	protected void callCloseOnChildren() {
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			sub.getTarget().close(getInstance(), sub.getSourceOutPort(),
					sub.getSinkInPort());
		}
	}

	protected void process_close() {
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

	// ------------------------------------------------------------------------
	// Getter and Setter
	// ------------------------------------------------------------------------

	@Override
	public String getName() {
		if (name == null) {
			return getInstance().getClass().getSimpleName() + "("
					+ getInstance().hashCode() + ")";
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

	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	@Override
	public void addOwner(IOperatorOwner owner) {
		this.owners.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
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

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

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

	// ------------------------------------------------------------------------
	// Other Methods
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return getInstance().getClass().getSimpleName() + "("
				+ getInstance().hashCode() + ")";
	}
	
	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	final public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	abstract public AbstractSink<T> clone();

}
