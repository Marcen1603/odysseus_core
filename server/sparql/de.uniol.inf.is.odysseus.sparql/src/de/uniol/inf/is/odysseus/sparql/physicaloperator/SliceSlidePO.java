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
//import java.util.List;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.PointInTime;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.intervalbased.OverlapsPredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.predicate.intervalbased.TotallyBeforePredicate;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.intervalbased.DefaultTISweepArea;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.NodeList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.util.SPARQL_Util;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.Slice;
//
///**
// * This is a slice operator for sliding windows. It produces output
// * for every time instant t.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class SliceSlidePO extends AbstractPipe<NodeList<? extends ITimeInterval>, 
//													NodeList<? extends ITimeInterval>>{
//
//	public DefaultTISweepArea<NodeList<? extends ITimeInterval>> sa;
//	
//	/*
//	 * defined, to be able to use them directly;
//	 */
//	private long limit; 
//	private long offset;
//	
//	/**
//	 * the list of next elems to put into the output stream
//	 */
//	public LinkedList<NodeList<? extends ITimeInterval>> nextElems;
//	
//	public SliceSlidePO(AbstractLogicalOperator logical){
//		
//		this.limit = ((Slice)logical).getLimit();
//		this.offset = ((Slice)logical).getOffset();
//		
//		this.sa = new DefaultTISweepArea<NodeList<? extends ITimeInterval>>();
//		this.sa.setRemovePredicate(TotallyBeforePredicate.getInstance());
//		this.sa.setQueryPredicate(OverlapsPredicate.getInstance());
//		
//		this.nextElems = new LinkedList<NodeList<? extends ITimeInterval>>();
//	}
//	
//	public SliceSlidePO(SliceSlidePO original){
//		this.limit = original.limit;
//		this.offset = original.offset;
//		this.nextElems = original.nextElems;
//		this.sa = original.sa;
//	}
//
//	public SliceSlidePO clone(){
//		return new SliceSlidePO(this);
//	}
//	
//	public synchronized void process_next(NodeList<? extends ITimeInterval> object, int port, boolean isReadOnly) {
//
//		doIntervalBased(object);
//		while(!this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//
//	}
//	
//	private void doIntervalBased(NodeList<? extends ITimeInterval> next){
//		// The ordering must be inversed because, we need a totally after predicate, but only
//		// have a totally before predicate
//		Iterator<NodeList<? extends ITimeInterval>> iter = this.sa.extractElements(next, Order.RightLeft);
//		int counterI = 0;
//		int counterJ = 0;
//		PointInTime t_tilde = new PointInTime(0,0);
//		
//		while(iter.hasNext()){
//			NodeList<? extends ITimeInterval> e_hat = iter.next();
//			if(t_tilde.before(e_hat.getMetadata().getStart())){
//				counterI = 0;
//				counterJ = 0;
//				t_tilde = e_hat.getMetadata().getStart();
//			}
//			if(counterI < this.offset){
//				counterI++;
//			}
//			else{
//				if(counterJ < this.limit){
//					counterJ++;
//					this.nextElems.addLast(e_hat);
//				}
//			}
//		}
//		
//		List<NodeList<? extends ITimeInterval>> splittedNewElements = 
//			new LinkedList<NodeList<? extends ITimeInterval>>();
//		splittedNewElements.add(next);
//		// Braucht man die naechste Zeile wirklich
////		splittedNewElements.add(new NodeList<ITimeInterval>());
//		Iterator<NodeList<? extends ITimeInterval>> qualifies = this.sa.query(next, Order.LeftRight);
//		List<NodeList<? extends ITimeInterval>> splittedOldElements = 
//			new LinkedList<NodeList<? extends ITimeInterval>>();
//		
//		while(qualifies.hasNext()){
//			splittedOldElements.add(qualifies.next());
//		}
//		
//		// remove the old elements
//		this.sa.removeAll(splittedOldElements);
//		
//		List<List<NodeList<? extends ITimeInterval>>> result = 
//			SPARQL_Util.splitElements(splittedNewElements,
//										splittedOldElements);
//		
//		this.sa.insertAll(result.get(0));
//		this.sa.insertAll(result.get(1));
//		
//	}
//	
//	
//	public void process_close(){
//	}
//}
