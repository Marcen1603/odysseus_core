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
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;

/**
 * @author Jonas Jacobi, Tobias Witt, Marco Grawunder
 */
public abstract class AbstractSource<T> extends AbstractMonitoringDataProvider implements ISource<T> {

	final public int ERRORPORT = Integer.MAX_VALUE;

	final private ReentrantLock lock = new ReentrantLock();
	final private List<PhysicalSubscription<ISink<? super T>>> sinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super T>>>();
	// Only active subscription are served on transfer
	final private List<PhysicalSubscription<ISink<? super T>>> activeSinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super T>>>();
	final private List<PhysicalSubscription<ISink<? super T>>> connectedSinks = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super T>>>();
	private boolean readdedConnectedSinks = false;
	final private Map<Integer, Integer> consumerCount = new HashMap<>();

	protected AtomicBoolean open = new AtomicBoolean(false);
	private String name = null;
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<Integer, SDFSchema>();
	private Map<IOperatorOwner, String> uniqueIds = new TreeMap<>();

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
	final private POEvent openInitEvent = new POEvent(this, POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this, POEventType.OpenDone);
	final private POEvent pushInitEvent = new POEvent(this, POEventType.PushInit);
	final private POEvent pushDoneEvent = new POEvent(this, POEventType.PushDone);
	final private POEvent pushListInitEvent = new POEvent(this, POEventType.PushListInit);
	final private POEvent pushListDoneEvent = new POEvent(this, POEventType.PushListDone);
	final private POEvent closeInitEvent = new POEvent(this, POEventType.CloseInit);
	final private POEvent closeDoneEvent = new POEvent(this, POEventType.CloseDone);

	private AtomicBoolean blocked = new AtomicBoolean(false);

	POEvent blockedEvent = new POEvent(this, POEventType.Blocked);
	POEvent unblockedEvent = new POEvent(this, POEventType.Unblocked);

	private Map<String, String> infos = new TreeMap<>();

	// ------------------------------------------------------------------

	public AbstractSource() {
		this.ownerHandler = new OwnerHandler();
	}

