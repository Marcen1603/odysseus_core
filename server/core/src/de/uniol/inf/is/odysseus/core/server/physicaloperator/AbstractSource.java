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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.lock.IMyLock;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.lock.NonLockingLock;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Jonas Jacobi, Tobias Witt, Marco Grawunder
 */
public abstract class AbstractSource<T extends IStreamObject<?>> extends AbstractMonitoringDataProvider
		implements ISource<T> {

	public static final int ERRORPORT = Integer.MAX_VALUE;

	// Locking
	private IMyLock locker = new NonLockingLock();
	private ReentrantLock openCloseLock = new ReentrantLock();

	private final List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> sinkSubscriptions = new CopyOnWriteArrayList<>();
	// Only active subscription are served on transfer
	private final List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> activeSinkSubscriptions = new CopyOnWriteArrayList<>();
	private final List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> connectedSinks = new CopyOnWriteArrayList<>();

	private final Comparator<AbstractPhysicalSubscription<?, ?>> sortByCloneComparator = new Comparator<AbstractPhysicalSubscription<?, ?>>() {

		@Override
		public int compare(AbstractPhysicalSubscription<?, ?> left, AbstractPhysicalSubscription<?, ?> right) {
			if (left.isNeedsClone() == right.isNeedsClone()) {
				return 0;
			}
			if (left.isNeedsClone() && !right.isNeedsClone()) {
				return -1;
			}
			return 1;
		}

	};
	
	private boolean readdedConnectedSinks = false;
	private final Map<Integer, Integer> consumerCount = new HashMap<>();

	protected AtomicBoolean open = new AtomicBoolean(false);
	protected AtomicBoolean started = new AtomicBoolean(false);

	private String name = null;
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<>();
	private Map<IOperatorOwner, Resource> uniqueIds = new TreeMap<>();

	private final OwnerHandler ownerHandler;

	private boolean inOrder = true;

	private ILogicalOperator definingOp;

	/**
	 * For stateful operators, this flag signals, that an operator state has been
	 * loaded.
	 */
	private boolean operatorStateLoaded = false;
	
	// --------------------------------------------------------------------
	// Identification 
	// --------------------------------------------------------------------

	private final UUID uuid = UUID.randomUUID();

	public UUID getUUID() {
		return uuid;
	}

	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------

	private static Logger LOGGER = LoggerFactory.getLogger(AbstractSource.class);

	protected boolean debug = false;

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	protected final InfoService infoService;

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

	private final POEvent doneEvent = new POEvent(this, POEventType.Done);
	private final POEvent openInitEvent = new POEvent(this, POEventType.OpenInit);
	private final POEvent openDoneEvent = new POEvent(this, POEventType.OpenDone);
	private final POEvent pushInitEvent = new POEvent(this, POEventType.PushInit);
	private final POEvent pushDoneEvent = new POEvent(this, POEventType.PushDone);
	private final POEvent closeInitEvent = new POEvent(this, POEventType.CloseInit);
	private final POEvent closeDoneEvent = new POEvent(this, POEventType.CloseDone);

	private AtomicBoolean blocked = new AtomicBoolean(false);

	POEvent blockedEvent = new POEvent(this, POEventType.Blocked);
	POEvent unblockedEvent = new POEvent(this, POEventType.Unblocked);

	private Map<String, String> infos = new TreeMap<>();

	// Allow to suppress punctuations
	private boolean suppressPunctuation;

	// ------------------------------------------------------------------

	public AbstractSource() {
		this.ownerHandler = new OwnerHandler();
		infoService = InfoServiceFactory.getInfoService(AbstractSource.class.getName());
	}

	public AbstractSource(AbstractSource<T> source) {
		this.outputSchema = source.outputSchema;
		this.blocked = new AtomicBoolean();
		this.blocked.set(source.blocked.get());
		this.name = source.name;
		this.ownerHandler = new OwnerHandler(source.ownerHandler);
		this.infos = new TreeMap<>(source.infos);
		infoService = InfoServiceFactory.getInfoService(name);
	}

	@Override
	public boolean isSink() {
		return false;
	}

	@Override
	public final boolean isSource() {
		return true;
	}

	@Override
	public final boolean isPipe() {
		return isSink() && isSource();
	}

	protected final boolean isRoot() {
		return !sinkSubscriptions.isEmpty();
	}

	protected final boolean hasSingleConsumer(int port) {
		return consumerCount.get(port) <= 1;
	}

	// Returns wether this source delivers an element which is also stored in
	// this source.
	// The element then may not be modified since this source needs it in its
	// original state
	public boolean deliversStoredElement(int outputPort) {
		LOGGER.warn("Operator {} doesn't implement method deliversStoredElement. Cloning performance may suffer! Was called on port {}",getName(), outputPort);
		return true;
	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName();
		}
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setSuppressPunctuations(boolean suppressPunctuation) {
		this.suppressPunctuation = suppressPunctuation;
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
	public final SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public final SDFSchema getOutputSchema(int port) {
		return outputSchema.get(port);
	}

	@Override
	public final void setOutputSchema(SDFSchema outputSchema) {
		setOutputSchema(outputSchema, 0);
	}

	@Override
	public final void setOutputSchema(SDFSchema outputSchema, int port) {
		this.outputSchema.put(port, outputSchema);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator#
	 * getOutputSchemas()
	 */
	@Override
	public final Map<Integer, SDFSchema> getOutputSchemas() {
		return Collections.unmodifiableMap(this.outputSchema);
	}

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public boolean isOpen() {
		return open.get();
	}

	@Override
	public synchronized void open(IOperatorOwner owner){
		openCloseLock.lock();
		try {
			if (!isOpen()) {
				fire(openInitEvent);
				process_open();
				fire(openDoneEvent);
			}

			open.set(true);

			reconnectSinks();
		} finally {
			openCloseLock.unlock();
		}
	}

	@Override
	public void open(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {

		openCloseLock.lock();
		try {

			// Hint: ignore callPath on sources because the source does not call
			// any
			// subscription

			// o can be null, if operator is top operator
			// otherwise top operator cannot be opened
			if (caller != null) {
				// Find subscription for caller
				AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller,
						sourcePort, sinkPort);
				if (sub == null) {
					throw new OpenFailedException("Open called from an unsubscribed sink " + caller);
				}
				// Add Subscription to the list of active subscriptions
				addActiveSubscription(sub);

				// increase numer of open calls for this subscription
				// Hint: Because of query sharing, there can be more than one
				// way, an open call occurs
				// op1 --> op2 --> op3
				// op4 --> op2
				// We need to remember how many times open was called, and
				// decrement
				// by close calls
				// Remove subscription if no one is interested anymore (i.e. the
				// number
				// of open calls == 0) --> Remove from activeSubscriptions
				// Operator can be closed if all active Subscriptions are
				// removed
				sub.incOpenCalls();
			}
			// Because of multiple calls from different source, the operator may
			// already have been initialized (isOpen())
			// in other cases open the operator
			if (!isOpen()) {
				fire(openInitEvent);
				// Avoid initalizing of operator state after loading it per
				// setState (see IStatefulPO)
				if (!this.operatorStateLoaded) {
					process_open();
				}
				fire(openDoneEvent);
				open.set(true);
			}

			reconnectSinks();
		} finally {
			openCloseLock.unlock();
		}
	}

	/**
	 *
	 */
	protected void reconnectSinks() {
		if (!connectedSinks.isEmpty() && !readdedConnectedSinks) {
			// re-add all connected sinks
			for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> s : connectedSinks) {
				if (!activeSinkSubscriptions.contains(s)) {
					activeSinkSubscriptions.add(s);
				}
			}
			readdedConnectedSinks = true;
		}
	}

	private void addActiveSubscription(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub) {
		if (!activeSinkSubscriptions.contains(sub)) {
			this.activeSinkSubscriptions.add(sub);
			Integer currentCount;
			if ((currentCount = consumerCount.get(sub.getSourceOutPort())) == null) {
				currentCount = 0;
			}
			consumerCount.put(sub.getSourceOutPort(), currentCount + 1);

			// the subscriptions must be ordered. All elements that clone need
			// to be the first in the list
			Collections.sort(activeSinkSubscriptions, this.sortByCloneComparator);

			newReceiver(sub);
		}
	}

	protected void newReceiver(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sink) {
		// can be overwritten if needed
	}

	private void removeActiveSubscription(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub) {
		if (activeSinkSubscriptions.contains(sub)) {
			consumerCount.put(sub.getSourceOutPort(), consumerCount.get(sub.getSourceOutPort()) - 1);
			this.activeSinkSubscriptions.remove(sub);
		}
	}

	public void replaceActiveSubscription(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> oldSub,
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> newSub) {
		// necessary to not lose tuples here
		locker.lock();
		removeActiveSubscription(oldSub);
		addActiveSubscription(newSub);
		locker.unlock();
	}

	public void replaceActiveSubscriptions(Set<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> oldSubs,
			Set<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> newSubs) {
		locker.lock();
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> oldSub : oldSubs) {
			removeActiveSubscription(oldSub);
		}
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> newSub : newSubs) {
			addActiveSubscription(newSub);
		}
		locker.unlock();
	}

	protected abstract void process_open();

	// ------------------------------------------------------------------------
	// start
	// ------------------------------------------------------------------------
	@Override
	public void start(IOperatorOwner id) {
		if (!isStarted()) {
			process_start();
		}
		started.set(true);
	}

	@Override
	public void start(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {

		// Hint: ignore callPath on sources because the source does not call
		// any subscription

		// o can be null, if operator is top operator
		// otherwise top operator cannot be opened
		if (caller != null) {
			// Find subscription for caller
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
					sinkPort);
			if (sub == null) {
				throw new StartFailedException("Start called from an unsubscribed sink " + caller);
			}
		}

		// Because of multiple calls from different source, the operator may
		// already have been started (isStarted())
		// in other cases start the operator
		if (!isStarted()) {
			process_start();
			started.set(true);
		}
	}

	@Override
	public boolean isStarted() {
		return started.get();
	}

	protected abstract void process_start();

	// ------------------------------------------------------------------------
	// Punctuations
	// ------------------------------------------------------------------------

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		if (!suppressPunctuation) {
			for (AbstractPhysicalSubscription<?, ? extends ISink<?>> sub : this.activeSinkSubscriptions) {
				sub.getSink().processPunctuation(punctuation, sub.getSinkInPort());
			}
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		if (!suppressPunctuation) {
			for (AbstractPhysicalSubscription<?, ? extends ISink<?>> sub : this.activeSinkSubscriptions) {
				sendPunctuation(punctuation, outPort, sub);
			}
		}
	}

	protected void sendPunctuation(IPunctuation punctuation, int outPort,
			AbstractPhysicalSubscription<?, ? extends ISink<?>> sub) {
		if (sub.getSourceOutPort() == outPort) {
			sub.getSink().processPunctuation(punctuation, sub.getSinkInPort());
		}
	}

	// ------------------------------------------------------------------------
	// TRANSFER
	// ------------------------------------------------------------------------

	@Override
	public void transfer(T object, int sourceOutPort) {
		fire(this.pushInitEvent);
		// necessary to not lose tuples in a plan migration
		locker.lock();
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sink : this.activeSinkSubscriptions) {
			transfer(object, sourceOutPort, sink);
		}
		locker.unlock();
		fire(this.pushDoneEvent);
	}

	protected void transfer(T object, int sourceOutPort,
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sink) {
		if (sink.getSourceOutPort() == sourceOutPort) {
			try {
				sink.process(object);
			} catch (Exception e) {
				// Send object that could not be processed to the error port
				LOGGER.error("Error Processing Object", e);
				transfer(object, ERRORPORT);
			}
		}
	}

	@Override
	public void transfer(T object) {
		transfer(object, 0);
	}

	public final void transfer(Collection<T> list, int sourceOutPort) {
		for (T o : list) {
			transfer(o, sourceOutPort);
		}
	}

	public final void transfer(Collection<T> list) {
		for (T o : list) {
			transfer(o, 0);
		}
	}

	// ------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------

	@Override
	public void close(IOperatorOwner id) {
		try {
			openCloseLock.lock();
			// this method is called, if the query contains only the source
			// other queries may contain this source as part --> So do not close
			// in every case
			doLocalClose();
		} finally {
			openCloseLock.unlock();
		}
	}

	@Override
	public void close(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		try {
			openCloseLock.lock();
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("CLOSE {}", getName());
			}
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
					sinkPort);
			if (sub == null) {
				throw new RuntimeException("Close called from an unsubscribed sink ");
			}
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Closing from {}", sub);
			}
			// Hint: Multiple Open calls can occur per subscription because of
			// query
			// sharing
			// Op1 --> Op2 --> Op3
			// Op4 --> Op2
			// Op3 must not be closed before Op1 and Op4 have called close (via
			// Op2)
			sub.decOpenCalls();
			// if this subscription has no more callers, remove it from
			// the set of activeSubscriptions
			if (sub.getOpenCalls() == 0) {
				doLocalClose();
				removeActiveSubscription(sub);
				// Close all sinks that are not connected by open
				if (activeSinkSubscriptions.size() == connectedSinks.size()) {
					closeAllSinkSubscriptions();
				}
			}
			this.operatorStateLoaded = false;
		} finally {
			openCloseLock.unlock();
		}
	}

	private void doLocalClose() {
		// The are some sink, that are not connected by open (because
		// they
		// will never
		// call close) kept in list connectedSinks
		// If all by open connected subscriptions are removed, close
		// operator
		if (activeSinkSubscriptions.size() == connectedSinks.size()
				|| (activeSinkSubscriptions.size() - 1 == connectedSinks.size())) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Closing {}", this);
			}
			fire(this.closeInitEvent);
			this.process_close();
			open.set(false);
			started.set(false);
			stopMonitoring();
			fire(this.closeDoneEvent);
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Closing {} done ", this);
			}
		}
	}

	protected void process_close() {
	}

	// ------------------------------------------------------------------------
	// DONE
	// ------------------------------------------------------------------------

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	}

	public final void propagateDone() {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Propagate done {}", getName());
		}
		// Could be that the query is already closed. In this cases the done
		// event
		// does not of any interest any more
		if (isOpen()) {
			fire(this.doneEvent);
			this.process_done();
			for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : activeSinkSubscriptions) {
				if (!sub.isDone()) {
					sub.getSink().done(sub.getSinkInPort());
				}
			}
		}
		if (isRoot()) {
			try {
				for (IOperatorOwner owner : getOwner()) {
					owner.done(this);
				}
			} catch (NoSuchElementException e) {
				LOGGER.error("Error while calling done of owner!", e);
			}
		}
	}

	@Override
	public boolean isDone() {
		return false;
	}

	// ------------------------------------------------------------------------
	// BLOCK
	// ------------------------------------------------------------------------

	@Override
	public boolean isBlocked() {
		return blocked.get();
	}

	@Override
	public void block() {
		this.blocked.set(true);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Operator {} blocked.", this);
		}
		fire(blockedEvent);
		locker.lock();
	}

	@Override
	public void unblock() {
		this.blocked.set(false);
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Operator {} unblocked.", this);
		}
		fire(unblockedEvent);
	}

	// ------------------------------------------------------------------------
	// LOCK
	// ------------------------------------------------------------------------

	public boolean isLocked() {
		return this.locker.isLocked();
	}

	public void setLocker(IMyLock locker) {
		this.locker = locker;
	}

	// ------------------------------------------------------------------------
	// SUSPEND/RESUME
	// ------------------------------------------------------------------------
	@Override
	public void suspend(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		if (isOpen()) {
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
					sinkPort);
			if (sub instanceof ControllablePhysicalSubscription) {
				((ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>) sub).suspend();
			} else {
				sendError("This query cannot be suspended", new RuntimeException("Query is not in controllable mode!"));
			}
		}
	}

	@Override
	public void resume(ISink<? extends T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
				sinkPort);
		if (sub instanceof ControllablePhysicalSubscription) {
			ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>> csub = (ControllablePhysicalSubscription<?, ISink<IStreamObject<?>>>) sub;
			if (csub.isSuspended()) {
				csub.resume();
			}
		} else {
			sendError("This query cannot be resumed", new RuntimeException("Query is not in controllable mode!"));
		}
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	private AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> findSinkInSubscription(IPhysicalOperator o,
			int sourcePort, int sinkPort) {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : this.sinkSubscriptions) {
			IPhysicalOperator sink = sub.getSink();
			if (sink == o && sub.getSourceOutPort() == sourcePort && sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}

	@Override
	public final void subscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema,
			boolean asActive, int openCount) {
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		sub.setOpenCalls(openCount);
		subscribeSink(sub, asActive);
	}

	private void subscribeSink(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub,
			boolean asActive) {
		if (!this.sinkSubscriptions.contains(sub)) {
			this.sinkSubscriptions.add(sub);
			sub.getSink().subscribeToSource((AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>) sub);
			if (asActive) {
				addActiveSubscription(sub);
			}
		}
	}

	@Override
	public final void subscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema, false, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void subscribeSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		subscribeSink((AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>) subscription,
				false);
	}

	@Override
	public void connectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		// All connected sinks needs a cloned version as the sinks are connected
		// without
		// running the query translation phase
		sub.setNeedsClone(true);
		sink.addOwner(this.getOwner());
		sink.setInputPortCount(sinkInPort+1);
		addActiveSubscription(sub);
		connectedSinks.add(sub);
	}
	
	@Override
	public Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> getConnectedSinks() {
		return Collections.unmodifiableCollection(connectedSinks);
	}

	@SuppressWarnings("unchecked")
	@Override
	public final void unsubscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		unsubscribeSink(new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void disconnectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		disconnectSink(sub);
	}
	
	@Override
	public void disconnectSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		removeActiveSubscription(subscription);
		connectedSinks.remove(subscription);
	}

	protected final void closeAllSinkSubscriptions() {
		// Do not close these connections to allow them to reconnect at open time
		readdedConnectedSinks = false;
		activeSinkSubscriptions.clear();
	}

	public boolean isActive(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		return this.activeSinkSubscriptions.contains(subscription);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribeSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Unsubscribe from Sink {}", subscription.getSource());
		}
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		removeActiveSubscription(subscription);
		if (subContained) {
			subscription.getSink()
					.unsubscribeFromSource((AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>) subscription);
		}
	}

	@Override
	public final List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")" + (blocked.get() ? "b" : "") + "(o="
				+ isOpen() + ")" + "(d=" + isDone() + ")";
	}

	@Override
	public void unsubscribeFromAllSinks() {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> s : this.sinkSubscriptions) {
			unsubscribeSink(s);
		}
		sinkSubscriptions.clear();
	}

	protected boolean hasOpenSinkSubscriptions() {
		return connectedSinks.size() != activeSinkSubscriptions.size();
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
	// Order
	// ------------------------------------------------------------------------

	public boolean isInOrder() {
		return inOrder;
	}

	public void setInOrder(boolean isInOrder) {
		this.inOrder = isInOrder;
	}

	// ------------------------------------------------------------------------
	// Other Methods
	// ------------------------------------------------------------------------

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo.isSource() || ipo instanceof IPipe))
			return false;
		return process_isSemanticallyEqual(ipo);
	}

	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("No implementation for process_isSemanticallyEqual in operator {}. Cannot compare to {}!",this.getName(), ipo.getName());
		}
		return false;
	}

	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		// in general there is no metadata
		return new SDFMetaAttributeList();
	}

	// ------------------------------------------------------
	// Memory Management
	// ------------------------------------------------------
	public long getElementsStored1() {
		return -1;
	}

	public long getElementsStored2() {
		return -1;
	}

	@Override
	public boolean hasInput() {
		return false;
	}

	// --------------------------------------------------------
	// Information Management
	// -------------------------------------------------------
	protected void sendError(String message, Throwable t) {
		infoService.error(message, t);
	}

	protected void sendWarning(String message) {
		infoService.warning(message);
	}

	protected void sendWarning(String message, Throwable t) {
		infoService.warning(message, t);
	}

	protected void sendInfo(String message) {
		infoService.info(message);
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

	// Methods for operator states
	// see IStatefulPO

	/**
	 * Default implementation of {@link #setState(Serializable)} that does nothing.
	 * Override it for stateful operators.
	 */
	protected void setStateInternal(Serializable state) {
	}

	/**
	 * Sets an operator state.
	 */
	public void setState(Serializable state) {
		setStateInternal(state);
		this.operatorStateLoaded = true;
	}

}
