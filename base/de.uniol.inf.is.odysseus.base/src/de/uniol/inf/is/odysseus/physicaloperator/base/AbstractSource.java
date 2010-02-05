package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSource<T> extends AbstractMonitoringDataProvider
		implements ISource<T> {
	//private static final Logger logger = LoggerFactory.getLogger(ISource.class);
	final protected List<PhysicalSubscription<ISink<? super T>>> subscriptions = new ArrayList<PhysicalSubscription<ISink<? super T>>>();;
	final protected Map<POEventType, ArrayList<POEventListener>> eventListener = new HashMap<POEventType, ArrayList<POEventListener>>();
	final protected ArrayList<POEventListener> genericEventListener = new ArrayList<POEventListener>();
	private boolean hasSingleConsumer = false;
	private AtomicBoolean open = new AtomicBoolean(false);
	private String name = null;
	private SDFAttributeList outputSchema;

	// Events
	final private POEvent doneEvent = new POEvent(this, POEventType.Done);
	final private POEvent openInitEvent = new POEvent(this,
			POEventType.OpenInit);
	final private POEvent openDoneEvent = new POEvent(this,
			POEventType.OpenDone);
	final private POEvent pushInitEvent = new POEvent(this,
			POEventType.PushInit);
	final private POEvent pushDoneEvent = new POEvent(this,
			POEventType.PushDone);

	protected List<IOperatorOwner> owners = new Vector<IOperatorOwner>();

	public AbstractSource(){};
			
	public AbstractSource(AbstractSource<T> source) {
		this.outputSchema = source.outputSchema;
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

	public boolean isOpen() {
		return open.get();
	}

	public void close() {
		open.set(false);
	};

	@Override
	public synchronized void open() throws OpenFailedException {
		if (!isOpen()) {
			fire(openInitEvent);
			process_open();
			fire(openDoneEvent);
			open.set(true);
		}
	}

	protected abstract void process_open() throws OpenFailedException;

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	};

	@Override
	public void transfer(T object, int sourcePort) {
		fire(this.pushInitEvent);
		process_transfer(object);
		// DEBUG
		// try{
		// if(((MVRelationalTuple)object).getAttribute(3).equals(19.3906)){
		// try{
		// throw new Exception("Doppelt.");
		// }catch(Exception e){
		// e.printStackTrace();
		// }
		// }
		// }catch(Exception e){
		// // falls es das attribut nicht gibt, tue nichts.
		// }
		fire(this.pushDoneEvent);
	}
	
	public void transfer(T object) {
		transfer(object,0);
	};

	@Override
	public void transfer(Collection<T> object, int sourcePort) {
		// TODO events erzeugen und verschicken, bzw. ein spezielles
		// transferbatchevent
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				if (sink.getSourcePort() == sourcePort){
					sink.getTarget().process(object, sink.getSinkPort(), isTransferExclusive());
				}
			}
		}
	}
	
	@Override
	public void transfer(Collection<T> object) {
		transfer(object,0);
	}

	/**
	 * states if the next Operator can change the transfer object oder has to
	 * make a copy
	 * 
	 * @return
	 */
	protected boolean isTransferExclusive() {
		return hasSingleConsumer;
	}

	final protected void fire(POEvent event) {
		ArrayList<POEventListener> list = this.eventListener.get(event
				.getPOEventType());
		if (list != null) {
			synchronized (list) {
				for (POEventListener listener : list) {
					listener.poEventOccured(event);
				}
			}
		}
		synchronized (this.eventListener) {
			for (POEventListener listener : this.genericEventListener) {
				listener.poEventOccured(event);
			}
		}
	}

	protected void process_transfer(T object) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				sink.getTarget().process(object, sink.getSinkPort(), !this.hasSingleConsumer);
			}
		}
	}
	
	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkPort, int sourcePort, SDFAttributeList schema) {
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(
				sink, sinkPort, sourcePort, schema);
		synchronized (this.subscriptions) {
			if (!this.subscriptions.contains(sub)) {
				this.subscriptions.add(sub);
				sink.subscribeToSource(this, sinkPort, sourcePort, schema);
				this.hasSingleConsumer = this.subscriptions.size() == 1;
			}
		}
	}

	@Override
	final public void unsubscribeSink(ISink<? super T> sink, int sinkPort, int sourcePort, SDFAttributeList schema) {
		unsubscribeSink(new PhysicalSubscription<ISink<? super T>>(	sink, sinkPort, sourcePort, schema));
	}
	
	@Override
	public void unsubscribeSink(PhysicalSubscription<ISink<? super T>> subscription) {
		synchronized (this.subscriptions) {
			if (this.subscriptions.remove(subscription)) {
				subscription.getTarget().unsubscribeFromSource(this,subscription.getSinkPort(), subscription.getSourcePort(), subscription.getSchema());
			}
			this.hasSingleConsumer = this.subscriptions.size() == 1;
		}	
	}
	

	@Override
	final public List<PhysicalSubscription<ISink<? super T>>> getSubscriptions() {
			return Collections.unmodifiableList(this.subscriptions);
	}

	final protected void propagateDone() {
		fire(this.doneEvent);
		this.process_done();
		synchronized (subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sub : subscriptions) {
				sub.getTarget().done(sub.getSinkPort());
			}
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void subscribe(POEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<POEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<POEventListener>();
				this.eventListener.put(type, curEventListener);
			}
			curEventListener.add(listener);
		}
		if (listener instanceof IMonitoringData) {
			IMonitoringData<?> mItem = (IMonitoringData<?>) listener;
			// nur dann registrieren, falls es noch nicht registriert ist
			// es kann z.B. sein, dass ein MetadataItem f�r zwei Events
			// registriert wird, aber nat�rlich trotzdem nur einmal
			// als MetadataItem registriert sein muss
			try {
				this.addMonitoringData(mItem.getType(), mItem);
			} catch (IllegalArgumentException e) {
			}
		}
	}

	@Override
	public void unsubscribe(POEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<POEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(POEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(POEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}

	final protected boolean hasSingleConsumer() {
		return this.hasSingleConsumer;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<? extends ISink<?>> sub : this.subscriptions) {
				sub.getTarget().processPunctuation(punctuation);
			}
		}
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
		return outputSchema;
	}
	
	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		this.outputSchema = outputSchema;	
	}
	
	@Override
	public void addOwner(IOperatorOwner owner) {
		this.owners.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		this.owners.remove(owner);
		// synchronized (this.deactivateRequestControls) {
		// this.deactivateRequestControls.remove(owner);
		// }
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
	
	
	public AbstractSource<T> clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}

	
}