	public AbstractSource(AbstractSource<T> source) {
		this.outputSchema = createCleanClone(source.outputSchema);
		this.blocked = new AtomicBoolean();
		this.blocked.set(source.blocked.get());
		this.name = source.name;
		this.ownerHandler = new OwnerHandler(source.ownerHandler);
		this.infos = new TreeMap<>(source.infos);
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

	final protected boolean hasSingleConsumer(int port) {
		return consumerCount.get(port) <= 1;
	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
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
	public void open(ISink<? super T> caller, int sourcePort, int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners)
			throws OpenFailedException {

		// Hint: ignore callPath on sources because the source does not call any
		// subscription

		// o can be null, if operator is top operator
		// otherwise top operator cannot be opened
		if (caller != null) {
			// Find subscription for caller
			PhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(caller, sourcePort, sinkPort);
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
			// We need to remember how many times open was called, and decrement
			// by close calls
			// Remove subscription if no one is interested anymore (i.e. the
			// number
			// of open calls == 0) --> Remove from activeSubscriptions
			// Operator can be closed if all active Subscriptions are removed
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
	}

	/**
	 * 
	 */
	protected void reconnectSinks() {
		if (connectedSinks.size() > 0 && !readdedConnectedSinks) {
			// re-add all connected sinks
			for (PhysicalSubscription<ISink<? super T>> s : connectedSinks) {
				if (!activeSinkSubscriptions.contains(s)) {
					activeSinkSubscriptions.add(s);
				}
			}
			readdedConnectedSinks = true;
		}
	}

	private void addActiveSubscription(PhysicalSubscription<ISink<? super T>> sub) {
		// Handle multiple open calls{
		if (!activeSinkSubscriptions.contains(sub)) {
			this.activeSinkSubscriptions.add(sub);
			Integer currentCount;
			if ((currentCount = consumerCount.get(sub.getSourceOutPort())) == null) {
				currentCount = 0;
			}
			consumerCount.put(sub.getSourceOutPort(), currentCount + 1);
		}
	}

	private void removeActiveSubscription(PhysicalSubscription<ISink<? super T>> sub) {
		if (activeSinkSubscriptions.contains(sub)) {
			consumerCount.put(sub.getSourceOutPort(), consumerCount.get(sub.getSourceOutPort()) - 1);
			this.activeSinkSubscriptions.remove(sub);
		}
	}
	
	public void replaceActiveSubscription(PhysicalSubscription<ISink<? super T>> oldSub, PhysicalSubscription<ISink<? super T>> newSub) {
		// necessary to not lose tuples here
		lock.lock();
		removeActiveSubscription(oldSub);
		addActiveSubscription(newSub);
		lock.unlock();
	}

	protected abstract void process_open() throws OpenFailedException;

	// ------------------------------------------------------------------------
	// Punctuations
	// ------------------------------------------------------------------------

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		for (PhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
			sub.getTarget().processPunctuation(punctuation, sub.getSinkInPort());
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		for (PhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
			if (sub.getSourceOutPort() == outPort) {
				sub.getTarget().processPunctuation(punctuation, sub.getSinkInPort());
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
		lock.lock();
		for (PhysicalSubscription<ISink<? super T>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				try {
					sink.getTarget().process(cloneIfNessessary(object, sourceOutPort), sink.getSinkInPort());
				} catch (Exception e) {
					// Send object that could not be processed to the error port
					e.printStackTrace();
					transfer(object, ERRORPORT);
				}
			}
		}
		lock.unlock();
		fire(this.pushDoneEvent);
	}

	@Override
	public void transfer(T object) {
		transfer(object, 0);
	}

	@Override
	public void transfer(Collection<T> object, int sourceOutPort) {
		fire(this.pushListInitEvent);
		for (PhysicalSubscription<ISink<? super T>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				sink.getTarget().process(object, sink.getSinkInPort());
			}
		}
		fire(this.pushListDoneEvent);
	}

	@Override
	public void transfer(Collection<T> object) {
		transfer(object, 0);
	}

	protected boolean needsClone(int port) {
		return !hasSingleConsumer(port);
	}

	// Classes for Objects not implementing IClone (e.g. ByteBuffer, String,
	// etc.)
	// MUST override this method (else there will be a ClassCastException)
	@SuppressWarnings("unchecked")
	protected T cloneIfNessessary(T object, int port) {
		if (needsClone(port)) {
			object = (T) ((IClone) object).clone();
		}
		return object;
	}

	// ------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------

	@Override
	public void close(ISink<? super T> caller, int sourcePort, int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath, List<IOperatorOwner> forOwners) {
		PhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(caller, sourcePort, sinkPort);
		if (sub == null) {
			throw new RuntimeException("Close called from an unsubscribed sink ");
		}
		// Hint: Multiple Open calls can occur per subscription because of query
		// sharing
		// Op1 --> Op2 --> Op3
		// Op4 --> Op2
		// Op3 must not be closed before Op1 and Op4 have called close (via Op2)
		sub.decOpenCalls();
		// if this subscription has no more callers, remove it from
		// the set of activeSubscriptions
		if (sub.getOpenCalls() == 0) {

			// The are some sink, that are not connected by open (because they
			// will never
			// call close) kept in list connectedSinks
			// If all by open connected subscriptions are removed, close
			// operator
			if ((activeSinkSubscriptions.size() - 1) == connectedSinks.size()) {
				getLogger().debug("Closing " + toString());
				fire(this.closeInitEvent);
				this.process_close();
				open.set(false);
				stopMonitoring();
				fire(this.closeDoneEvent);
			}
			removeActiveSubscription(sub);
			// Close all sinks that are not connected by open
			if (activeSinkSubscriptions.size() == connectedSinks.size()) {
				closeAllSinkSubscriptions();
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

	final protected void propagateDone() {
		// Could be that the query is already closed. In this cases the done
		// event
		// does not of any interest any more
		if (isOpen()) {
			fire(this.doneEvent);
			this.process_done();
			for (PhysicalSubscription<ISink<? super T>> sub : sinkSubscriptions) {
				if (!sub.isDone()) {
					sub.getTarget().done(sub.getSinkInPort());
				}
			}
		}
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
		getLogger().debug("Operator " + this.toString() + " blocked");
		fire(blockedEvent);
		// }
		lock.lock();
	}

	@Override
	public void unblock() {
		// synchronized (blocked) {
		this.blocked.set(false);
		getLogger().debug("Operator " + this.toString() + " unblocked");
		fire(unblockedEvent);
		// }
		lock.unlock();
	}

	// ------------------------------------------------------------------------
	// LOCK
	// ------------------------------------------------------------------------
	
	public boolean isLocked() {
		return this.lock.isLocked();
	}
	
	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	private PhysicalSubscription<ISink<? super T>> findSinkInSubscription(IPhysicalOperator o, int sourcePort, int sinkPort) {
		for (PhysicalSubscription<ISink<? super T>> sub : this.sinkSubscriptions) {
			if (sub.getTarget() == o && sub.getSourceOutPort() == sourcePort && sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}

	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(sink, sinkInPort, sourceOutPort, schema);
		if (!this.sinkSubscriptions.contains(sub)) {
			// getLogger().debug(
			// this + " Subscribe Sink " + sink + " to " + sinkInPort
			// + " from " + sourceOutPort);
			this.sinkSubscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void connectSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(sink, sinkInPort, sourceOutPort, schema);
		sink.addOwner(this.getOwner());
		addActiveSubscription(sub);
		connectedSinks.add(sub);
	}

	@Override
	final public void unsubscribeSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new PhysicalSubscription<ISink<? super T>>(sink, sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void disconnectSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(sink, sinkInPort, sourceOutPort, schema);
		this.activeSinkSubscriptions.remove(sub);
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

	@Override
	public void unsubscribeSink(PhysicalSubscription<ISink<? super T>> subscription) {
		getLogger().debug("Unsubscribe from Sink " + subscription.getTarget());
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeFromSource(this, subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	// TODO: Das folgende macht eigentlich keinen Sinn mehr mit CopyOnWrite
	// Arrays (MG)
	@Override
	public void atomicReplaceSink(List<PhysicalSubscription<ISink<? super T>>> remove, ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// synchronized (this.sinkSubscriptions) {
		for (PhysicalSubscription<ISink<? super T>> sub : remove) {
			unsubscribeSink(sub);
		}
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		// }
	}

	// TODO: Das folgende macht eigentlich keinen Sinn mehr mit CopyOnWrite
	// Arrays (MG)
	@Override
	public void atomicReplaceSink(PhysicalSubscription<ISink<? super T>> remove, List<ISink<? super T>> sinks, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// synchronized (this.sinkSubscriptions) {
		unsubscribeSink(remove);
		for (ISink<? super T> sink : sinks) {
			subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		}
		// }
	}

	@Override
	final public List<PhysicalSubscription<ISink<? super T>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")" + (blocked.get() ? "b" : "");
	}

	@Override
	public void unsubscribeFromAllSinks() {
		for (PhysicalSubscription<ISink<? super T>> s : this.sinkSubscriptions) {
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

	public void addOwner(IOperatorOwner owner) {
		ownerHandler.addOwner(owner);
	}

	public void addOwner(Collection<IOperatorOwner> owner) {
		ownerHandler.addOwner(owner);
	}

	public void removeOwner(IOperatorOwner owner) {
		ownerHandler.removeOwner(owner);
	}

	public void removeAllOwners() {
		ownerHandler.removeAllOwners();
	}

	public boolean isOwnedBy(IOperatorOwner owner) {
		return ownerHandler.isOwnedBy(owner);
	}

	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAny(owners);
	}

	public boolean hasOwner() {
		return ownerHandler.hasOwner();
	}

	public List<IOperatorOwner> getOwner() {
		return ownerHandler.getOwner();
	}

	public String getOwnerIDs() {
		return ownerHandler.getOwnerIDs();
	}

	// ------------------------------------------------------------------------
	// Id Management
	// ------------------------------------------------------------------------

	@Override
	public void addUniqueId(IOperatorOwner owner, String id) {
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
	public Map<IOperatorOwner, String> getUniqueIds() {
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
	abstract public AbstractSource<T> clone();

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

	private static Map<Integer, SDFSchema> createCleanClone(Map<Integer, SDFSchema> old) {
		Map<Integer, SDFSchema> copy = new HashMap<Integer, SDFSchema>();
		for (Entry<Integer, SDFSchema> e : old.entrySet()) {
			copy.put(e.getKey(), new SDFSchema(e.getValue().getURI(), e.getValue()));
		}
		return copy;
	}

	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		// in general there is no metadata
		return new SDFMetaAttributeList();
	}

}
