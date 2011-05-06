/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.ILeftMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.physicaloperator.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	private ILeftMergeFunction<T> dataMerge;
	private JoinTISweepArea<T>[] joinAreas;
	
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	private SDFAttributeList resultSchema;

	public SDFAttributeList getLeftSchema() {
		return leftSchema;
	}


	public SDFAttributeList getRightSchema() {
		return rightSchema;
	}


	public SDFAttributeList getResultSchema() {
		return resultSchema;
	}

	
	public LeftJoinTIPO(SDFAttributeList leftSchema, SDFAttributeList rightSchema, SDFAttributeList resultSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;
		this.resultSchema = resultSchema;
		this.left_t_tilde = new HashMap<T, PointInTime>();
	}
	
	
	public LeftJoinTIPO(LeftMergeFunction<T> dataMerge,
			IMetadataMergeFunction<M> metadataMerge,
			ITransferArea<T,T> transferFunction, JoinTISweepArea<T>[] areas) {
		super(dataMerge, metadataMerge, transferFunction, areas);
		this.left_t_tilde = new HashMap<T, PointInTime>();
		this.joinAreas = areas;
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
		
		// man muss immer den minimalen Zeitstempel der linken SweepArea wählen
		transferFunction.newHeartbeat(this.joinAreas[0].getMinTs(), port);
		
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
	
	private void doLeft(T object, int port){
		int otherport = port ^ 1;
		Order leftRight = Order.fromOrdinal(port);
		
		// purgeElemente nimmt keine Ruecksicht auf <order>
		joinAreas[otherport].purgeElements(object, leftRight);
		
		PointInTime t_tilde = object.getMetadata().getStart();
		
		Iterator<T> qualifies;
		synchronized (this.joinAreas) {
			synchronized (this.joinAreas[otherport]) {
				qualifies = joinAreas[otherport].queryCopy(object, leftRight);
		
				while (qualifies.hasNext()) {
					T next = qualifies.next();
					T merged = merge(object, next, leftRight);
					
					if(merged.getMetadata().getStart().after(t_tilde)){
						@SuppressWarnings("unchecked")
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
				
				PointInTime ts_max_right = this.joinAreas[otherport].getMaxTs();
				if(ts_max_right != null && ts_max_right.after(t_tilde)){
					@SuppressWarnings("unchecked")
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
				this.joinAreas[port].insert(object);
		
		
			}
		}
	}
	
	private void doRight(T object, int port){
		int leftport = port ^ 1;
		Order leftRight = Order.fromOrdinal(leftport);
		Order rightLeft = Order.fromOrdinal(port);

		Iterator<T> invalids = this.joinAreas[leftport].extractElements(object, leftRight);
		while(invalids.hasNext()){
			T invalid = invalids.next();
			PointInTime invalid_t_tilde = this.left_t_tilde.get(invalid);
			if(invalid_t_tilde.before(invalid.getMetadata().getEnd())){
				@SuppressWarnings("unchecked")
				T leftUnbound = null;
				leftUnbound = this.dataMerge.createLeftFilledUp((T)invalid.clone());
				leftUnbound.getMetadata().setStart(invalid_t_tilde);
				transferFunction.transfer(leftUnbound);
			}
		}
		
		Iterator<T> qualifies = this.joinAreas[leftport].queryCopy(object, rightLeft);
		while(qualifies.hasNext()){
			T e_hat = qualifies.next(); // es werden nur kompatible Partner zurueckgeliefert, die auch nach einem Join das Join Praedikat erfuellen
			PointInTime e_hat_t_tilde = this.left_t_tilde.get(e_hat);
			T merged = this.merge(e_hat, object, leftRight);
			if(merged.getMetadata().getStart().after(e_hat_t_tilde)){
				@SuppressWarnings("unchecked")
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
			this.joinAreas[i].clear();
		}
		this.transferFunction.init(this);
	}

	@Override
	protected void process_done() {
		 transferFunction.done();
		 joinAreas[0].clear();
		 joinAreas[1].clear();
	 }
	
	@Override
	protected boolean isDone() { 
		if (getSubscribedToSource(0).isDone()) {
			return getSubscribedToSource(1).isDone() || joinAreas[0].isEmpty();
		} else {
			return getSubscribedToSource(0).isDone()  && joinAreas[1].isEmpty();
		}
	}
	
	public ITemporalSweepArea<T>[] getAreas() {
		return joinAreas;
	}
	
	public void setAreas(JoinTISweepArea<T>[] areas) {
		this.joinAreas = areas;
		if (this.joinPredicate != null) {
			this.joinAreas[0].setQueryPredicate(this.joinPredicate);
			this.joinAreas[1].setQueryPredicate(this.joinPredicate);
		}
	}
	
	public void setDataMerge(ILeftMergeFunction<T> dataMerge){
		this.dataMerge = dataMerge;
	}
	
	public ILeftMergeFunction<T> getDataMerge(){
		return this.dataMerge;
	}
}
