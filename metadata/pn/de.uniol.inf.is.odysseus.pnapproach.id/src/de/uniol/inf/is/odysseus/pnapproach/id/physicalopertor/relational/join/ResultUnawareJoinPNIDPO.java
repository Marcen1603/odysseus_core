package de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.AbstractBinaryPNPipe;
import de.uniol.inf.is.odysseus.pnapproach.base.sweeparea.PNAwareSweepArea;
import de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join.helper.IPNMetadataMergeFunction;
import de.uniol.inf.is.odysseus.pnapproach.id.sweeparea.DefaultPNIDSweepArea;

/**
 * Dieser JoinPNPO merkt sich nicht, welche Ergebnisse werzeugt wurden.Ein negatives Element erhält immer eine
 * Wildcard-ID und wird in den Ausgabedatenstrom geschrieben.
 * 
 * @author abolles
 *
 * @param <M> Metadatentyp
 * @param <T> Datentyp
 */
public class ResultUnawareJoinPNIDPO<M extends IPosNeg, T extends IMetaAttributeContainer<M>> extends
		AbstractBinaryPNPipe<T, T> {
	private static final Logger logger = LoggerFactory.getLogger(ResultAwareJoinPNIDPO.class);
	protected final DefaultPNIDSweepArea<T>[] areas;
	/*
	 * These areas temporally store negative elements
	 * until these elements are allowed to delete their
	 * positive counterparts.
	 */
	protected final DefaultPNIDSweepArea<T>[] nareas;
	protected final PNAwareSweepArea<T>[] preareas;
	protected final IDataMergeFunction<T> dataMerge;
	protected final IPNMetadataMergeFunction<M> metadataMerge;
	protected ITransferFunction<T> transferFunction;
	protected PointInTime[] lastTs;
	protected boolean[] isDone;
	protected int lastPort;
	protected int lookahead;

	@SuppressWarnings("unchecked")
	public ResultUnawareJoinPNIDPO(IDataMergeFunction<T> dataMerge,
			IPNMetadataMergeFunction<M> metadataMerge,
			ITransferFunction<T> transferFunction,
			DefaultPNIDSweepArea<T>[] areas,
			DefaultPNIDSweepArea<T>[] nareas,
			int lookahead) {
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.areas = areas;
		this.nareas = nareas;
		
		this.lastTs = new PointInTime[2];
		this.lastTs[0] = new PointInTime(0);
		this.lastTs[1] = new PointInTime(0);
		
		this.preareas = new PNAwareSweepArea[2];
		this.preareas[0] = new PNAwareSweepArea<T>();
		this.preareas[1] = new PNAwareSweepArea<T>();
		
		this.isDone = new boolean[2];
		this.isDone[0] = false;
		this.isDone[1] = false;
		
		this.lookahead = lookahead;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(T object, int port) {
		if (isDone()) { // TODO bei den sources abmelden ?? MG: Warum?? propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
			
		Object[] nextElem = this.preStore(object, port);
		
		if(nextElem != null){
			object = (T)nextElem[0];
			port = (Integer)nextElem[1];
		
			this.process_next_internal(object, port);
		}
		
		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
	}
	
	/**
	 * Inserts the new element into the pre sweep area.
	 * @param object
	 * @param port
	 * @return The element with the minimal timestamp of both pre areas (index 0) and its port (index 1)
	 */
	private Object[] preStore(T object, int port){
		Object[] nextElem = new Object[2];
		this.preareas[port].insert(object);
		
		T left_min = this.preareas[0].peek();
		T right_min = this.preareas[1].peek();
		
		if(left_min != null && right_min != null){
			
			if(left_min.getMetadata().getTimestamp().before(right_min.getMetadata().getTimestamp())){
				nextElem[0] = this.preareas[0].poll();
				nextElem[1] = 0;
				this.lastPort = 0;
			}
			else if(right_min.getMetadata().getTimestamp().before(left_min.getMetadata().getTimestamp())){
				nextElem[0] = this.preareas[1].poll();
				nextElem[1] = 1;
				this.lastPort = 1;
			}else{
				// Die Zeitstempel sind gleich, also schreibe zuerst das negative heraus.
				if(left_min.getMetadata().getElementType() == ElementType.NEGATIVE){
					nextElem[0] = this.preareas[0].poll();
					nextElem[1] = 0;
					this.lastPort = 0;
				}
				else if(right_min.getMetadata().getElementType() == ElementType.NEGATIVE){
					nextElem[0] = this.preareas[1].poll();
					nextElem[1] = 1;
					this.lastPort = 1;
				}
				// beide elemente gleich und beide elemente positiv, also mache ein lookahead
				else{
					PointInTime peekTs = this.preareas[0].peek().getMetadata().getTimestamp();
					
					Iterator<T> iter_left = this.preareas[0].iterator();
					Iterator<T> iter_right = this.preareas[0].iterator();
					
					int counter = 0;
					int usePort = 0;
					T left = null;
					T right = null;
					while(counter < this.lookahead && (iter_right.hasNext() || iter_left.hasNext())){
						if(iter_right.hasNext() && iter_left.hasNext()){
							left = iter_left.next();
							right = iter_right.next();
							if(left.getMetadata().getTimestamp().after(peekTs) || right.getMetadata().getElementType() == ElementType.NEGATIVE){
								usePort = 1;
								break;
							}
							else if(right.getMetadata().getTimestamp().after(peekTs) || left.getMetadata().getElementType() == ElementType.NEGATIVE){
								usePort = 0;
								break;
							}
						}
						else if(!iter_left.hasNext() && iter_right.hasNext() && iter_right.next().getMetadata().getTimestamp().equals(peekTs)){
							usePort = 1;
							break;
						}
						else if(!iter_right.hasNext() && iter_left.hasNext() && iter_left.next().getMetadata().getTimestamp().equals(peekTs)){
							usePort = 0;
							break;
						}
						counter++;
					}
					nextElem[0] = this.preareas[usePort].poll();
					nextElem[1] = usePort;
					this.lastPort = usePort;
				}
			}
			return nextElem;
		}
		
		// falls der Port, auf dem etwas null ist, auch done ist, kann der andere rausgeschrieben werden
		else if(left_min == null && this.isDone[0] && right_min != null){
			nextElem[0] = this.preareas[1].poll();
			nextElem[1] = 1;
			return nextElem;
		}
		else if(right_min == null && this.isDone[1] && left_min != null){
			nextElem[0] = this.preareas[0].poll();
			nextElem[1] = 0;
			return nextElem;
		}
		
		return null;
	}
	
	private void process_next_internal(T object, int port){
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		Order otherOrder = Order.fromOrdinal(otherport);
		this.lastTs[port] = object.getMetadata().getTimestamp();
		
		// Als erstes müssen die gespeicherten negativen Elemente,
		// die nun ihre positiven Gegenstücke löschen dürfen
		// verarbeitet werden.
		synchronized(this.nareas){
			synchronized(this.areas){
				Iterator<T> negatives = this.nareas[otherport].extractElements(new PointInTime(object.getMetadata().getTimestamp().getMainPoint() + 1));
				while(negatives.hasNext()){
					T neg = negatives.next();
					areas[otherport].insert(neg, null);
					
					M newMData = this.metadataMerge.createNegativeResult(neg.getMetadata(), otherOrder);
					neg.setMetadata(newMData);
					this.transferFunction.transfer(neg);
				}
			}
		}
		
		if(object.getMetadata().getElementType() == ElementType.POSITIVE){
			
			Iterator<T> qualifies = null;
			synchronized (this.areas) {
				synchronized (this.areas[otherport]) {
					qualifies = areas[otherport].queryCopy(object, order);
					transferFunction.newElement(object, port);
				}
				synchronized (areas[port]) {
					areas[port].insert(object);
				}
			}
			
			while (qualifies.hasNext()) {
				T next = qualifies.next();
				T newElement = merge(object, next, order);
				transferFunction.transfer(newElement);
			}
		}
		
		else{
			if(lastTs[otherport].after(object.getMetadata().getTimestamp())){
				synchronized(this.nareas){
					synchronized(this.areas){
						areas[port].insert(object, null);
						
						M newMData = this.metadataMerge.createNegativeResult(object.getMetadata(), order);
						object.setMetadata(newMData);
						this.transferFunction.transfer(object);


					}
				}
			}
			else{
				synchronized(nareas[port]){
					this.nareas[port].insert(object);
				}
			}
		}
	}

	private T merge(T left, T right, Order order) {
		if (logger.isTraceEnabled()) {
			logger.trace("ResultAwareJoinPNIDPO (" + hashCode() + ") start merging: " + left
					+ " AND " + right);
		}
		T mergedData;
		M mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, right);
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
					right.getMetadata());
		} else {
			mergedData = dataMerge.merge(right, left);
			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(),
					left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		return mergedData;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
		}
		this.transferFunction.init(this);
	}
	
	@Override
	protected void process_done(int port){
		int otherport = port ^1;
		
		// leere die pre sweep areas
		if(this.isDone[otherport]){
			while(!this.preareas[port].isEmpty() && !this.preareas[otherport].isEmpty()){
				T left_min = this.preareas[0].peek();
				T right_min = this.preareas[1].peek();
					
				if(left_min.getMetadata().getTimestamp().before(right_min.getMetadata().getTimestamp())){
					this.process_next_internal(this.preareas[0].poll(), 0);
				}
				else{
					this.process_next_internal(this.preareas[1].poll(), 1);
				}
			}
			
			// leere noch die volle liste, falls vorhanden
			while(!this.preareas[port].isEmpty()){
				this.process_next_internal(this.preareas[port].poll(), 0);
			}
			while(!this.preareas[otherport].isEmpty()){
				this.process_next_internal(this.preareas[otherport].poll(), 0);
			}
		}
		// der andere port ist noch nicht fertig
		else{
			while(!this.preareas[port].isEmpty() && !this.preareas[otherport].isEmpty()){
				T left_min = this.preareas[0].peek();
				T right_min = this.preareas[1].peek();
					
				if(left_min.getMetadata().getTimestamp().before(right_min.getMetadata().getTimestamp())){
					this.process_next_internal(this.preareas[0].poll(), 0);
				}
				else{
					this.process_next_internal(this.preareas[1].poll(), 1);
				}
			}
			
			// falls die liste port, welcher ja done ist, leer ist, kann die andere auch geleert werden
			if(this.preareas[port].isEmpty()){
				while(!this.preareas[otherport].isEmpty()){
					this.process_next_internal(this.preareas[otherport].poll(), 0);
				}
			}
		}
		
		Order otherOrder = Order.fromOrdinal(otherport);
		synchronized(this.nareas){
			synchronized(this.areas){
				Iterator<T> negatives = this.nareas[otherport].iterator();
				while(negatives.hasNext()){
					T neg = negatives.next();
					areas[otherport].insert(neg, null);
					
					M newMData = this.metadataMerge.createNegativeResult(neg.getMetadata(), otherOrder);
					neg.setMetadata(newMData);
					this.transferFunction.transfer(neg);
				}
				this.nareas[otherport].clear();
			}
		}
//		System.out.println(this + " Join Process done port: " + port);
//		
//		System.out.println(this + " JoinPNPO done: area0: " + this.areas[0].size());
//		System.out.println(this + " JoinPNPO done: area1: " + this.areas[1].size());
//		
//		System.out.println(this + " JoinPNPO done: narea0: " + this.nareas[0].size());
//		System.out.println(this + " JoinPNPO done: narea1: " + this.nareas[1].size());
	}
	
	@Override
	protected void process_done() {
		// Die negativen müssen nicht rausgeschrieben werden
		// weil der Intervall-Ansatz das auch nicht macht. Alles
		// was dort noch zum Schluss in den SweepAreas hängt,
		// bleibt dort auch drin.
		
		System.out.println(this + " JoinPNPO done: area0: " + this.areas[0].size());
		System.out.println(this + " JoinPNPO done: area1: " + this.areas[1].size());
		
		System.out.println(this + " JoinPNPO done: narea0: " + this.nareas[0].size());
		System.out.println(this + " JoinPNPO done: narea1: " + this.nareas[1].size());
		
		transferFunction.done();
		areas[0].clear();
		areas[1].clear();
	 }
	
	@Override
	protected boolean isDone() { 
		if (getSubscribedToSource(0).isDone()) {
			return getSubscribedToSource(1).isDone() || (areas[0].isEmpty() && nareas[0].isEmpty());
		} else {
			return getSubscribedToSource(0).isDone()  && areas[1].isEmpty() && nareas[1].isEmpty();
		}
	}

	/**
	 * Sorgt dafuer, dass das {@link MemoryUsage} MetadataItem an die SweepAreas
	 * des Operators gelangen kann, um die enthaltenen Elemente zaehlen zu
	 * koennen
	 * 
	 * @param factory
	 *            - {@link MemoryUsageMonitoringDataFactory}, die ein MetadataItem
	 *            erzeugt welches auf die SweepAreas zugreifen kann.
	 */
//	public void addMemoryUsageMetadataItem(MemoryUsageMonitoringDataFactory factory) {
//		IMonitoringData<Integer> item = factory.createPNJoinMemoryHandler(this, preareas, areas, nareas);
//		addMonitoringData(MemoryUsageMonitoringDataFactory.METADATA_TYPE, item);
//	}
	
	@Override
	public ResultUnawareJoinPNIDPO<M, T> clone() throws CloneNotSupportedException {
		// TODO: Implement clone()-Method
		throw new CloneNotSupportedException();
	}

}
