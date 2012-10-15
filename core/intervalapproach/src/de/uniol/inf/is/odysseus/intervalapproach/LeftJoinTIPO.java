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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.HashMap;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order;

/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von Daten- und
 * Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse implementierten
 * Operators entspricht jedoch der Logik eines JoinOperators im Intervallansatz.
 * @author abolles
 *
 * @param <M> Metadatentyp
 * @param <T> Datentyp
 */
public class LeftJoinTIPO<M extends ITimeInterval, T extends IStreamObject<M>> extends
		JoinTIPO<M, T> {
//	private static final Logger logger = LoggerFactory.getLogger(LeftJoinTIPO.class);
	
	private HashMap<T, PointInTime> left_t_tilde;
	private ILeftMergeFunction<T> dataMerge;
//	private JoinTISweepArea<T>[] joinAreas;
	
	private SDFSchema leftSchema;
	private SDFSchema rightSchema;
	private SDFSchema resultSchema;

	public SDFSchema getLeftSchema() {
		return leftSchema;
	}


	public SDFSchema getRightSchema() {
		return rightSchema;
	}


	public SDFSchema getResultSchema() {
		return resultSchema;
	}

	
	public LeftJoinTIPO(SDFSchema leftSchema, SDFSchema rightSchema, SDFSchema resultSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
		this.left_t_tilde = new HashMap<T, PointInTime>();
	}
	
	
	public LeftJoinTIPO(LeftMergeFunction<T> dataMerge,
			IMetadataMergeFunction<M> metadataMerge,
			ITransferArea<T,T> transferFunction, ITimeIntervalSweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		this.left_t_tilde = new HashMap<T, PointInTime>();
		this.areas = areas;
		this.dataMerge = dataMerge;
	}

	@Override
	protected void process_next(T object, int port) {
		if (isDone()) {
			return;
		}
		
// Was soll denn der Unfung?
		//		if(this.hashCode() == 29392698){
//			String s = "hallo";
//		}
		
		if(port == 0){
			this.doLeft(object, port);
		}
		else{
			this.doRight(object, port);
		}
		
		// man muss immer den minimalen Zeitstempel der linken SweepArea wählen
		transferFunction.newHeartbeat(this.areas[0].getMinTs(), port);
		
//		transferFunction.newElement(object, port);
		
		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
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
						T leftUnbound = null;
						leftUnbound = this.dataMerge.createLeftFilledUp((T)object.clone());
						
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
					T leftUnbound = null;
					leftUnbound = this.dataMerge.createLeftFilledUp((T)object.clone());
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
	
	@SuppressWarnings("unchecked")
	private void doRight(T object, int port){
		int leftport = port ^ 1;
		Order leftRight = Order.fromOrdinal(leftport);
		Order rightLeft = Order.fromOrdinal(port);

		// first process all invalid elements
		Iterator<T> invalids = this.areas[leftport].extractElements(object, leftRight);
		while(invalids.hasNext()){
			T invalid = invalids.next();
			PointInTime invalid_t_tilde = this.left_t_tilde.get(invalid);
			// the invalid object will not be needed anymore
			this.left_t_tilde.remove(invalid);
			if(invalid_t_tilde.before(invalid.getMetadata().getEnd())){
				T leftUnbound = null;
				leftUnbound = this.dataMerge.createLeftFilledUp((T)invalid.clone());
				leftUnbound.getMetadata().setStart(invalid_t_tilde);
				transferFunction.transfer(leftUnbound);
			}
		}
		
		// second, process all valid elements with join partner
		Iterator<T> qualifies = this.areas[leftport].queryCopy(object, rightLeft);
		while(qualifies.hasNext()){
			T e_hat = qualifies.next(); // es werden nur kompatible Partner zurueckgeliefert, die auch nach einem Join das Join Praedikat erfuellen
			PointInTime e_hat_t_tilde = this.left_t_tilde.get(e_hat);
			T merged = this.merge(e_hat, object, leftRight);
			if(merged.getMetadata().getStart().after(e_hat_t_tilde)){
				T leftUnbound = null;
				leftUnbound = this.dataMerge.createLeftFilledUp((T) e_hat.clone());
				leftUnbound.getMetadata().setStart(e_hat_t_tilde);
				leftUnbound.getMetadata().setEnd(merged.getMetadata().getStart());
				this.transferFunction.transfer(leftUnbound);
			}
			// es muss <port> sein, weil der Startzeitstempel von Merged mit dem Startzeitstempel des rechten Elementes uebereinstimmt. 
			this.transferFunction.transfer(merged);
			this.left_t_tilde.put(e_hat, merged.getMetadata().getEnd());
		}
		
		this.areas[port].insert(object);
		
//		// third, process all elements, that are still in
//		// left_t_tilde with t_tilde < object.getStart();
//		// since left_t_tilde has been updated by the
//		// processing before, we can simply compare
//		// object.getStart() and each t_tilde timestamp
//		// each time t_tilde < object.getStart() we
//		// insert the left element with [t_tilde;objet.getStart())
//		// into the transfer function
//		List<Pair<T, PointInTime>> modifiedElements = new ArrayList<Pair<T, PointInTime>>();
//		for(Entry<T, PointInTime> entry: this.left_t_tilde.entrySet()){
//			if(entry.getValue().before(object.getMetadata().getStart())){
//				T leftUnbound = null;
//				leftUnbound = this.dataMerge.createLeftFilledUp((T)entry.getKey().clone());
//				leftUnbound.getMetadata().setStart(entry.getValue());
//				leftUnbound.getMetadata().setEnd(object.getMetadata().getStart());
//				this.transferFunction.transfer(leftUnbound);
//				Pair<T, PointInTime> newEntry = new Pair(entry.getKey(), entry.getValue());
//				modifiedElements.add(newEntry);
//			}
//		}
//		
//		// insert the modified left_t_tilde entries
//		for(Pair<T, PointInTime> newEntry: modifiedElements){
//			this.left_t_tilde.put(newEntry.getE1(), newEntry.getE2());
//		}
		
	}

	@Override
	protected T merge(T left, T right, Order order) {
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

	@SuppressWarnings("unchecked")
	@Override
	protected void process_done() {
		System.out.println("LeftJoinTIPO (" + this.hashCode() + ").processDone().");
		
		// transfer all elements from the left side
		while(!areas[0].isEmpty()){
			T leftUnbound = areas[0].poll();
			// get t_tilde
			PointInTime t_tilde = this.left_t_tilde.get(leftUnbound);
			
			// in the case that we have infinite windows
			// t_tilde can be infinity. In this case
			// we do not have put a new element into
			// the transfer function. More general
			// we only have to put a new element into
			// the transfer function, if t_tilde < element.getEnd()
			if(t_tilde == null){
				leftUnbound = this.dataMerge.createLeftFilledUp((T) leftUnbound.clone());
				// start timestamp is already set from the copy
				this.transferFunction.transfer(leftUnbound);
			}
			else if(t_tilde.before(leftUnbound.getMetadata().getEnd())){
				leftUnbound = this.dataMerge.createLeftFilledUp((T) leftUnbound.clone());
				leftUnbound.getMetadata().setStart(t_tilde);
				this.transferFunction.transfer(leftUnbound);
			}
		}
		transferFunction.done();
		areas[0].clear();
		areas[1].clear();
	 }
	
	@Override
	protected boolean isDone() { 
		if (getSubscribedToSource(0).isDone()) {
			return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
		}
        return getSubscribedToSource(0).isDone()  && areas[1].isEmpty();
	}
	
	public void setDataMerge(ILeftMergeFunction<T> dataMerge){
		this.dataMerge = dataMerge;
	}
	
	@Override
    public ILeftMergeFunction<T> getDataMerge(){
		return this.dataMerge;
	}
}
