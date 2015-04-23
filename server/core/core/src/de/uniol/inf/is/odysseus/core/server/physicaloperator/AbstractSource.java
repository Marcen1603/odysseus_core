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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
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
import de.uniol.inf.is.odysseus.core.streamconnection.DefaultStreamConnection;

/**
 * @author Jonas Jacobi, Tobias Witt, Marco Grawunder
 */
public abstract class AbstractSource<T extends IStreamObject<?>> extends AbstractMonitoringDataProvider
		implements ISource<T> {

	final public int ERRORPORT = Integer.MAX_VALUE;

	// Locking
	private IMyLock locker = new NonLockingLock();
	private ReentrantLock openCloseLock = new ReentrantLock();

	final private List<AbstractPhysicalSubscription<ISink<? super T>>> sinkSubscriptions = new CopyOnWriteArrayList<AbstractPhysicalSubscription<ISink<? super T>>>();
	// Only active subscription are served on transfer
	final private List<AbstractPhysicalSubscription<ISink<? super T>>> activeSinkSubscriptions = new CopyOnWriteArrayList<AbstractPhysicalSubscription<ISink<? super T>>>();
	final private List<AbstractPhysicalSubscription<ISink<? super T>>> connectedSinks = new CopyOnWriteArrayList<AbstractPhysicalSubscription<ISink<? super T>>>();
	private boolean readdedConnectedSinks = false;
	final private Map<Integer, Integer> consumerCount = new HashMap<>();

	protected AtomicBoolean open = new AtomicBoolean(false);

	private String name = null;
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<Integer, SDFSchema>();
	private Map<IOperatorOwner, Resource> uniqueIds = new TreeMap<>();

	final private OwnerHandler ownerHandler;

	private boolean inOrder = true;

	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------

	static private Logger _logger = null;

	private static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(AbstractSource.class);
		}
		return _logger;
	}

	protected boolean debug = false;

	@Override
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	final InfoService infoService;

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

	final private POEvent doneEvent = new POEvent(this, POEventType.Done);
	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);
	final private POEvent pushInitEvent = new POEvent(this,
			POEventType.PushInit);
	final private POEvent pushDoneEvent = new POEvent(this,
			POEventType.PushDone);
	// final private POEvent pushListInitEvent = new POEvent(this,
	// POEventType.PushListInit);
	// final private POEvent pushListDoneEvent = new POEvent(this,
	// POEventType.PushListDone);
	final private POEvent closeInitEvent = new POEvent(this,
			POEventType.CloseInit);
	final private POEvent closeDoneEvent = new POEvent(this,
			POEventType.CloseDone);

	private AtomicBoolean blocked = new AtomicBoolean(false);

	POEvent blockedEvent = new POEvent(this, POEventType.Blocked);
	POEvent unblockedEvent = new POEvent(this, POEventType.Unblocked);

	private Map<String, String> infos = new TreeMap<>();

	// Allow to suppress punctuations
	private boolean suppressPunctuation;

	// ------------------------------------------------------------------

	public AbstractSource() {
		this.ownerHandler = new OwnerHandler();
		infoService = InfoServiceFactory.getInfoService(AbstractSource.class
				.getName());
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
	final public boolean isSource() {
		return true;
	}

	@Override
	final public boolean isPipe() {
		return isSink() && isSource();
	}

	final protected boolean isRoot() {
		return sinkSubscriptions.size() == 0;
	}

	final protected boolean hasSingleConsumer(int port) {
		return consumerCount.get(port) <= 1;
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
	final public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	final public SDFSchema getOutputSchema(int port) {

		return outputSchema.get(port);
	}

	@Override
	final public void setOutputSchema(SDFSchema outputSchema) {
		setOutputSchema(outputSchema, 0);
	}

	@Override
	final public void setOutputSchema(SDFSchema outputSchema, int port) {
		this.outputSchema.put(port, outputSchema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator#
	 * getOutputSchemas()
	 */
	@Override
	final public Map<Integer, SDFSchema> getOutputSchemas() {
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
	synchronized public void open(IOperatorOwner owner)
			throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
		}

		open.set(true);

		reconnectSinks();
	}

	@Override
	public void open(ISink<? super T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) throws OpenFailedException {

		openCloseLock.lock();
		try {

			// Hint: ignore callPath on sources because the source does not call
			// any
			// subscription

			// o can be null, if operator is top operator
			// otherwise top operator cannot be opened
			if (caller != null) {
				// Find subscription for caller
				AbstractPhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
						caller, sourcePort, sinkPort);
				if (sub == null) {
					throw new OpenFailedException(
							"Open called from an unsubscribed sink " + caller);
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
				process_open();
				fire(openDoneEvent);
				open.set(true);
			}

			reconnectSinks();
		} catch (Exception e) {
			throw e;
		} finally {
			openCloseLock.unlock();
		}
	}

	/**
	 * 
	 */
	protected void reconnectSinks() {
		if (connectedSinks.size() > 0 && !readdedConnectedSinks) {
			// re-add all connected sinks
			for (AbstractPhysicalSubscription<ISink<? super T>> s : connectedSinks) {
				if (!activeSinkSubscriptions.contains(s)) {
					activeSinkSubscriptions.add(s);
				}
			}
			readdedConnectedSinks = true;
		}
	}

	private void addActiveSubscription(
			AbstractPhysicalSubscription<ISink<? super T>> sub) {
		// Handle multiple open calls{
		if (!activeSinkSubscriptions.contains(sub)) {
			this.activeSinkSubscriptions.add(sub);
			Integer currentCount;
			if ((currentCount = consumerCount.get(sub.getSourceOutPort())) == null) {
				currentCount = 0;
			}
			consumerCount.put(sub.getSourceOutPort(), currentCount + 1);
			newReceiver(sub);
		}
	}

	protected void newReceiver(
			AbstractPhysicalSubscription<ISink<? super T>> sink) {
		// can be overwritten if needed
	}

	private void removeActiveSubscription(
			AbstractPhysicalSubscription<ISink<? super T>> sub) {
		if (activeSinkSubscriptions.contains(sub)) {
			consumerCount.put(sub.getSourceOutPort(),
					consumerCount.get(sub.getSourceOutPort()) - 1);
			this.activeSinkSubscriptions.remove(sub);
		}
	}

	public void replaceActiveSubscription(
			AbstractPhysicalSubscription<ISink<? super T>> oldSub,
			AbstractPhysicalSubscription<ISink<? super T>> newSub) {
		// necessary to not lose tuples here
		locker.lock();
		removeActiveSubscription(oldSub);
		addActiveSubscription(newSub);
		locker.unlock();
	}

	public void replaceActiveSubscriptions(
			Set<AbstractPhysicalSubscription<ISink<? super T>>> oldSubs,
			Set<AbstractPhysicalSubscription<ISink<? super T>>> newSubs) {
		locker.lock();
		for (AbstractPhysicalSubscription<ISink<? super T>> oldSub : oldSubs) {
			removeActiveSubscription(oldSub);
		}
		for (AbstractPhysicalSubscription<ISink<? super T>> newSub : newSubs) {
			addActiveSubscription(newSub);
		}
		locker.unlock();
	}

	protected abstract void process_open() throws OpenFailedException;

	// ------------------------------------------------------------------------
	// Punctuations
	// ------------------------------------------------------------------------

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		for (AbstractPhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
			sub.getTarget()
					.processPunctuation(punctuation, sub.getSinkInPort());
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		if (!suppressPunctuation) {
			for (AbstractPhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
				if (sub.getSourceOutPort() == outPort) {
					sub.getTarget().processPunctuation(punctuation,
							sub.getSinkInPort());
				}
			}
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
		for (AbstractPhysicalSubscription<ISink<? super T>> sink : this.activeSinkSubscriptions) {
			transfer(object, sourceOutPort, sink);
		}
		locker.unlock();
		fire(this.pushDoneEvent);
	}

	@SuppressWarnings("rawtypes")
	protected void transfer(T object, int sourceOutPort,
			AbstractPhysicalSubscription<ISink<? super T>> sink) {
		if (sink.getSourceOutPort() == sourceOutPort) {
			try {
				// TODO: remove this and implement a handler that set cloning information 
				final boolean nc = needsClone(sourceOutPort);
				if (sink.getTarget() instanceof DefaultStreamConnection){
					sink.setNeedsClone(false);
				}else{
					sink.setNeedsClone(nc);
				}
				
				sink.process((IStreamObject)object);
				
			} catch (Throwable e) {
				// Send object that could not be processed to the error port
				e.printStackTrace();
				transfer(object, ERRORPORT);
			}
		}
	}

	@Override
	public void transfer(T object) {
		transfer(object, 0);
	}

	final public void transfer(Collection<T> list, int sourceOutPort) {
		for (T o : list) {
			transfer(o, sourceOutPort);
		}
	}

	final public void transfer(Collection<T> list) {
		for (T o : list) {
			transfer(o, 0);
		}
	}

	// @Override
	// final public void transfer(Collection<T> object, int sourceOutPort) {
	// fire(this.pushListInitEvent);
	// for (PhysicalSubscription<ISink<? super T>> sink :
	// this.activeSinkSubscriptions) {
	// if (sink.getSourceOutPort() == sourceOutPort) {
	// sink.process(object);
	// }
	// }
	// fire(this.pushListDoneEvent);
	// }
	//
	// @Override
	// final public void transfer(Collection<T> object) {
	// transfer(object, 0);
	// }

	protected boolean needsClone(int port) {
		return !hasSingleConsumer(port);
	}

	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String,
	// etc.)
	// MUST override this method (else there will be a ClassCastException)
//	@SuppressWarnings("unchecked")
//	protected T cloneIfNessessary(T object, int port) {
//		if (needsClone(port)) {
//			object = (T) ((IClone) object).clone();
//		}
//		return object;
//	}

	// ------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------

	@Override
	public void close(IOperatorOwner id) {
		try {
			openCloseLock.lock();
			doLocalClose();
		} finally {
			openCloseLock.unlock();
		}
	}

	@Override
	public void close(ISink<? super T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) {
		try {
			openCloseLock.lock();

			getLogger().trace("CLOSE " + getName());
			AbstractPhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
					caller, sourcePort, sinkPort);
			if (sub == null) {
				throw new RuntimeException(
						"Close called from an unsubscribed sink ");
			}
			getLogger().trace("Closing from " + sub);
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

				// The are some sink, that are not connected by open (because
				// they
				// will never
				// call close) kept in list connectedSinks
				// If all by open connected subscriptions are removed, close
				// operator
				if ((activeSinkSubscriptions.size() - 1) == connectedSinks
						.size()) {
					getLogger().trace("Closing " + toString());
					doLocalClose();
				}
				removeActiveSubscription(sub);
				// Close all sinks that are not connected by open
				if (activeSinkSubscriptions.size() == connectedSinks.size()) {
					closeAllSinkSubscriptions();
				}
			}
		} catch (Exception e) {
			throw e;
		} finally {
			openCloseLock.unlock();
		}
	}

	private void doLocalClose() {
		fire(this.closeInitEvent);
		this.process_close();
		open.set(false);
		stopMonitoring();
		fire(this.closeDoneEvent);
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

	protected void propagateDone() {
		getLogger().trace("Propagate done " + getName());
		// Could be that the query is already closed. In this cases the done
		// event
		// does not of any interest any more
		if (isOpen()) {
			fire(this.doneEvent);
			this.process_done();
			for (AbstractPhysicalSubscription<ISink<? super T>> sub : activeSinkSubscriptions) {
				if (!sub.isDone()) {
					sub.getTarget().done(sub.getSinkInPort());
				}
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
		// synchronized (blocked) {
		this.blocked.set(true);
		getLogger().trace("Operator " + this.toString() + " blocked");
		fire(blockedEvent);
		// }
		locker.lock();
	}

	@Override
	public void unblock() {
		// synchronized (blocked) {
		this.blocked.set(false);
		getLogger().trace("Operator " + this.toString() + " unblocked");
		fire(unblockedEvent);
		// }
		locker.unlock();
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
	public void suspend(ISink<? super T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) {
		if (isOpen()) {
			AbstractPhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
					caller, sourcePort, sinkPort);
			if (sub instanceof ControllablePhysicalSubscription) {
				((ControllablePhysicalSubscription<ISink<? super T>>) sub)
						.suspend();
			} else {
				sendError("This query cannot be suspended",
						new RuntimeException(
								"Query is not in controllable mode!"));
			}
		}
	}

	@Override
	public void resume(ISink<? super T> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<ISink<?>>> callPath,
			List<IOperatorOwner> forOwners) {
		AbstractPhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
				caller, sourcePort, sinkPort);
		if (sub instanceof ControllablePhysicalSubscription) {
			ControllablePhysicalSubscription<ISink<? super T>> csub = (ControllablePhysicalSubscription<ISink<? super T>>) sub;
			if (csub.isSuspended()) {
				csub.resume();
			}
		} else {
			sendError("This query cannot be resumed", new RuntimeException(
					"Query is not in controllable mode!"));
		}
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	private AbstractPhysicalSubscription<ISink<? super T>> findSinkInSubscription(
			IPhysicalOperator o, int sourcePort, int sinkPort) {
		for (AbstractPhysicalSubscription<ISink<? super T>> sub : this.sinkSubscriptions) {
			if (sub.getTarget() == o && sub.getSourceOutPort() == sourcePort
					&& sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}

	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema, boolean asActive, int openCount) {
		// TODO: Make configurable
		AbstractPhysicalSubscription<ISink<? super T>> sub = new ControllablePhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		sub.setOpenCalls(openCount);
		if (!this.sinkSubscriptions.contains(sub)) {
			// getLogger().trace(
			// this + " Subscribe Sink " + sink + " to " + sinkInPort
			// + " from " + sourceOutPort);
			this.sinkSubscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
			if (asActive) {
				addActiveSubscription(sub);
			}
		}
	}

	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema, false, 0);
	}

	@Override
	public void connectSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		AbstractPhysicalSubscription<ISink<? super T>> sub = new ControllablePhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		sink.addOwner(this.getOwner());
		addActiveSubscription(sub);
		connectedSinks.add(sub);
	}

	@Override
	final public void unsubscribeSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new ControllablePhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void disconnectSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		// unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
		AbstractPhysicalSubscription<ISink<? super T>> sub = new ControllablePhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		removeActiveSubscription(sub);
		connectedSinks.remove(sub);
	}

	final protected void closeAllSinkSubscriptions() {
		// Do not close these connections to allow them to reconnect at open
		// time
		// for (PhysicalSubscription<ISink<? super T>> sup : connectedSinks) {
		// sup.getTarget().close();
		// }
		// connectedSinks.clear();
		readdedConnectedSinks = false;
		activeSinkSubscriptions.clear();
	}

	public boolean isActive(
			AbstractPhysicalSubscription<ISink<? super T>> subscription) {
		return this.activeSinkSubscriptions.contains(subscription);
	}

	@Override
	public void unsubscribeSink(
			AbstractPhysicalSubscription<ISink<? super T>> subscription) {
		getLogger().trace("Unsubscribe from Sink " + subscription.getTarget());
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		removeActiveSubscription(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeFromSource(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	// // Das folgende macht eigentlich keinen Sinn mehr mit CopyOnWrite
	// // Arrays (MG)
	// @Override
	// public void atomicReplaceSink(
	// List<PhysicalSubscription<ISink<? super T>>> remove,
	// ISink<? super T> sink, int sinkInPort, int sourceOutPort,
	// SDFSchema schema) {
	// // synchronized (this.sinkSubscriptions) {
	// for (PhysicalSubscription<ISink<? super T>> sub : remove) {
	// unsubscribeSink(sub);
	// }
	// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	// // }
	// }
	//
	// // Das folgende macht eigentlich keinen Sinn mehr mit CopyOnWrite
	// // Arrays (MG)
	// @Override
	// public void atomicReplaceSink(
	// PhysicalSubscription<ISink<? super T>> remove,
	// List<ISink<? super T>> sinks, int sinkInPort, int sourceOutPort,
	// SDFSchema schema) {
	// // synchronized (this.sinkSubscriptions) {
	// unsubscribeSink(remove);
	// for (ISink<? super T> sink : sinks) {
	// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	// }
	// // }
	// }

	@Override
	final public List<AbstractPhysicalSubscription<ISink<? super T>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")"
				+ (blocked.get() ? "b" : "");
	}

	@Override
	public void unsubscribeFromAllSinks() {
		for (AbstractPhysicalSubscription<ISink<? super T>> s : this.sinkSubscriptions) {
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
	final public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo.isSource() || ipo instanceof IPipe))
			return false;
		return process_isSemanticallyEqual(ipo);
	}

	// TODO: Make abstract again and implement in Children
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}

	// private static Map<Integer, SDFSchema> createCleanClone(
	// Map<Integer, SDFSchema> old) {
	// Map<Integer, SDFSchema> copy = new HashMap<Integer, SDFSchema>();
	// for (Entry<Integer, SDFSchema> e : old.entrySet()) {
	// copy.put(e.getKey(),
	// new SDFSchema(e.getValue().getURI(), e.getValue()));
	// }
	// return copy;
	// }

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
		infoService.warning(message);
	}

	protected void sendInfo(String message) {
		infoService.info(message);
	}
}
