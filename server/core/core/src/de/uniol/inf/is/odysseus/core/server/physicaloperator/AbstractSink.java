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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
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

	final private List<AbstractPhysicalSubscription<ISource<? extends R>>> subscribedToSource = new CopyOnWriteArrayList<AbstractPhysicalSubscription<ISource<? extends R>>>();

	protected int noInputPorts = -1;
	private ReentrantLock openCloseLock = new ReentrantLock();

	private String name;
	private Map<String, String> infos = new TreeMap<>();
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<Integer, SDFSchema>();

	private Map<IOperatorOwner, Resource> uniqueIds = new TreeMap<>();

	private ILogicalOperator definingOp;

	// private boolean allInputsDone = false;
	final private OwnerHandler ownerHandler;
	private final Map<IOperatorOwner, Integer> openFor = new HashMap<>();

	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------
	private static Logger logger = LoggerFactory.getLogger(AbstractSink.class);

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

	final private POEvent openInitEvent;
	final private POEvent openDoneEvent;

	final protected AtomicBoolean sinkOpen = new AtomicBoolean(false);
	final protected AtomicBoolean sinkStarted = new AtomicBoolean(false);

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
		// allInputsDone = false;
	}

	// "delegatable this", used for the delegate sink
	protected ISink<R> getInstance() {
		return this;
	}

	@Override
	final public boolean isSink() {
		return true;
	}

	@Override
	final public boolean isSource() {
		return false;
	}

	@Override
	final public boolean isPipe() {
		return isSink() && isSource();
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
	final public int getInputPortCount() {
		return this.noInputPorts;
	}

	public SDFSchema getInputSchema(int port) {
		AbstractPhysicalSubscription<ISource<? extends R>> sub = getSubscribedToSource(port);
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
		// allInputsDone = false;
		List<IOperatorOwner> forOwners = null;
		if (owner != null) {
			forOwners = new ArrayList<>();
			forOwners.add(owner);
		} else {
			forOwners = getOwner();
		}
		open(new ArrayList<AbstractPhysicalSubscription<ISink<?>>>(), forOwners);
	}

	protected void open(List<AbstractPhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners)
			throws OpenFailedException {

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
			// allInputsDone = false;
			// getLogger().trace("open() " + this);
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
			for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					sub.setDone(false);
					callPath.add(new ControllablePhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(),
							sub.getSourceOutPort(), null));
					if (sub.getTarget().isOwnedByAny(forOwners)) {
						sub.getTarget().open(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			openCloseLock.unlock();
		}
	}



	@SuppressWarnings("static-method")
	private boolean containsSubscription(List<AbstractPhysicalSubscription<ISink<?>>> callPath, ISink<? super R> sink,
			int sourcePort, int sinkPort) {
		for (AbstractPhysicalSubscription<ISink<?>> sub : callPath) {
			if (sub.getTarget() == sink && sub.getSinkInPort() == sinkPort && sub.getSourceOutPort() == sourcePort) {
				// getLogger().trace(
				// "contains " + sink + " " + sourcePort + " " + sinkPort
				// + " in " + callPath);
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
		start(new ArrayList<AbstractPhysicalSubscription<ISink<?>>>(), forOwners);
	}

	protected void start(List<AbstractPhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners)
			throws OpenFailedException {

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
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
			// Check if callPath contains this call already to avoid cycles
			if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new ControllablePhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(),
						sub.getSourceOutPort(), null));
				if (sub.getTarget().isOwnedByAny(forOwners)) {
					sub.getTarget().start(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
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
	final public void process(R object, int port) {
		fire(processInitEvent[port]);
		process_next(object, port);
		fire(processDoneEvent[port]);
	}

	// @Override
	// public void process(Collection<? extends R> object, int port) {
	// for (R cur : object) {
	// process(cur, port);
	// }
	// }

	protected abstract void process_next(R object, int port);

	@Override
	public abstract void processPunctuation(IPunctuation punctuation, int port);

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
		close(new ArrayList<AbstractPhysicalSubscription<ISink<?>>>(), owner);
	}

	public void close(List<AbstractPhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners) {
		logger.trace("Closing for " + forOwners);
		internal_close(callPath, forOwners, true);
	}

	protected void internal_close(List<AbstractPhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners,
			boolean doProcessClose) {

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
		} catch (Exception e) {
			throw e;
		} finally {
			openCloseLock.unlock();
		}
	}

	protected void callCloseOnChildren(List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) {
		openCloseLock.lock();
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
			if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new ControllablePhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(),
						sub.getSourceOutPort(), null));
				try {
					if (sub.getTarget().isOwnedByAny(forOwners)) {
						// Call close only on sources from the list of owners
						ISink<R> instance = getInstance();
						int outport = sub.getSourceOutPort();
						int inPort = sub.getSinkInPort();
						logger.trace("Close for " + getName() + " on Child: " + sub.getTarget() + " in Thread "
								+ Thread.currentThread().getName());
						sub.getTarget().close(instance, outport, inPort, callPath, forOwners);
					}
				} catch (Throwable e) {
					e.printStackTrace();
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
	final synchronized public void done(int port) {
		process_done(port);

		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
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
				logger.error("Error while calling done of owner!", e);
			}
		}
	}

	@Override
	final synchronized public boolean isDone() {
		boolean done = true;
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
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
		suspend(new ArrayList<AbstractPhysicalSubscription<ISink<?>>>(), forOwners);
	}

	final protected void suspend(List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) {

		openCloseLock.lock();
		try {

			// Call open on all registered sources0
			for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					callPath.add(new ControllablePhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(),
							sub.getSourceOutPort(), null));
					if (sub.getTarget().isOwnedByAny(forOwners)) {
						sub.getTarget().suspend(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} catch (Exception e) {
			throw e;
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
		resume(new ArrayList<AbstractPhysicalSubscription<ISink<?>>>(), forOwners);
	}

	final void resume(List<AbstractPhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners) {

		openCloseLock.lock();
		try {

			// Call open on all registered sources0
			for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
				// Check if callPath contains this call already to avoid cycles
				if (!containsSubscription(callPath, getInstance(), sub.getSourceOutPort(), sub.getSinkInPort())) {
					callPath.add(new ControllablePhysicalSubscription<ISink<?>>(getInstance(), sub.getSinkInPort(),
							sub.getSourceOutPort(), null));
					if (sub.getTarget().isOwnedByAny(forOwners)) {
						sub.getTarget().resume(getInstance(), sub.getSourceOutPort(), sub.getSinkInPort(), callPath,
								forOwners);
					}
				}
			}
		} catch (Exception e) {
			throw e;
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
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
			ISource<? extends R> target = sub.getTarget();

			for (AbstractPhysicalSubscription<?> subFromTarget : target.getSubscriptions()) {
				if (subFromTarget.getTarget() == tgt) {
					if (subFromTarget instanceof ControllablePhysicalSubscription) {
						((ControllablePhysicalSubscription<?>) subFromTarget).setSheddingFactor(sheddingFactor);
					}
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
	final public Map<Integer, SDFSchema> getOutputSchemas() {
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

	@Override
	public void subscribeToSource(ISource<? extends R> source, int sinkInPort, int sourceOutPort, SDFSchema schema) {

		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}

		if (sinkInPort >= this.noInputPorts) {
			setInputPortCount(sinkInPort + 1);
		}

		AbstractPhysicalSubscription<ISource<? extends R>> sub = new ControllablePhysicalSubscription<ISource<? extends R>>(
				source, sinkInPort, sourceOutPort, schema);
		if (!this.subscribedToSource.contains(sub)) {
			if (!sinkInPortFree(sinkInPort)) {
				throw new IllegalArgumentException("SinkInPort " + sinkInPort + " already bound ");
			}
			// getLogger().trace(
			// this.getInstance() + " Subscribe To Source " + source
			// + " to " + sinkInPort + " from " + sourceOutPort);
			while (this.subscribedToSource.size() < sub.getSinkInPort() + 1) {
				this.subscribedToSource.add(null);
			}
			this.subscribedToSource.set(sub.getSinkInPort(), sub);

			source.subscribeSink(getInstance(), sinkInPort, sourceOutPort, schema);
			newSourceSubscribed(sub);
		}

	}

	protected void newSourceSubscribed(AbstractPhysicalSubscription<ISource<? extends R>> sub) {
		// Implement this method if need to react to new source subscription
	}

	private int getNextFreeSinkInPort() {
		int sinkInPort = -1;
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() > sinkInPort) {
				sinkInPort = sub.getSinkInPort();
			}
		}
		// und erh�he um eins
		sinkInPort++;
		return sinkInPort;
	}

	private boolean sinkInPortFree(int sinkInPort) {
		for (AbstractPhysicalSubscription<ISource<? extends R>> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() == sinkInPort) {
				logger.error("SinkInPort " + sinkInPort + " already bound to " + sub);
				return false;
			}
		}
		return true;
	}

	@Override
	final public List<AbstractPhysicalSubscription<ISource<? extends R>>> getSubscribedToSource() {
		return Collections.unmodifiableList(this.subscribedToSource);
	}

	@Override
	public void unsubscribeFromSource(AbstractPhysicalSubscription<ISource<? extends R>> subscription) {
		logger.trace("Unsubscribe from Source " + subscription.getTarget());
		if (this.subscribedToSource.remove(subscription)) {
			subscription.getTarget().unsubscribeSink(this.getInstance(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
			sourceUnsubscribed(subscription);
		}
	}

	protected void sourceUnsubscribed(AbstractPhysicalSubscription<ISource<? extends R>> subscription) {
		// Implement this method if need to react to source unsubscription
	}

	@Override
	public void unsubscribeFromSource(ISource<? extends R> source, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		unsubscribeFromSource(
				new ControllablePhysicalSubscription<ISource<? extends R>>(source, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void unsubscribeFromAllSources() {
		while (!subscribedToSource.isEmpty()) {
			AbstractPhysicalSubscription<ISource<? extends R>> subscription = subscribedToSource.remove(0);
			logger.trace("Unsubscribe from Source " + subscription.getTarget());
			subscription.getTarget().unsubscribeSink(this.getInstance(), subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
			logger.trace("Unsubscribe from Source " + subscription.getTarget() + " done.");

		}
	}

	@Override
	final public AbstractPhysicalSubscription<ISource<? extends R>> getSubscribedToSource(int port) {
		return this.subscribedToSource.get(port);
	}

	// ------------------------------------------------------------------------
	// Other Methods
	// ------------------------------------------------------------------------

	@Override
	public String toString() {
		return getInstance().getClass().getSimpleName() + "(" + getInstance().hashCode() + ")";
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
		Map<Integer, SDFSchema> copy = new HashMap<Integer, SDFSchema>();
		for (Entry<Integer, SDFSchema> e : old.entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
	}

	@Override
	public boolean hasInput() {
		return getSubscribedToSource().size() > 0;
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
