package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import de.uniol.inf.is.odysseus.base.CloseFailedException;
import de.uniol.inf.is.odysseus.base.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringDataProvider;
import de.uniol.inf.is.odysseus.monitoring.IMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEvent;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.IPOEventListener;
import de.uniol.inf.is.odysseus.physicaloperator.base.event.POEventType;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi, Tobias Witt
 */
public abstract class AbstractSource<T> extends AbstractMonitoringDataProvider
		implements ISource<T> {
	//private static final Logger logger = LoggerFactory.getLogger(ISource.class);
	final protected List<PhysicalSubscription<ISink<? super T>>> subscriptions = new ArrayList<PhysicalSubscription<ISink<? super T>>>();;
	final protected Map<POEventType, ArrayList<IPOEventListener>> eventListener = new HashMap<POEventType, ArrayList<IPOEventListener>>();
	final protected ArrayList<IPOEventListener> genericEventListener = new ArrayList<IPOEventListener>();
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
		try {
			this.process_close();
		} catch (CloseFailedException e) {
			//TODO @MG: close failed macht wenig sinn oder? man kann nicht reagieren
			e.printStackTrace();
		}
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
	
	protected void process_close() throws CloseFailedException{}

	/**
	 * If a source needs to propagate done to other elements in the operator,
	 * overwrite this method. It is called when this operator is done!
	 */
	protected void process_done() {
	};

	@Override
	public void transfer(T object, int sourceOutPort) {
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
	public void transfer(Collection<T> object, int sourceOutPort) {
		// TODO events erzeugen und verschicken, bzw. ein spezielles
		// transferbatchevent
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				if (sink.getSourceOutPort() == sourceOutPort){
					sink.getTarget().process(object, sink.getSinkInPort(), isTransferExclusive());
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
		return hasSingleConsumer();
	}
	
	protected boolean hasSingleConsumer(){
		return this.subscriptions.size() == 1;
	}

	final protected void fire(POEvent event) {
		ArrayList<IPOEventListener> list = this.eventListener.get(event
				.getPOEventType());
		if (list != null) {
			synchronized (list) {
				for (IPOEventListener listener : list) {
					listener.poEventOccured(event);
				}
			}
		}
		synchronized (this.eventListener) {
			for (IPOEventListener listener : this.genericEventListener) {
				listener.poEventOccured(event);
			}
		}
	}

	protected void process_transfer(T object) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sink : this.subscriptions) {
				//if (sink.getTarget().isActive() ?? hasOwner??){
				if (sink.isEnabled()){
					sink.getTarget().process(object, sink.getSinkInPort(), !this.hasSingleConsumer());
				}
				//}
			}
		}
	}
	
	@Override
	final public void subscribeSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		PhysicalSubscription<ISink<? super T>> sub = new PhysicalSubscription<ISink<? super T>>(
				sink, sinkInPort, sourceOutPort, schema);
		synchronized (this.subscriptions) {
			if (!this.subscriptions.contains(sub)) {
				this.subscriptions.add(sub);
				sink.subscribeToSource(this, sinkInPort, sourceOutPort, schema);
			}
		}
	}

	@Override
	final public void unsubscribeSink(ISink<? super T> sink, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		unsubscribeSink(new PhysicalSubscription<ISink<? super T>>(	sink, sinkInPort, sourceOutPort, schema));
	}
	
	@Override
	public void unsubscribeSink(PhysicalSubscription<ISink<? super T>> subscription) {
		synchronized (this.subscriptions) {
//			System.out.print(subscriptions+" remove "+subscription);
			boolean subContained = this.subscriptions.remove(subscription);
//			System.out.println("--> "+subContained);
			if (subContained) {
				subscription.getTarget().unsubscribeFromSource(this,subscription.getSinkInPort(), subscription.getSourceOutPort(), subscription.getSchema());
			}
		}	
	}
	
	@Override
	public void atomicReplaceSink(List<PhysicalSubscription<ISink<? super T>>> remove, ISink<? super T> sink,
			int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<ISink<? super T>> sub : remove) {
				unsubscribeSink(sub);
			}
			subscribeSink(sink, sinkInPort, sourceOutPort, schema);
		}
	}
	
	@Override
	public void atomicReplaceSink(PhysicalSubscription<ISink<? super T>> remove, List<ISink<? super T>> sinks,
			int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		synchronized (this.subscriptions) {
			unsubscribeSink(remove);
			for (ISink<? super T> sink : sinks) {
				subscribeSink(sink, sinkInPort, sourceOutPort, schema);
			}
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
				sub.getTarget().done(sub.getSinkInPort());
			}
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender and
	 * the same event type
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribe(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener,
	 *      de.uniol.inf.is.odysseus.monitor.queryexecution.event.POEventType)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void subscribe(IPOEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IPOEventListener> curEventListener = this.eventListener
					.get(type);
			if (curEventListener == null) {
				curEventListener = new ArrayList<IPOEventListener>();
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
			
			// TODO: Die Loesung mit der Exception war nicht schoen ...
			// Jetzt wird es einfach ignoriert
			//try {
				this.addMonitoringData(mItem.getType(), mItem);
//			} catch (IllegalArgumentException e) {
//			}
		}
	}

	@Override
	public void unsubscribe(IPOEventListener listener, POEventType type) {
		synchronized (this.eventListener) {
			ArrayList<IPOEventListener> curEventListener = this.eventListener
					.get(type);
			curEventListener.remove(listener);
		}
	}

	/**
	 * One listener can have multiple subscriptions to the same event sender
	 * 
	 * @see de.uniol.inf.is.odysseus.queryexecution.po.base.operators.IPhysicalOperator#subscribeToAll(de.uniol.inf.is.odysseus.IPOEventListener.queryexecution.event.POEventListener)
	 */
	@Override
	public void subscribeToAll(IPOEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.add(listener);
		}
	}

	@Override
	public void unSubscribeFromAll(IPOEventListener listener) {
		synchronized (this.genericEventListener) {
			this.genericEventListener.remove(listener);
		}
	}


	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + this.hashCode() + ")";
	}

	@Override
	public void sendPunctuation(PointInTime punctuation) {
		synchronized (this.subscriptions) {
			for (PhysicalSubscription<? extends ISink<?>> sub : this.subscriptions) {
				sub.getTarget().processPunctuation(punctuation, sub.getSinkInPort());
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
		// remove all occurrences
		while (this.owners.remove(owner)) {
		}
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
