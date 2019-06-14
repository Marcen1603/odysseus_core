/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OwnerHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * ReceiverPO without AbstractSource but with ISource
 *
 * @author Merlin Wasmann, Marco Grawunder
 */
public class ClientReceiver<R, W extends IStreamObject<IMetaAttribute>>
		implements ISource<W>, IAccessConnectionListener<R>, ITransferHandler<W>, IPhysicalOperator {

	final public int ERRORPORT = Integer.MAX_VALUE;

	private IProtocolHandler<W> protocolHandler;
	private boolean opened;

	final private AtomicBoolean open = new AtomicBoolean(false);
	final private AtomicBoolean done = new AtomicBoolean(false);

	private String name = null;
	private Map<Integer, SDFSchema> outputSchema = new HashMap<Integer, SDFSchema>();

	final private Map<Integer, Integer> consumerCount = new HashMap<>();

	final private OwnerHandler ownerHandler;

	private AtomicBoolean blocked = new AtomicBoolean(false);

	final private List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> sinkSubscriptions = new CopyOnWriteArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>();
	// Only active subscription are served on transfer
	final private List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> activeSinkSubscriptions = new CopyOnWriteArrayList<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>>();

	private Map<String, String> infos = new TreeMap<String, String>();

	private boolean suppressPunctuations;

	final private Comparator<AbstractPhysicalSubscription<?, ?>> sortByCloneComparator = new Comparator<AbstractPhysicalSubscription<?, ?>>() {

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

	// --------------------------------------------------------------------
	// Logging
	// --------------------------------------------------------------------

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ClientReceiver.class);
		}
		return _logger;
	}

	// ------------------------------------------------------------------

	public ClientReceiver(IProtocolHandler<W> protocolHandler) {
		this.protocolHandler = protocolHandler;
		this.protocolHandler.setTransfer(this);
		this.name = "ClientReceiver " + protocolHandler;
		this.opened = false;
		this.ownerHandler = new OwnerHandler();
	}

	public ClientReceiver(ClientReceiver<R, W> other) {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public boolean isSource() {
		return true;
	}

	@Override
	public boolean isSink() {
		return false;
	}

	@Override
	public boolean isPipe() {
		return isSink() && isSource();
	}

	protected boolean hasSingleConsumer() {
		return this.sinkSubscriptions.size() == 1;
	}

	@Override
	public String getName() {
		if (name == null) {
			return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
		}
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public SDFSchema getOutputSchema(int port) {
		return outputSchema.get(port);
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

	@Override
	public List<ISession> getSessions() {
		return null;
	}

	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	// MONITORING
	// ------------------------------------------------------------------------

	@Override
	public Collection<String> getProvidedMonitoringData() {
		return null;
	}

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
	public boolean isOwnedByAll(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAll(owners);
	}

	@Override
	public int hashCode() {
		return ownerHandler.hashCode();
	}

	@Override
	public boolean isOwnedByAny(List<IOperatorOwner> owners) {
		return ownerHandler.isOwnedByAny(owners);
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
	public boolean equals(Object obj) {
		return ownerHandler.equals(obj);
	}

	@Override
	public boolean providesMonitoringData(String type) {
		return false;
	}

	@Override
	public <T> IMonitoringData<T> getMonitoringData(String type) {
		return null;
	}

	/**
	 * not implemented
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void createAndAddMonitoringData(IPeriodicalMonitoringData item, long period) {
	}

	/**
	 * not implemented
	 */
	@Override
	public void addMonitoringData(String type, IMonitoringData<?> item) {

	}

	/**
	 * not implemented
	 */
	@Override
	public void removeMonitoringData(String type) {

	}

	// ------------------------------------------------------------------
	// Eventhandling
	// ------------------------------------------------------------------

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		// TODO
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		// TODO Auto-generated method stub

	}

	/**
	 * not implemented
	 */
	@Override
	public void fire(IEvent<?, ?> event) {
		// This method is intentionally left blank
	}

	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		// TODO Auto-generated method stub
		return new SDFMetaAttributeList();
	}

	// ------------------------------------------------------------------------
	// TRANSFER
	// ------------------------------------------------------------------------

	// @Override
	// public void transfer(Collection<W> object, int sourceOutPort) {
	// for (PhysicalSubscription<ISink<? super W>> sink :
	// this.activeSinkSubscriptions) {
	// if (sink.getSourceOutPort() == sourceOutPort) {
	// sink.getTarget().process(object, sink.getSinkInPort());
	// }
	// }
	// }
	//
	// @Override
	// public void transfer(Collection<W> object) {
	// transfer(object, 0);
	// }

	@Override
	public void transfer(W object, int sourceOutPort) {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				try {
					sink.getSink().process(object, sink.getSinkInPort());
				} catch (Exception e) {
					// Send object that could not be processed to the error port
					e.printStackTrace();
					transfer(object, ERRORPORT);
				}
			}
		}
	}

	@Override
	public void transfer(W object) {
		transfer(object, 0);
	}

	// ------------------------------------------------------------------------
	// Punctuations
	// ------------------------------------------------------------------------

	@Override
	public void setSuppressPunctuations(boolean suppressPunctuations) {
		this.suppressPunctuations = suppressPunctuations;
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation) {
		if (!suppressPunctuations) {
			for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : this.activeSinkSubscriptions) {
				sub.getSink().processPunctuation(punctuation, sub.getSinkInPort());
			}
		}
	}

	@Override
	public void sendPunctuation(IPunctuation punctuation, int outPort) {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : this.activeSinkSubscriptions) {
			if (sub.getSourceOutPort() == outPort) {
				sub.getSink().processPunctuation(punctuation, sub.getSinkInPort());
			}
		}
	}

	// ------------------------------------------------------------------------
	// BLOCK
	// ------------------------------------------------------------------------

	@Override
	public void unblock() {
		this.blocked.set(false);
		getLogger().debug("Operator " + this.toString() + " unblocked");
		// fire(unblockedEvent);
	}

	@Override
	public void block() {
		this.blocked.set(true);
		getLogger().debug("Operator " + this.toString() + " blocked");
		// fire(blockedEvent);
	}

	@Override
	public boolean isBlocked() {
		return blocked.get();
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	@Override
	final public void subscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema,
			boolean asActive, int openCount) {
		// TODO: Make configurable
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		sub.setOpenCalls(openCount);
		subscribeSink(sub, asActive);
	}

	private void subscribeSink(AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub,
			boolean asActive) {
		if (!this.sinkSubscriptions.contains(sub)) {
			// getLogger().trace(
			// this + " Subscribe Sink " + sink + " to " + sinkInPort
			// + " from " + sourceOutPort);
			this.sinkSubscriptions.add(sub);
			sub.getSink().subscribeToSource((AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ?>) sub);
			if (asActive) {
				addActiveSubscription(sub);
			}
		}
	}

	@Override
	final public void subscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema, false, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	final public void subscribeSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		subscribeSink((AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>) subscription,
				false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unsubscribeSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		getLogger().debug("Unsubscribe from Sink " + subscription.getSink());
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getSink().unsubscribeFromSource((ISource<IStreamObject<?>>) this, subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	private void addActiveSubscription(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub) {
		// Handle multiple open calls{
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

	@Override
	public Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public void connectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		// All connected sinks needs a cloned version as the sinks are connected
		// without
		// running the query translation phase
		sub.setNeedsClone(true);
		sink.addOwner(this.getOwner());
		addActiveSubscription(sub);
	}

	@Override
	public void disconnectSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
		@SuppressWarnings("unchecked")
		AbstractPhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>> sub = new ControllablePhysicalSubscription<ISource<IStreamObject<?>>, ISink<IStreamObject<?>>>(
				(ISource<IStreamObject<?>>) this, sink, sinkInPort, sourceOutPort, schema);
		disconnectSink(sub);
	}

	@Override
	public void disconnectSink(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> subscription) {
		removeActiveSubscription(subscription);		
	}
	
	private void removeActiveSubscription(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub) {
		if (activeSinkSubscriptions.contains(sub)) {
			consumerCount.put(sub.getSourceOutPort(), consumerCount.get(sub.getSourceOutPort()) - 1);
			this.activeSinkSubscriptions.remove(sub);
		}
	}

	@Override
	public boolean isDone() {
		return done.get();
	}

	// @Override
	// public void atomicReplaceSink(
	// List<PhysicalSubscription<ISink<? super W>>> remove,
	// ISink<? super W> sink, int sinkInPort, int sourceOutPort,
	// SDFSchema schema) {
	// for (PhysicalSubscription<ISink<? super W>> sub : remove) {
	// unsubscribeSink(sub);
	// }
	// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	// }
	//
	// @Override
	// public void atomicReplaceSink(
	// PhysicalSubscription<ISink<? super W>> remove,
	// List<ISink<? super W>> sinks, int sinkInPort, int sourceOutPort,
	// SDFSchema schema) {
	// // synchronized (this.sinkSubscriptions) {
	// unsubscribeSink(remove);
	// for (ISink<? super W> sink : sinks) {
	// subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	// }
	// }

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")" + (blocked.get() ? "b" : "");
	}

	private AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> findSinkInSubscription(IPhysicalOperator o,
			int sourcePort, int sinkPort) {
		for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : this.sinkSubscriptions) {
			if (sub.getSink() == o && sub.getSourceOutPort() == sourcePort && sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}

	// ------------------------------------------------------------------------
	// DONE
	// ------------------------------------------------------------------------

	@Override
	public void done() {
		propagateDone();
	}

	final public void propagateDone() {
		// Could be that the query is already closed. In this cases the done
		// event
		// does not of any interest any more
		if (isOpen()) {
			// fire(this.doneEvent);
			this.process_done();
			for (AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub : sinkSubscriptions) {
				if (!sub.isDone()) {
					sub.getSink().done(sub.getSinkInPort());
				}
			}
			for (IOperatorOwner owner : getOwner()) {
				owner.done(this);
			}
		}
	}

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	}

	// @Override
	// public boolean isOpened() {
	// return this.isOpen();
	// }

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public boolean isOpen() {
		return open.get();
	}

	@Override
	synchronized public void open(IOperatorOwner owner) throws OpenFailedException {
		// do nothing
	}

	@Override
	public void open(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners)
			throws OpenFailedException {
		// Hint: ignore callPath on sources because the source does not call any
		// subscription

		// o can be null, if operator is top operator
		// otherwise top operator cannot be opened
		if (caller != null) {
			AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
					sinkPort);
			if (sub == null) {
				throw new OpenFailedException("Open called from an unsubscribed sink " + caller);
			}
			// Handle multiple open calls
			if (!activeSinkSubscriptions.contains(sub)) {
				this.activeSinkSubscriptions.add(sub);
			}
			sub.incOpenCalls();
		}
		if (!isOpen()) {
			// fire(openInitEvent);
			processOpen();
			// fire(openDoneEvent);
			open.set(true);
			done.set(false);
		}
	}

	// @Override
	public void processOpen() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				protocolHandler.open();
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	// ------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------

	@Override
	public void close(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sub = findSinkInSubscription(caller, sourcePort,
				sinkPort);
		getLogger().trace("CLOSE " + getName());
		if (sub == null) {
			throw new RuntimeException("Close called from an unsubscribed sink ");
		}
		sub.decOpenCalls();
		if (sub.getOpenCalls() == 0) {
			this.activeSinkSubscriptions.remove(sub);
			if (activeSinkSubscriptions.size() == 0) {
				getLogger().debug("Closing " + toString());
				// fire(this.closeInitEvent);
				this.processClose();
				open.set(false);
				// stopMonitoring();
				// fire(this.closeDoneEvent);
			}
		}
	}

	@Override
	public void close(IOperatorOwner id) {
		this.processClose();
	}

	// @Override
	public void processClose() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false; // Do not read any data anymore
				protocolHandler.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ClientReceiver<R, W> clone() {
		return new ClientReceiver<R, W>(this);
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo.isSource())) {
			return false;
		}
		return process_isSemanticallyEqual(ipo);
	}

	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ClientReceiver)) {
			return false;
		}

		return false;
	}

	@Override
	public void socketDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void socketException(Exception ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsubscribeFromAllSinks() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUniqueId(IOperatorOwner owner, Resource id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUniqueId(IOperatorOwner key) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<IOperatorOwner, Resource> getUniqueIds() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean hasInput() {
		return true;
	}

	@Override
	public void process(long callerId, R buffer) throws ClassNotFoundException {
		// TODO Auto-generated method stub
	}

	@Override
	public void setDebug(boolean debug) {

	}

	@Override
	public void suspend(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLogicalOperator(ILogicalOperator op) {
		// TODO Auto-generated method stub

	}

	@Override
	public ILogicalOperator getLogicalOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void start(IOperatorOwner id) throws StartFailedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start(ISink<? extends W> caller, int sourcePort, int sinkPort,
			List<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> callPath, List<IOperatorOwner> forOwners)
			throws StartFailedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void unsubscribeSink(ISink<IStreamObject<?>> sink, int sinkInPort, int sourceOutPort, SDFSchema schema) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>>> getConnectedSinks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID getUUID() {
		// TODO Auto-generated method stub
		return null;
	}

}
