package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.awt.util.IdentityArrayList;
import de.uniol.inf.is.odysseus.event.EventHandler;
import de.uniol.inf.is.odysseus.event.IEvent;
import de.uniol.inf.is.odysseus.event.IEventHandler;
import de.uniol.inf.is.odysseus.event.IEventListener;
import de.uniol.inf.is.odysseus.event.IEventType;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.event.POEventType;
import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

/**
 * @author Jonas Jacobi, Tobias Witt, Marco Grawunder
 */
public abstract class AbstractSource<T> extends AbstractMonitoringDataProvider
		implements ISource<T>{

	final private List<PhysicalSubscription<ISink<? super T>>> sinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super T>>>();;
	// Only active subscription are served on transfer
	final private List<PhysicalSubscription<ISink<? super T>>> activeSinkSubscriptions = new CopyOnWriteArrayList<PhysicalSubscription<ISink<? super T>>>();;

	private AtomicBoolean open = new AtomicBoolean(false);
	private String name = null;
	private Map<Integer, SDFAttributeList> outputSchema = new HashMap<Integer, SDFAttributeList>();
	protected List<IOperatorOwner> owners = new IdentityArrayList<IOperatorOwner>();

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

	private IEventHandler eventHandler = new EventHandler();

	@Override
	public void subscribe(IEventListener listener, IEventType type) {
		eventHandler.subscribe(listener, type);
	}

	@Override
	public void unsubscribe(IEventListener listener, IEventType type) {
		eventHandler.unsubscribe(listener, type);
	}

	@Override
	public void subscribeToAll(IEventListener listener) {
		eventHandler.subscribeToAll(listener);
	}

	@Override
	public void unSubscribeFromAll(IEventListener listener) {
		eventHandler.unSubscribeFromAll(listener);
	}

	@Override
	public void fire(IEvent<?, ?> event) {
		eventHandler.fire(event);
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
	final private POEvent pushListInitEvent = new POEvent(this,
			POEventType.PushListInit);
	final private POEvent pushListDoneEvent = new POEvent(this,
			POEventType.PushListDone);
	final private POEvent closeInitEvent = new POEvent(this,
			POEventType.CloseInit);
	final private POEvent closeDoneEvent = new POEvent(this,
			POEventType.CloseDone);

	private AtomicBoolean blocked = new AtomicBoolean(false);

	POEvent blockedEvent = new POEvent(this, POEventType.Blocked);
	POEvent unblockedEvent = new POEvent(this, POEventType.Unblocked);

	// ------------------------------------------------------------------

	public AbstractSource() {
	};

	public AbstractSource(AbstractSource<T> source) {
		this.outputSchema = createCleanClone(source.outputSchema);
		this.blocked = new AtomicBoolean();
		this.blocked.set(source.blocked.get());
		this.name = source.name;
		this.owners = new Vector<IOperatorOwner>(source.owners);
	}

	@Override
	public boolean isSink() {
		return false;
	}

	@Override
	public boolean isSource() {
		return true;
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
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getOutputSchema(0);
	}

	@Override
	public SDFAttributeList getOutputSchema(int port) {
		return outputSchema.get(port);
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		setOutputSchema(outputSchema, 0);
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema, int port) {
		this.outputSchema.put(port, outputSchema);		
	}
	
	

	// ------------------------------------------------------------------------
	// OPEN
	// ------------------------------------------------------------------------

	@Override
	public boolean isOpen() {
		return open.get();
	}

	@Override
	public synchronized void open(ISink<? super T> caller, int sourcePort,
			int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath)
			throws OpenFailedException {

		// Hint: ignore callPath on sources because the source does not call any
		// subscription

		// o can be null, if operator is top operator
		// otherwise top operator cannot be opened
		if (caller != null) {
			PhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
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
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			open.set(true);
		}
	}

	protected abstract void process_open() throws OpenFailedException;

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
	// TRANSFER
	// ------------------------------------------------------------------------

	@Override
	public void transfer(T object, int sourceOutPort) {
		fire(this.pushInitEvent);
		for (PhysicalSubscription<ISink<? super T>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				sink.getTarget().process(object, sink.getSinkInPort(),
						isTransferExclusive());
			}
		}
		fire(this.pushDoneEvent);
	}

	@Override
	public void transfer(T object) {
		transfer(object, 0);
	};

	@Override
	public void transfer(Collection<T> object, int sourceOutPort) {
		fire(this.pushListInitEvent);
		for (PhysicalSubscription<ISink<? super T>> sink : this.activeSinkSubscriptions) {
			if (sink.getSourceOutPort() == sourceOutPort) {
				sink.getTarget().process(object, sink.getSinkInPort(),
						isTransferExclusive());
			}
		}
		fire(this.pushListDoneEvent);
	}

	@Override
	public void transfer(Collection<T> object) {
		transfer(object, 0);
	}

	/**
	 * states if the next Operator can change the transfer object oder has to
	 * make a copy
	 * 
	 * @return
	 */
	protected boolean isTransferExclusive() {
		return hasSingleConsumer();
	}

	// ------------------------------------------------------------------------
	// CLOSE
	// ------------------------------------------------------------------------

	@Override
	public void close(ISink<? super T> caller, int sourcePort, int sinkPort, List<PhysicalSubscription<ISink<?>>> callPath) {
		PhysicalSubscription<ISink<? super T>> sub = findSinkInSubscription(
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
				fire(this.closeInitEvent);
				this.process_close();
				open.set(false);
				stopMonitoring();
				fire(this.closeDoneEvent);
			}
		}

	};

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
	};

	final protected void propagateDone() {
		fire(this.doneEvent);
		this.process_done();
		for (PhysicalSubscription<ISink<? super T>> sub : sinkSubscriptions) {
			sub.getTarget().done(sub.getSinkInPort());
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
		synchronized (blocked) {
			this.blocked.set(true);
			getLogger().debug("Operator " + this.toString() + " blocked");
			fire(blockedEvent);
		}
	}

	@Override
	public void unblock() {
		synchronized (blocked) {
			this.blocked.set(false);
			getLogger().debug("Operator " + this.toString() + " unblocked");
			fire(unblockedEvent);
		}
	}

	// ------------------------------------------------------------------------
	// Subscription management
	// ------------------------------------------------------------------------

	private PhysicalSubscription<ISink<? super T>> findSinkInSubscription(
			IPhysicalOperator o, int sourcePort, int sinkPort) {
		for (PhysicalSubscription<ISink<? super T>> sub : this.sinkSubscriptions) {
			if (sub.getTarget() == o && sub.getSourceOutPort() == sourcePort
					&& sub.getSinkInPort() == sinkPort) {
				return sub;
			}
		}
		return null;
	}

	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		if (!this.sinkSubscriptions.contains(sub)) {
			getLogger().debug(
					this + " Subscribe Sink " + sink + " to " + sinkInPort
							+ " from " + sourceOutPort);
			this.sinkSubscriptions.add(sub);
			sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void connectSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		this.activeSinkSubscriptions.add(sub);
	}

	@Override
	final public void unsubscribeSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		unsubscribeSink(new PhysicalSubscription<ISink<? super T>>(sink,
				sinkInPort, sourceOutPort, schema));
	}

	@Override
	public void disconnectSink(ISink<? super T> sink, int sinkInPort,
			int sourceOutPort, SDFAttributeList schema) {
		unsubscribeSink(sink, sinkInPort, sourceOutPort, schema);
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		this.activeSinkSubscriptions.remove(sub);
	}

	@Override
	public void unsubscribeSink(
			PhysicalSubscription<ISink<? super T>> subscription) {
		getLogger().debug("Unsubscribe from Sink " + subscription.getTarget());
		boolean subContained = this.sinkSubscriptions.remove(subscription);
		if (subContained) {
			subscription.getTarget().unsubscribeFromSource(this,
					subscription.getSinkInPort(),
					subscription.getSourceOutPort(), subscription.getSchema());
		}
	}

	@Override
	public void atomicReplaceSink(
			List<PhysicalSubscription<ISink<? super T>>> remove,
			ISink<? super T> sink, int sinkInPort, int sourceOutPort,
			SDFAttributeList schema) {
		synchronized (this.sinkSubscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sub : remove) {
				unsubscribeSink(sub);
			}
			subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		}
	}

	@Override
	public void atomicReplaceSink(
			PhysicalSubscription<ISink<? super T>> remove,
			List<ISink<? super T>> sinks, int sinkInPort, int sourceOutPort,
			SDFAttributeList schema) {
		synchronized (this.sinkSubscriptions) {
			unsubscribeSink(remove);
			for (ISink<? super T> sink : sinks) {
				subscribeSink(sink, sinkInPort, sourceOutPort, schema);
			}
		}
	}

	@Override
	final public List<PhysicalSubscription<ISink<? super T>>> getSubscriptions() {
		return Collections.unmodifiableList(this.sinkSubscriptions);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")"
				+ (blocked.get() ? "b" : "");
	}

	// ------------------------------------------------------------------------
	// Owner Management
	// ------------------------------------------------------------------------

	@Override
	public void addOwner(IOperatorOwner owner) {
		if (!this.owners.contains(owner)){
			this.owners.add(owner);
		}
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		// remove all occurrences
		while (this.owners.remove(owner)) {
		}
		// synchronized (this.deactivateRequestControls) {
		// this.deactivateRequestControls.remove(owner);
		// }
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

	/**
	 * Returns a ","-separated string of the owner IDs.
	 * 
	 * @param owner
	 *            Owner which have IDs.
	 * @return ","-separated string of the owner IDs.
	 */
	@Override
	public String getOwnerIDs() {
		String result = "";
		for (IOperatorOwner iOperatorOwner : owners) {
			if (result != "") {
				result += ", ";
			}
			result += iOperatorOwner.getID();
		}
		return result;
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
		if(! (ipo instanceof ISource || ipo instanceof IPipe)) return false;
		return process_isSemanticallyEqual(ipo);
	}
	
	// TODO: Make abstract again and implement in Children
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
	
	
	private Map<Integer, SDFAttributeList> createCleanClone(Map<Integer, SDFAttributeList> old){
		Map<Integer, SDFAttributeList> copy = new HashMap<Integer, SDFAttributeList>();
		for(Entry<Integer, SDFAttributeList> e : old.entrySet()){
			copy.put(e.getKey(), new SDFAttributeList(e.getValue()));
		}
		return copy;
	}
	
	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		// in general there is no metadata
		return new SDFMetaAttributeList();
	}
}
