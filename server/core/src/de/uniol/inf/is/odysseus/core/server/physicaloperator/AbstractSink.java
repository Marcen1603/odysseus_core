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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POPortEvent;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractSink<R extends IStreamObject<?>> extends AbstractMonitoringDataProvider
		implements ISink<R> {

	private final List<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> subscribedToSource = new CopyOnWriteArrayList<>();
	
	protected int noInputPorts = -1;
	private ReentrantLock openCloseLock = new ReentrantLock();

	private String name;
	private Map<String, String> infos = new TreeMap<>();
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<>();

	private Map<IOperatorOwner, Resource> uniqueIds = new TreeMap<>();

	private ILogicalOperator definingOp;

	private final OwnerHandler ownerHandler;
	private final Map<IOperatorOwner, Integer> openFor = new HashMap<>();

	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSink.class);

	protected boolean debug = false;

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	// ------------------------------------------------------------------
	// Eventhandling
	// ------------------------------------------------------------------

	private EventHandler eventHandler = null;

	private void initEventHandler() {
		if (eventHandler == null) {
			eventHandler = EventHandler.getInstance(this);
		}
	}

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		initEventHandler();
		eventHandler.subscribe(this, listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(this, listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		initEventHandler();
		eventHandler.subscribeToAll(this, listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(this, listener);
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		if (eventHandler != null) {
			eventHandler.fire(this, event);
		}
	}

	private final POEvent openInitEvent;
	private final POEvent openDoneEvent;

	protected final AtomicBoolean sinkOpen = new AtomicBoolean(false);
	protected final AtomicBoolean sinkStarted = new AtomicBoolean(false);

	protected POEvent[] processInitEvent = null;
	protected POEvent[] processDoneEvent = null;

	// ------------------------------------------------------------------

	public AbstractSink() {
		openInitEvent = new POEvent(getInstance(), POEventType.OpenInit);
		openDoneEvent = new POEvent(getInstance(), POEventType.OpenDone);
		ownerHandler = new OwnerHandler();
	}

	public AbstractSink(AbstractSink<R> other) {
		openInitEvent = new POEvent(getInstance(), POEventType.OpenInit);
		openDoneEvent = new POEvent(getInstance(), POEventType.OpenDone);
		ownerHandler = new OwnerHandler(other.ownerHandler);
		init(other);
	}

	protected void init(AbstractSink<R> other) {
		noInputPorts = other.noInputPorts;
		name = other.name;
		this.infos = new TreeMap<>(other.infos);
		this.outputSchema = createCleanClone(other.outputSchema);
	}

	// "delegatable this", used for the delegate sink
	protected ISink<R> getInstance() {
		return this;
	}

	@Override
	public final boolean isSink() {
		return true;
	}

	@Override
	public final boolean isSource() {
		return false;
	}

	@Override
	public final boolean isPipe() {
		return false;
	}

	protected void setInputPortCount(int ports) {
		if (ports > noInputPorts) {
			this.noInputPorts = ports;
			processInitEvent = new POEvent[noInputPorts];
			processDoneEvent = new POEvent[noInputPorts];
			for (int i = 0; i < noInputPorts; i++) {
				processInitEvent[i] = new POPortEvent(getInstance(), POEventType.ProcessInit, i);
				processDoneEvent[i] = new POPortEvent(getInstance(), POEventType.ProcessDone, i);
			}
		}
	}

	@Override
	public final int getInputPortCount() {
		return this.noInputPorts;
	}

	public SDFSchema getInputSchema(int port) {
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub = getSubscribedToSource(port);
		if (sub != null) {
			return sub.getSchema();
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public void open(IOperatorOwner owner) throws OpenFailedException {
		List<IOperatorOwner> forOwners = null;
		if (owner != null) {
			forOwners = new ArrayList<>();
			forOwners.add(owner);
		} else {
			forOwners = getOwner();
		}
		open(new ArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>(), forOwners);
	}

	@SuppressWarnings("unchecked")
	protected void open(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) throws OpenFailedException {

		openCloseLock.lock();
		try {
			for (IOperatorOwner o : forOwners) {
				Integer count = openFor.get(o);
				if (count == null) {
					openFor.put(o, 1);
				} else {
					openFor.put(o, count + 1);
				}
			}
			// The operator can already be initialized from former calls
			if (!isOpen()) {
				fire(openInitEvent);
				process_open();
				fire(openDoneEvent);
			}
			// In every case, the sink is now open (no need to check, its
			// cheaper to
			// always set this value to true
			// Hint: The operator can be opened by another method (c.f.
			// AbstractPipe)
			this.sinkOpen.set(true);

			// Call open on all registered sources0
			for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					sub.setDone(false);
					callPath.add(
							new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
									sub.getSource(), (ISink<IStreamObject<?>>) getInstance(), sub.getSinkInPort(),
									sub.getSourceOutPort(), null));
					if (sub.getSource().isOwnedByAny(forOwners)) {
						sub.getSource().open(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} finally {
			openCloseLock.unlock();
		}
	}

	private boolean containsSubscription(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			ISink<? super R> sink, int sourcePort, int sinkPort) {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : callPath) {
			if (sub.getSink() == sink && sub.getSinkInPort() == sinkPort && sub.getSourceOutPort() == sourcePort) {
				return true;
			}
		}

		return false;
	}

	protected void process_open() throws OpenFailedException {
		// Empty Default Implementation
	}

	@Override
	public boolean isOpen() {
		return this.sinkOpen.get();
	}

	@Override
	public boolean isStarted() {
		return this.sinkStarted.get();
	}

	@Override
	public boolean isOpenFor(IOperatorOwner owner) {
		return openFor.containsKey(owner);
	}

	// ------------------------------------------------------------------------
	// START
	// ------------------------------------------------------------------------

	@Override
	public void start(IOperatorOwner owner) throws StartFailedException {

		List<IOperatorOwner> forOwners = null;
		if (owner != null) {
			forOwners = new ArrayList<>();
			forOwners.add(owner);
		} else {
			forOwners = getOwner();
		}
		start(new ArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>(), forOwners);
	}

	@SuppressWarnings("unchecked")
	protected void start(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) throws OpenFailedException {

		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(MessageFormat.format("Calling start on {0}for {1}", this, forOwners));
		}
		if (!isStarted()) {
			process_start();
		}
		// In every case, the sink is now open (no need to check, its
		// cheaper to
		// always set this value to true
		// Hint: The operator can be opened by another method (c.f.
		// AbstractPipe)
		this.sinkStarted.set(true);

		// Call open on all registered sources0
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			// Check if callPath contains this call already to avoid cycles
			if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
						sub.getSource(), (ISink<IStreamObject<?>>) getInstance(), sub.getSinkInPort(),
						sub.getSourceOutPort(), null));
				if (sub.getSource().isOwnedByAny(forOwners)) {
					sub.getSource().start(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
							forOwners);
				}
			}
		}
	}

	protected void process_start() {

	}

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------

	@Override
	public final void process(R object, int port) {
		
		//fire(processInitEvent[port]);

		process_next(object, port);
		//fire(processDoneEvent[port]);
	}

	protected abstract void process_next(R object, int port);

	@Override
	public void setSuppressPunctuations(boolean suppressPunctuation) {
	}

	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	@Override
	public void close(IOperatorOwner id) {
		List<IOperatorOwner> owner;
		if (id != null) {
			owner = new ArrayList<>();
			owner.add(id);
		} else {
			owner = getOwner();
		}
		close(new ArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>(), owner);
	}

	public void close(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(MessageFormat.format("Closing for {0}", forOwners));
		}
		internal_close(callPath, forOwners, true);
	}

	protected void internal_close(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners, boolean doProcessClose) {

		openCloseLock.lock();
		try {
			for (IOperatorOwner o : forOwners) {
				Integer count = openFor.get(o);
				if (count == null) {
					throw new IllegalArgumentException("Call from not opened sink");
				} else {
					if (count == 1) {
						openFor.remove(o);
					} else {
						openFor.put(o, count - 1);
					}
				}
			}
			if (this.sinkOpen.get()) {
				try {
					callCloseOnChildren(callPath, forOwners);
					if (openFor.size() == 0) {
						if (doProcessClose) {
							process_close();
						}
						stopMonitoring();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					if (openFor.size() == 0) {
						this.sinkOpen.set(false);
					}
				}
			}
		} finally {
			openCloseLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	protected void callCloseOnChildren(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) {
		openCloseLock.lock();
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
						sub.getSource(), (ISink<IStreamObject<?>>) getInstance(), sub.getSinkInPort(),
						sub.getSourceOutPort(), null));
				try {
					if (sub.getSource().isOwnedByAny(forOwners)) {
						// Call close only on sources from the list of owners
						ISink<R> instance = getInstance();
						int outport = sub.getSourceOutPort();
						int inPort = sub.getSinkInPort();
						LOGGER.trace("Close for " + getName() + " on Child: " + sub.getSource() + " in Thread "
								+ Thread.currentThread().getName());
						sub.getSource().close(instance, outport, inPort, callPath, forOwners);
					}
				} catch (Throwable e) {
					LOGGER.error("Error calling close",e);
				}
			}
		}
		openCloseLock.unlock();
	}

	protected void process_close() {
		// Empty Default Implementation
	}

	protected void process_done(int port) {
		// Empty default Implementation
	}

	@Override
	public final synchronized void done(int port) {
		process_done(port);

		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() == port) {
				sub.setDone(true);
			}
		}

		if (isDone()) {
			try {
				for (IOperatorOwner owner : getOwner()) {
					owner.done(this);
				}
			} catch (NoSuchElementException e) {
				// TODO Timing Problem? MBr
				LOGGER.error("Error while calling done of owner!", e);
			}
		}
	}

	@Override
	public final synchronized boolean isDone() {
		boolean done = true;
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			if (!sub.isDone()) {
				done = false;
				break;
			}
		}
		return done;

	}

	// ------------------------------------------------------------------------
	// Suspend and Resume
	// ------------------------------------------------------------------------

	@Override
	public void suspend(IOperatorOwner id) {
		List<IOperatorOwner> forOwners = null;
		if (id != null) {
			forOwners = new ArrayList<>();
			forOwners.add(id);
		} else {
			forOwners = getOwner();
		}
		suspend(new ArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>(), forOwners);
	}

	@SuppressWarnings("unchecked")
	protected final void suspend(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) {

		openCloseLock.lock();
		try {

			// Call open on all registered sources0
			for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					callPath.add(
							new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
									sub.getSource(), (ISink<IStreamObject<?>>) getInstance(), sub.getSinkInPort(),
									sub.getSourceOutPort(), null));
					if (sub.getSource().isOwnedByAny(forOwners)) {
						sub.getSource().suspend(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} finally {
			openCloseLock.unlock();
		}
	}

	@Override
	public void resume(IOperatorOwner id) {
		List<IOperatorOwner> forOwners = null;
		if (id != null) {
			forOwners = new ArrayList<>();
			forOwners.add(id);
		} else {
			forOwners = getOwner();
		}
		resume(new ArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>(), forOwners);
	}

	@SuppressWarnings("unchecked")
	final void resume(List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath,
			List<IOperatorOwner> forOwners) {

		openCloseLock.lock();
		try {

			// Call resume on all registered sources
			for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					callPath.add(
							new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
									sub.getSource(), (ISink<IStreamObject<?>>) getInstance(), sub.getSinkInPort(),
									sub.getSourceOutPort(), null));
					if (sub.getSource().isOwnedByAny(forOwners)) {
						sub.getSource().resume(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} finally {
			openCloseLock.unlock();
		}
	}

	// ------------------------------------------------------------------------
	// Partial
	// ------------------------------------------------------------------------
	@Override
	public void partial(IOperatorOwner id, int sheddingFactor) {
		partial(id, sheddingFactor, this);
	}

	public void partial(IOperatorOwner id, int sheddingFactor, Object tgt) {
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			ISource<IStreamObject<?>> target = sub.getSource();

			for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subFromTarget : target.getSubscriptions()) {
				if (subFromTarget.getSink() == tgt && subFromTarget instanceof ControllablePhysicalSubscription) {
					((ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>) subFromTarget)
							.setSheddingFactor(sheddingFactor);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Getter and Setter
	// ------------------------------------------------------------------------

	@Override
	public String getName() {
		if (name == null) {
			return getInstance().getClass().getSimpleName() + "(" + getInstance().hashCode() + ")";
		}
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Map<String, String> getParameterInfos() {
		return infos;
	}

	@Override
	public void addParameterInfo(String key, Object value) {
		this.infos.put(key, value.toString());
	}

	@Override
	public void setParameterInfos(Map<String, String> infos) {
		this.infos = infos;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public SDFSchema getOutputSchema(int port) {
		SDFSchema schema = outputSchema.get(port);
		if (schema == null) {
			schema = getSubscribedToSource(port).getSchema();
		}
		return schema;
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		setOutputSchema(outputSchema, 0);
	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema, int port) {
		this.outputSchema.put(port, outputSchema);
	}

	@Override
	public final Map<Integer, SDFSchema> getOutputSchemas() {
		return Collections.unmodifiableMap(this.outputSchema);
	}

	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	@Override
	public void addOwner(IOperatorOwner owner) {
		ownerHandler.addOwner(owner);
	}

	@Override
	public void addOwner(Collection<IOperatorOwner> owner) {
		ownerHandler.addOwner(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		ownerHandler.removeOwner(owner);
	}

	@Override
	public void removeAllOwners() {
		ownerHandler.removeAllOwners();
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return ownerHandler.isOwnedBy(owner);
	}

	@Override
	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAny(owners);
	}

	@Override
	public boolean isOwnedByAll(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAll(owners);
	}

	@Override
	public boolean hasOwner() {
		return ownerHandler.hasOwner();
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return ownerHandler.getOwner();
	}

	@Override
	public String getOwnerIDs() {
		return ownerHandler.getOwnerIDs();
	}

	@Override
	public List<ISession> getSessions() {
		return ownerHandler.getSessions();
	}

	// ------------------------------------------------------------------------
	// Id Management
	// ------------------------------------------------------------------------

	@Override
	public void addUniqueId(IOperatorOwner owner, Resource id) {
		if (this.uniqueIds.containsKey(owner)) {
			throw new IllegalArgumentException("Id already set exception!");
		}
		this.uniqueIds.put(owner, id);
	}

	@Override
	public void removeUniqueId(IOperatorOwner key) {
		uniqueIds.remove(key);
	}

	@Override
	public Map<IOperatorOwner, Resource> getUniqueIds() {
		return uniqueIds;
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	@Override
	public void subscribeToSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {

		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}

		if (sinkInPort >= this.noInputPorts) {
			setInputPortCount(sinkInPort + 1);
		}

		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				source, (ISink<IStreamObject<?>>) getInstance(), sinkInPort, sourceOutPort, schema);
		subscribeToSource(sub);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void subscribeToSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub) {
		if (!this.subscribedToSource.contains(sub)) {
			if (!sinkInPortFree(sub.getSinkInPort())) {
				throw new IllegalArgumentException("SinkInPort " + sub.getSinkInPort() + " already bound ");
			}

			if (sub.getSinkInPort() >= this.noInputPorts) {
				setInputPortCount(sub.getSinkInPort() + 1);
			}
			
			this.subscribedToSource.add(sub);

			sub.getSource().subscribeSink((AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>) sub);
			newSourceSubscribed(sub);
		}
	}

	protected void newSourceSubscribed(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub) {
		// Implement this method if need to react to new source subscription
	}

	final protected int getNextFreeSinkInPort() {
		int sinkInPort = -1;
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() > sinkInPort) {
				sinkInPort = sub.getSinkInPort();
			}
		}
		sinkInPort++;
		return sinkInPort;
	}

	private boolean sinkInPortFree(int sinkInPort) {
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub : this.subscribedToSource) {
			if (sub != null && sub.getSinkInPort() == sinkInPort) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error(MessageFormat.format("SinkInPort {0} already bound to {1}", sinkInPort, sub));
				}
				return false;
			}
		}
		return true;
	}

	@Override
	final public List<AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>> getSubscribedToSource() {
		return Collections.unmodifiableList(this.subscribedToSource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribeFromSource(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(MessageFormat.format("Unsubscribe from Source {0}", subscription.getSource()));
		}
		if (subscribedToSource.remove(subscription)) {
			subscription.getSource().unsubscribeSink((ISink<IStreamObject<?>>) this.getInstance(),
					subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			sourceUnsubscribed(subscription);
		}
	}

	protected void sourceUnsubscribed(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription) {
		// Implement this method if need to react to source unsubscription
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribeFromSource(ISource<IStreamObject<?>> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		unsubscribeFromSource(new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				source, (ISink<IStreamObject<?>>) this, sinkInPort, sourceOutPort, schema));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribeFromAllSources() {
		while (!subscribedToSource.isEmpty()) {
			AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> subscription = subscribedToSource.remove(0);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace(MessageFormat.format("Unsubscribe from Source {0}", subscription.getSource()));
			}
			subscription.getSource().unsubscribeSink((ISink<IStreamObject<?>>) this.getInstance(),
					subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace(MessageFormat.format("Unsubscribe from Source {0} done.", subscription.getSource()));
			}
		}
	}

	@Override
	public final AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> getSubscribedToSource(int port) {
		for (AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?> sub: subscribedToSource) {
			if (sub.getSinkInPort() == port) {
				return sub;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// Other Methods
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return getInstance().getClass().getSimpleName() + "(" + getInstance().hashCode() + ")";
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo.isSink() || ipo.isPipe()))
			return false;
		return process_isSemanticallyEqual(ipo);
	}

	// TODO: Make abstract again and implement in Children
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	private static Map<Integer, SDFSchema> createCleanClone(Map<Integer, SDFSchema> old) {
		Map<Integer, SDFSchema> copy = new HashMap<>();
		for (Entry<Integer, SDFSchema> e : old.entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
	}

	@Override
	public boolean hasInput() {
		return !getSubscribedToSource().isEmpty();
	}

	// -----------------------
	// Provenance
	// -----------------------
	@Override
	public void setLogicalOperator(ILogicalOperator op) {
		this.definingOp = op;
	}

	@Override
	public ILogicalOperator getLogicalOperator() {
		return this.definingOp;
	}
}
