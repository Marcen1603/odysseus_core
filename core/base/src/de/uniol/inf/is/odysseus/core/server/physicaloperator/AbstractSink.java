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
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.core.event.IEvent;
import de.uniol.inf.is.odysseus.core.event.IEventListener;
import de.uniol.inf.is.odysseus.core.event.IEventType;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.PhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.core.physicaloperator.event.POPortEvent;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.planmanagement.OperatorOwnerComparator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.event.EventHandler;
import de.uniol.inf.is.odysseus.core.server.monitoring.AbstractMonitoringDataProvider;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractSink<T extends IStreamObject<?>> extends AbstractMonitoringDataProvider
		implements ISink<T> {

	final private List<PhysicalSubscription<ISource<? extends T>>> subscribedToSource = new CopyOnWriteArrayList<PhysicalSubscription<ISource<? extends T>>>();

	protected int noInputPorts = -1;

	private String name;
	private Map<Integer, SDFSchema> outputSchema = new TreeMap<Integer, SDFSchema>();

	final transient protected List<IOperatorOwner> owners = new IdentityArrayList<IOperatorOwner>();

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
		this.outputSchema = createCleanClone(other.outputSchema);
		owners.addAll(other.owners);
		allInputsDone = false;
	}

	// "delegatable this", used for the delegate sink
	protected ISink<T> getInstance() {
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

	protected void open(List<PhysicalSubscription<ISink<?>>> callPath)
			throws OpenFailedException {
		// getLogger().debug("open() " + this);
		// The operator can already be initialized from former calls
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
		}
		// In every case, the sink is now open (no need to check, its cheaper to
		// always set this value to true
		// Hint: The operator can be opened by another method (c.f. AbstractPipe)
		this.sinkOpen.set(true);
		
		// Call open on all registered sources0
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			// Check if callPath contains this call already to avoid cycles
			if (!containsSubscription(callPath, getInstance(),
					sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new PhysicalSubscription<ISink<?>>(getInstance(),
						sub.getSinkInPort(), sub.getSourceOutPort(), null));
				sub.getTarget().open(getInstance(), sub.getSourceOutPort(),
						sub.getSinkInPort(), callPath);
			}
		}
	}

	@SuppressWarnings("static-method")
	private boolean containsSubscription(
			List<PhysicalSubscription<ISink<?>>> callPath,
			ISink<? super T> sink, int sourcePort, int sinkPort) {
		for (PhysicalSubscription<ISink<?>> sub : callPath) {
			if (sub.getTarget() == sink && sub.getSinkInPort() == sinkPort
					&& sub.getSourceOutPort() == sourcePort) {
				// getLogger().debug(
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

	// ------------------------------------------------------------------------
	// PROCESS
	// ------------------------------------------------------------------------

	@Override
	final public void process(T object, int port) {
		fire(processInitEvent[port]);
		process_next(object, port);
		fire(processDoneEvent[port]);
	}

	@Override
	public void process(Collection<? extends T> object, int port) {
		for (T cur : object) {
			process(cur, port);
		}
	}

	protected abstract void process_next(T object, int port);

	@Override
	public abstract void processPunctuation(PointInTime timestamp, int port);

	@Override
	public void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
	}

	// ------------------------------------------------------------------------
	// CLOSE and DONE
	// ------------------------------------------------------------------------

	@Override
	public void close() {
		close(new ArrayList<PhysicalSubscription<ISink<?>>>());
	}

	public void close(List<PhysicalSubscription<ISink<?>>> callPath) {
		if (this.sinkOpen.get()) {
			try {		
				callCloseOnChildren(callPath);
				process_close();
				stopMonitoring();				
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				this.sinkOpen.set(false);
			}
		}
	}	

	protected void callCloseOnChildren(
			List<PhysicalSubscription<ISink<?>>> callPath) {
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			if (!containsSubscription(callPath, getInstance(),
					sub.getSourceOutPort(), sub.getSinkInPort())) {
				callPath.add(new PhysicalSubscription<ISink<?>>(getInstance(),
						sub.getSinkInPort(), sub.getSourceOutPort(), null));
				sub.getTarget().close(getInstance(), sub.getSourceOutPort(),
						sub.getSinkInPort(), callPath);
			}
		}
	}
	
	protected void process_close() {
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
		if (!this.owners.contains(owner)) {
			this.owners.add(owner);
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());

	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		while (this.owners.remove(owner)) {
			// Remove all owners
		}
		Collections.sort(owners, OperatorOwnerComparator.getInstance());
	}

	@Override
	public void removeAllOwners() {
		this.owners.clear();
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

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owner
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	@Override
	public String getOwnerIDs() {
		StringBuffer result = new StringBuffer();
		for (IOperatorOwner iOperatorOwner : owners) {
			if (result.length() > 0) {
				result.append(", ");
			}
			result.append(iOperatorOwner.getID());
		}
		return result.toString();
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	@Override
	public void subscribeToSource(ISource<? extends T> source, int sinkInPort,
			int sourceOutPort, SDFSchema schema) {

		if (sinkInPort == -1) {
			sinkInPort = getNextFreeSinkInPort();
		}

		if (sinkInPort >= this.noInputPorts) {
			setInputPortCount(sinkInPort + 1);
		}

		PhysicalSubscription<ISource<? extends T>> sub = new PhysicalSubscription<ISource<? extends T>>(
				source, sinkInPort, sourceOutPort, schema);
		if (!this.subscribedToSource.contains(sub)) {
			if (!sinkInPortFree(sinkInPort)) {
				throw new IllegalArgumentException("SinkInPort " + sinkInPort
						+ " already bound ");
			}
			// getLogger().debug(
			// this.getInstance() + " Subscribe To Source " + source
			// + " to " + sinkInPort + " from " + sourceOutPort);
			this.subscribedToSource.add(sub);
			source.subscribeSink(getInstance(), sinkInPort, sourceOutPort,
					schema);
		}
	}

	private int getNextFreeSinkInPort() {
		int sinkInPort = -1;
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() > sinkInPort) {
				sinkInPort = sub.getSinkInPort();
			}
		}
		// und erhöhe um eins
		sinkInPort++;
		return sinkInPort;
	}

	private boolean sinkInPortFree(int sinkInPort) {
		for (PhysicalSubscription<ISource<? extends T>> sub : this.subscribedToSource) {
			if (sub.getSinkInPort() == sinkInPort) {
				getLogger()
						.error("SinkInPort " + sinkInPort
								+ " already bound to " + sub);
				return false;
			}
		}
		return true;
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
			int sinkInPort, int sourceOutPort, SDFSchema schema) {
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

	@Override
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

	private static Map<Integer, SDFSchema> createCleanClone(
			Map<Integer, SDFSchema> old) {
		Map<Integer, SDFSchema> copy = new HashMap<Integer, SDFSchema>();
		for (Entry<Integer, SDFSchema> e : old.entrySet()) {
			copy.put(e.getKey(), e.getValue().clone());
		}
		return copy;
	}
}
