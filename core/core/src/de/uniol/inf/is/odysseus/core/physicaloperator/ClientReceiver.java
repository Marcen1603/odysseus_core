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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionHandler;
import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.datahandler.IInputDataHandler;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.core.monitoring.IPeriodicalMonitoringData;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

/**
 * ReceiverPO without AbstractSource but with ISource
 * 
 * @author Merlin Wasmann
 */
public class ClientReceiver<R, W> implements ISource<W>,
		IAccessConnectionListener<R>, ITransferHandler<W>, IPhysicalOperator {

	final public int ERRORPORT = Integer.MAX_VALUE;

	protected IObjectHandler<W> objectHandler;
	private boolean opened;

	final IAccessConnectionHandler<R> accessHandler;
	final private IInputDataHandler<R, W> inputDataHandler;

	private AtomicBoolean open = new AtomicBoolean(false);

	private String name = null;
	private Map<Integer, SDFSchema> outputSchema = new HashMap<Integer, SDFSchema>();

	protected List<IOperatorOwner> owners = new IdentityArrayList<IOperatorOwner>();

	private AtomicBoolean blocked = new AtomicBoolean(false);

	final private List<PhysicalSubscription<ISink<? super W>>> sinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super W>>>();
	// Only active subscription are served on transfer
	final private List<PhysicalSubscription<ISink<? super W>>> activeSinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super W>>>();
	
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

	public ClientReceiver(IObjectHandler<W> objectHandler,
			IInputDataHandler<R, W> inputDataHandler,
			IAccessConnectionHandler<R> accessHandler) {
		this.objectHandler = objectHandler;
		this.inputDataHandler = inputDataHandler;
		this.accessHandler = accessHandler;
		this.name = "ClientReceiver " + accessHandler;
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public ClientReceiver(ClientReceiver<R, W> other) {
		objectHandler = (IObjectHandler<W>) other.objectHandler.clone();
		inputDataHandler = other.inputDataHandler.clone();
		accessHandler = (IAccessConnectionHandler<R>) other.clone();
		opened = other.opened;
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
			return this.getClass().getSimpleName() + "(" + this.hashCode()
					+ ")";
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
	
	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	@Override
	public void addOwner(IOperatorOwner owner) {
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		while (this.owners.remove(owner))
			;
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
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

	@Override
	public String getOwnerIDs() {
		StringBuffer res = new StringBuffer();
		for (IOperatorOwner owner : owners) {
			if (res.length() > 0) {
				res.append(", ");
			}
			res.append(owner.getID());
		}
		return res.toString();
	}

	// ------------------------------------------------------------------------
	// MONITORING
	// ------------------------------------------------------------------------
	
	@Override
	public Collection<String> getProvidedMonitoringData() {
		return null;
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
	public void createAndAddMonitoringData(IPeriodicalMonitoringData item,
			long period) {
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
		return null;
	}


	// ------------------------------------------------------------------------
	// TRANSFER
	// ------------------------------------------------------------------------
	
	@Override
	public void transfer(Collection<W> object, int sourceOutPort) {
		for (PhysicalSubscription<ISink<? super W>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				sink.getTarget().process(object, sink.getSinkInPort());
			}
		}
	}

	@Override
	public void transfer(Collection<W> object) {
		transfer(object, 0);
	}

	@Override
	public void transfer(W object, int sourceOutPort) {
		for (PhysicalSubscription<ISink<? super W>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				try {
					sink.getTarget().process(object, sink.getSinkInPort());
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
	
	@Override
	public void transferSecurityPunctuation(ISecurityPunctuation sp) {
		transferSecurityPunctuation(sp, 0);
	}

	public void transferSecurityPunctuation(ISecurityPunctuation sp, int sourceOutPort) {
		for (PhysicalSubscription<ISink<? super W>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				try {
					sink.getTarget().processSecurityPunctuation(sp, sink.getSinkInPort());
				} catch (Exception e) {
					// Send object that could not be processed to the error port
					e.printStackTrace();
					transferSecurityPunctuation(sp, ERRORPORT);
				}
			}
		}
	}

	// ------------------------------------------------------------------------
	// Punctuations
	// ------------------------------------------------------------------------

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		for (PhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
			sub.getTarget()
					.processPunctuation(punctuation, sub.getSinkInPort());
		}
	}

	@Override
	public void sendPunctuation(PointInTime punctuation, int outPort) {
		for (PhysicalSubscription<? extends ISink<?>> sub : this.activeSinkSubscriptions) {
			if (sub.getSourceOutPort() == outPort) {
				sub.getTarget().processPunctuation(punctuation,
						sub.getSinkInPort());
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
	public void subscribeSink(ISink<? super W> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		PhysicalSubscription<ISink<? super W>> sub = new PhysicalSubscription<ISink<? super W>>(
				sink, sinkInPort, sourceOutPort, schema);
		if (!this.sinkSubscriptions.contains(sub)) {
			// getLogger().debug(
			// this + " Subscribe Sink " + sink + " to " + sinkInPort
			// + " from " + sourceOutPort);
			this.sinkSubscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void unsubscribeSink(ISink<? super W> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(new PhysicalSubscription<ISink<? super W>>(sink,
				sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void unsubscribeSink(
			PhysicalSubscription<ISink<? super W>> subscription) {
		getLogger().debug("Unsubscribe from Sink " + subscription.getTarget());
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeFromSource(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	@Override
	public Collection<PhysicalSubscription<ISink<? super W>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public void connectSink(ISink<? super W> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super W>> sub = new PhysicalSubscription<ISink<? super W>>(
				sink, sinkInPort, sourceOutPort, schema);
		this.activeSinkSubscriptions.add(sub);
	}

	@Override
	public void disconnectSink(ISink<? super W> sink, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super W>> sub = new PhysicalSubscription<ISink<? super W>>(
				sink, sinkInPort, sourceOutPort, schema);
		this.activeSinkSubscriptions.remove(sub);
	}

	@Override
	public void atomicReplaceSink(
			List<PhysicalSubscription<ISink<? super W>>> remove,
			ISink<? super W> sink, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		for (PhysicalSubscription<ISink<? super W>> sub : remove) {
			unsubscribeSink(sub);
		}
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
	}

	@Override
	public void atomicReplaceSink(
			PhysicalSubscription<ISink<? super W>> remove,
			List<ISink<? super W>> sinks, int sinkInPort, int sourceOutPort,
			SDFSchema schema) {
		// synchronized (this.sinkSubscriptions) {
		unsubscribeSink(remove);
		for (ISink<? super W> sink : sinks) {
			subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		}
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")"
				+ (blocked.get() ? "b" : "");
	}
	

	private PhysicalSubscription<ISink<? super W>> findSinkInSubscription(
			IPhysicalOperator o, int sourcePort, int sinkPort) {
		for (PhysicalSubscription<ISink<? super W>> sub : this.sinkSubscriptions) {
			if (sub.getTarget() == o && sub.getSourceOutPort() == sourcePort
					&& sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}
	
	@Override
	public void process(R buffer) throws ClassNotFoundException {
		inputDataHandler.process(buffer, objectHandler, accessHandler, this);
	}

	// ------------------------------------------------------------------------
	// DONE
	// ------------------------------------------------------------------------
	
	@Override
	public void done() {
		propagateDone();
	}

	final protected void propagateDone() {
		// Could be that the query is already closed. In this cases the done
		// event
		// does not of any interest any more
		if (isOpen()) {
			// fire(this.doneEvent);
			this.process_done();
			for (PhysicalSubscription<ISink<? super W>> sub : sinkSubscriptions) {
				if (!sub.isDone()) {
					sub.getTarget().done(sub.getSinkInPort());
				}
			}
		}
	}

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	}


	@Override
	public boolean isOpened() {
		return this.isOpen();
	}

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------
	
	@Override
	public boolean isOpen() {
		return open.get();
	}

	@Override
	public void open(ISink<? super W> caller, int sourcePort, int sinkPort,
			List<PhysicalSubscription<ISink<?>>> callPath)
			throws OpenFailedException {
		// Hint: ignore callPath on sources because the source does not call any
		// subscription

		// o can be null, if operator is top operator
		// otherwise top operator cannot be opened
		if (caller != null) {
			PhysicalSubscription<ISink<? super W>> sub = findSinkInSubscription(
					caller, sourcePort, sinkPort);
			if (sub == null) {
				throw new OpenFailedException(
						"Open called from an unsubscribed sink " + caller);
			}
			// Handle multiple open calls
			if (!activeSinkSubscriptions.contains(sub)) {
				this.activeSinkSubscriptions.add(sub);
			}
			sub.incOpenCalls();
		}
		if (!isOpen()) {
			// fire(openInitEvent);
			process_open();
			// fire(openDoneEvent);
			open.set(true);
		}
	}
	
	@Override
	public void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				objectHandler.clear();
				inputDataHandler.init();
				accessHandler.open(this);
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
	public void close(ISink<? super W> caller, int sourcePort, int sinkPort,
			List<PhysicalSubscription<ISink<?>>> callPath) {
		PhysicalSubscription<ISink<? super W>> sub = findSinkInSubscription(
				caller, sourcePort, sinkPort);
		if (sub == null) {
			throw new RuntimeException(
					"Close called from an unsubscribed sink ");
		}
		sub.decOpenCalls();
		if (sub.getOpenCalls() == 0) {
			this.activeSinkSubscriptions.remove(sub);
			if (activeSinkSubscriptions.size() == 0) {
				getLogger().debug("Closing " + toString());
				// fire(this.closeInitEvent);
				this.process_close();
				open.set(false);
				//stopMonitoring();
				// fire(this.closeDoneEvent);
			}
		}
	}
	
	@Override
	public void process_close() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false;
				accessHandler.close(this);
				inputDataHandler.done();
				objectHandler.clear();
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
		if (!(ipo instanceof ISource)) {
			return false;
		}
		return process_isSemanticallyEqual(ipo);
	}
	
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ClientReceiver)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		ClientReceiver cr = (ClientReceiver) ipo;
		if (this.objectHandler.equals(cr.objectHandler)
				&& this.accessHandler.equals(cr.accessHandler)) {
			return true;
		}
		return false;
	}
}
