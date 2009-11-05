package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.HashMap;
import java.util.Iterator;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;

/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von Daten- und
 * Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse implementierten
 * Operators entspricht jedoch der Logik eines JoinOperators im Intervallansatz.
 * @author abolles
 *
 * @param <M> Metadatentyp
 * @param <T> Datentyp
 */
public class LeftJoinTIPO<M extends ITimeInterval, T extends IMetaAttributeContainer<M>> extends
		JoinTIPO<M, T> {
//	private static final Logger logger = LoggerFactory.getLogger(LeftJoinTIPO.class);
	
	private HashMap<T, PointInTime> left_t_tilde;
	private LeftMergeFunction<T> dataMerge;
	private JoinTISweepArea<T>[] areas;

	public LeftJoinTIPO(LeftMergeFunction<T> dataMerge,
			IMetadataMergeFunction<M> metadataMerge,
			ITransferFunction<T> transferFunction, JoinTISweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		this.left_t_tilde = new HashMap<T, PointInTime>();
		this.areas = areas;
		this.dataMerge = dataMerge;
	}

	@Override
	protected void process_next(T object, int port) {
		if (isDone()) { // TODO bei den sources abmelden ?? MG: Warum?? propagateDone gemeint?
			return;
		}
		
		if(port == 0){
			this.doLeft(object, port);
		}
		else{
			this.doRight(object, port);
		}
		
		transferFunction.newElement(object, port);
		
		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
	}
	
	private void doLeft(T object, int port){
		int otherport = port ^ 1;
		Order leftRight = Order.fromOrdinal(port);
		
		// purgeElemente nimmt keine Ruecksicht auf <order>
		areas[otherport].purgeElements(object, leftRight);
		
		PointInTime t_tilde = object.getMetadata().getStart();
		
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, leftRight);
		
				while (qualifies.hasNext()) {
					T next = qualifies.next();
					T merged = merge(object, next, leftRight);
					
					if(merged.getMetadata().getStart().after(t_tilde)){
						@SuppressWarnings("unchecked")
						T leftUnbound = this.dataMerge.createLeftFilledUp((T)object.clone());
						
						leftUnbound.getMetadata().setStart(t_tilde);
						leftUnbound.getMetadata().setEnd(merged.getMetadata().getStart());
						transferFunction.transfer(leftUnbound);
					}
					transferFunction.transfer(merged);
					t_tilde = merged.getMetadata().getEnd();
					this.left_t_tilde.put(object, t_tilde);
				}
				
				PointInTime ts_max_right = this.areas[otherport].getMaxTs();
				if(ts_max_right != null && ts_max_right.after(t_tilde)){
					@SuppressWarnings("unchecked")
					T leftUnbound = this.dataMerge.createLeftFilledUp((T)object.clone());
					leftUnbound.getMetadata().setStart(t_tilde);
					PointInTime t_end = PointInTime.min(ts_max_right, object.getMetadata().getEnd());
					leftUnbound.getMetadata().setEnd(t_end);
					transferFunction.transfer(leftUnbound);
					
					// t_tilde kann neu gesetzt werden
					t_tilde = t_end;
				}
				
				this.left_t_tilde.put(object, t_tilde);
				this.areas[port].insert(object);
		
		
			}
		}
	}
	
	private void doRight(T object, int port){
		int leftport = port ^ 1;
		Order leftRight = Order.fromOrdinal(leftport);
		Order rightLeft = Order.fromOrdinal(port);

		Iterator<T> invalids = this.areas[leftport].extractElements(object, leftRight);
		while(invalids.hasNext()){
			T invalid = invalids.next();
			PointInTime invalid_t_tilde = this.left_t_tilde.get(invalid);
			if(invalid_t_tilde.before(invalid.getMetadata().getEnd())){
				@SuppressWarnings("unchecked")
				T leftUnbound = this.dataMerge.createLeftFilledUp((T)invalid.clone());
				leftUnbound.getMetadata().setStart(invalid_t_tilde);
				transferFunction.transfer(leftUnbound);
			}
		}
		
		Iterator<T> qualifies = this.areas[leftport].queryCopy(object, rightLeft);
		while(qualifies.hasNext()){
			T e_hat = qualifies.next(); // es werden nur kompatible Partner zurueckgeliefert, die auch nach einem Join das Join Praedikat erfuellen
			PointInTime e_hat_t_tilde = this.left_t_tilde.get(e_hat);
			T merged = this.merge(e_hat, object, leftRight);
			if(merged.getMetadata().getStart().after(e_hat_t_tilde)){
				@SuppressWarnings("unchecked")
				T leftUnbound = this.dataMerge.createLeftFilledUp((T) e_hat.clone());
				leftUnbound.getMetadata().setStart(e_hat_t_tilde);
				leftUnbound.getMetadata().setEnd(merged.getMetadata().getStart());
				this.transferFunction.transfer(leftUnbound);
			}
			// es muss <port> sein, weil der Startzeitstempel von Merged mit dem Startzeitstempel des rechten Elementes uebereinstimmt. 
			this.transferFunction.transfer(merged);
			this.left_t_tilde.put(e_hat, merged.getMetadata().getEnd());
		}
	}

	private T merge(T left, T right, Order order) {
//		if (logger.isTraceEnabled()) {
//			logger.trace("LeftJoinTIPO (" + hashCode() + ") start merging: " + left
//					+ " AND " + right);
//		}
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
	protected void process_done() {
		 transferFunction.done();
		 areas[0].clear();
		 areas[1].clear();
	 }
	
	@Override
	protected boolean isDone() { 
		if (getSubscribedTo(0).isDone()) {
			return getSubscribedTo(1).isDone() || areas[0].isEmpty();
		} else {
			return getSubscribedTo(0).isDone()  && areas[1].isEmpty();
		}
	}
}
