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
//package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.convert;
//
//import java.util.LinkedList;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.PointInTime;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.TimeInterval;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.container.Container;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.container.IMetaAttribute;
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//
//
///**
// * This is the phys ask operator for sliding windows in sparql stream processing.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class AskSlidePO<T extends IMetaAttribute<? extends ITimeInterval>> extends AbstractPipe<T, Container<Boolean, ? extends ITimeInterval>>{
//	
//	/**
//	 * this is the endtimestamp of the last true element
//	 * written into the output stream
//	 */
//	private PointInTime t_tilde;
//		
//	/**
//	 * this is the list of next elements to return
//	 */
//	private LinkedList<Container<Boolean, ? extends ITimeInterval>> nextElems;
//	
//	public AskSlidePO(AbstractLogicalOperator logical){
//		this.t_tilde = new PointInTime(0,0);
//		this.nextElems = new LinkedList<Container<Boolean, ? extends ITimeInterval>>();
//	}
//	
//	public AskSlidePO(AskSlidePO<T> original){
//		this.t_tilde = original.t_tilde;
//		this.nextElems = new LinkedList<Container<Boolean, ? extends ITimeInterval>>(original.nextElems);
//	}
//	
//	public AskSlidePO<T> clone(){
//		return new AskSlidePO<T>(this);
//	}
//	
//	public synchronized void process_next(T object, int port, boolean isReadOnly) {
//		this.doIntervalBased(object);
//		while(!this.nextElems.isEmpty()){
//			this.transfer(this.nextElems.removeFirst());
//		}
//	}
//	
//	private void doIntervalBased(IMetaAttribute<? extends ITimeInterval> next){
//		if(next.getMetadata().getStart().beforeOrEquals(this.t_tilde) && 
//				next.getMetadata().getEnd().after(this.t_tilde)){
//			TimeInterval newValidity = new TimeInterval(t_tilde, next.getMetadata().getEnd());
//			Container<Boolean, ITimeInterval> retval = new Container<Boolean, ITimeInterval>(new Boolean(true));
//			retval.setMetadata(newValidity);
//			this.nextElems.addLast(retval);
//		}
//		else if(next.getMetadata().getStart().after(t_tilde)){
//			TimeInterval newValFalse = new TimeInterval(t_tilde, next.getMetadata().getStart());
//			Container<Boolean, ITimeInterval> retvalfalse = new Container<Boolean, ITimeInterval>(new Boolean(false));
//			retvalfalse.setMetadata(newValFalse);
//			this.nextElems.addLast(retvalfalse);
//			
//			TimeInterval newValTrue = new TimeInterval(next.getMetadata().getStart(), next.getMetadata().getEnd());
//			Container<Boolean, ITimeInterval> retvaltrue= new Container<Boolean, ITimeInterval>(new Boolean(true));
//			retvaltrue.setMetadata(newValTrue);
//			this.nextElems.addLast(retvaltrue);
//		}
//		if(next.getMetadata().getEnd().after(this.t_tilde)){
//			t_tilde = next.getMetadata().getEnd();
//		}
//	}
//	
//	public void process_open(){
//	}
//	
//	public void process_close(){
//	}
//}
