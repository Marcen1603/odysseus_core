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
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.slice;
//
//import java.util.Iterator;
//import java.util.LinkedList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.intervalbased.TotallyBeforePredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.intervalbased.DefaultTISweepArea;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Slice;
//
///**
// * This is a slice operator for fixed windows. It produces output
// * for every fixed window.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class SliceFixedPO extends AbstractPipe<NodeList<? extends ITimeInterval>, 
//													NodeList<? extends ITimeInterval>>{
//
//	public DefaultTISweepArea<NodeList<? extends ITimeInterval>> sa;
//	
//	/*
//	 * defined, to be able to use them directly;
//	 */
//	private long limit; 
//	private long offset;
//	private long range;
//	
//	/**
//	 * this is number of the window in which the last element
//	 * taken by this.getInputNext() lay.
//	 */
//	private long numberOfLastWindow;
//	
//	/**
//	 * the list of next elems to put into the output stream
//	 */
//	public LinkedList<NodeList<? extends ITimeInterval>> nextElems;
//	
//	public SliceFixedPO(AbstractLogicalOperator logical){
//		
//		this.limit = ((Slice)logical).getLimit();
//		this.offset = ((Slice)logical).getOffset();
//		
//		this.range = ((Slice)logical).getFixedWidth();
//		
//		this.numberOfLastWindow = 0;
//		
//		this.sa = new DefaultTISweepArea<NodeList<? extends ITimeInterval>>();		
//		this.sa.setRemovePredicate(TotallyBeforePredicate.getInstance());
//			
//		
//		this.nextElems = new LinkedList<NodeList<? extends ITimeInterval>>();
//	}
//	
//	public SliceFixedPO(SliceFixedPO original){
//		this.limit = original.limit;
//		this.offset = original.offset;
//		this.sa = original.sa;
//		this.numberOfLastWindow = original.numberOfLastWindow;
//		this.range = original.range;
//		this.nextElems = new LinkedList<NodeList<? extends ITimeInterval>>(original.nextElems);
//	}
//
//	public SliceFixedPO clone(){
//		return new SliceFixedPO(this);
//	}
//	
//	public synchronized void process_next(NodeList<? extends ITimeInterval> object, int port, boolean isReadOnly) {
//		if(isReadOnly){
//			object = (NodeList<? extends ITimeInterval>)object.clone();
//		}
//		this.doIntervalBased(object);
//		
//		while(!this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//
//	}
//	
//	private void doIntervalBased(NodeList<? extends ITimeInterval> next){
//		// using integer division to emulate Match.floor();
//		long noOfActWindow = next.getMetadata().getStart().getMainPoint() / this.range;
//		if(noOfActWindow > this.numberOfLastWindow){
//			this.numberOfLastWindow = noOfActWindow;
//		
//			// the ordering must be inversed because, we only have a totally before predicate, but
//			// need a totally after predicate
//			Iterator<NodeList<? extends ITimeInterval>> iter = this.sa.extractElements(next, Order.RightLeft);
//			int counterI = 0;
//			int counterJ = 0;
//			
//			while(iter.hasNext()){
//				NodeList<? extends ITimeInterval> e_hat = iter.next();
//				if(counterI < this.offset){
//					counterI++;
//				}
//				else{
//					if(counterJ < this.limit){
//						counterJ++;
//						this.nextElems.addLast(e_hat);
//					}
//				}
//			}
//		}
//		
//		this.sa.insert(next);
//	}
//	
//	
//	public void process_close(){
//	}
//}
