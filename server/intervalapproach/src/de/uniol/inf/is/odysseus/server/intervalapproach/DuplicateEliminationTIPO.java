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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This is an operator for removing duplicates that are valid
 * at the same point in time.
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 *
 */
public class DuplicateEliminationTIPO<T extends IStreamObject<? extends ITimeInterval>> extends AbstractPipe<T,T>{

	/**
	 * the list of next elems to return
	 * 
	 */
	private LinkedList<T> nextElems;
	
	/**
	 * the attributelist to emulate the left schema for the
	 * equality predicate
	 */
	//private SDFSchema leftPredicateSchema;
	
	/**
	 * the attributelist to emulate the right schema for the
	 * equality predicate
	 */
	//private SDFSchema rightPredicateSchema;
	
	/**
	 * the sweep area for this operator;
	 */
	private ITimeIntervalSweepArea<T> sa;
	
	
	public DuplicateEliminationTIPO(ITimeIntervalSweepArea<T> sweepArea){
		this.sa = sweepArea;
		
		this.nextElems = new LinkedList<T>();
	}
	
	public DuplicateEliminationTIPO(DuplicateEliminationTIPO<T> original){
		this.sa = original.sa;
		this.nextElems = original.nextElems;
	}
	
	public DuplicateEliminationTIPO<T> cloneMe(){
		return new DuplicateEliminationTIPO<T>(this);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	public void process_next(T next, int port) {
		
		// Using extractElements the odering parameters for the
		// remove predicate must be inversed,
		// we only have a totally before predicate but need a
		// totally after predicate
		Iterator<T> iter = this.sa.extractElements(next, Order.RightLeft);
		
		while(iter.hasNext()){
			this.transfer(iter.next());
		}
		
		Iterator<T> qualifies = this.sa.query(next, Order.LeftRight);
		if(!qualifies.hasNext()){
			this.sa.insert(next);
			// now the sweep area has to be sorted again
			// because there can be elements in the sweep
			// area that have a later start timestamp
			// as this element, because they are not equal
			// to this new element and have been splitted
			// when they were inserted into the sweep area.
			
			// Nicht mehr notwendig, da immer sortiert wird (TreeMap)
			// this.sa.sort();
		}
		else{
			PointInTime maxEnd = new PointInTime(0);
			while(qualifies.hasNext()){
				PointInTime newEnd = qualifies.next().getMetadata().getEnd();
				if(maxEnd.before(newEnd)){
					maxEnd = newEnd;
				}
			}
			if(maxEnd.before(next.getMetadata().getEnd())){
				next.getMetadata().setStart(maxEnd);
				this.sa.insert(next);
				// now the sweep area has to be sorted again
				// because there can be elements in the sweep
				// area that have a later start timestamp
				// as this element, because they are not equal
				// to this new element and have been splitted
				// when they were inserted into the sweep area.
				
				//Nicht mehr notwendig, da immer sortiert wird (TreeMap)
				//this.sa.sort();
			}
		}
//			// for testing >>>>>>>>>>>>>>>>>>><
//			System.out.println("=========== SWEEP AREA DE ===============");
//			for(Object o : this.sa.getAll()){
//				System.out.println("SA Elem: " + o);
//			}
//			System.out.println("=========================================");
//			// <<<<<<<<<<<<<<<<<<<<<<<<<<
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public final void process_open(){
	}
	
	@Override
	public final void process_close(){
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof DuplicateEliminationTIPO)) {
			return false;
		} else {
			return true;
		}
	}

}
