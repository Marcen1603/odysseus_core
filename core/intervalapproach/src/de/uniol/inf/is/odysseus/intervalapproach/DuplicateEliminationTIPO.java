package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;

/**
 * This is an operator for removing duplicates that are valid
 * at the same point in time.
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 *
 */
public class DuplicateEliminationTIPO<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractPipe<T,T>{

	/**
	 * the list of next elems to return
	 * 
	 */
	private LinkedList<T> nextElems;
	
	/**
	 * the attributelist to emulate the left schema for the
	 * equality predicate
	 */
	//private SDFAttributeList leftPredicateSchema;
	
	/**
	 * the attributelist to emulate the right schema for the
	 * equality predicate
	 */
	//private SDFAttributeList rightPredicateSchema;
	
	/**
	 * the sweep area for this operator;
	 */
	private DefaultTISweepArea<T> sa;
	
	
	public DuplicateEliminationTIPO(DefaultTISweepArea<T> sweepArea){
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
	public synchronized void process_next(T next, int port) {
		
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
	public final void process_open(){
	}
	
	public final void process_close(){
	}
	
	@Override
	public DuplicateEliminationTIPO<T> clone() {
		return new DuplicateEliminationTIPO<T>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
	
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof DuplicateEliminationTIPO)) {
			return false;
		}
		DuplicateEliminationTIPO bpp = (DuplicateEliminationTIPO) ipo;
		if(this.getSubscribedToSource().equals(bpp.getSubscribedToSource())) {
			return true;
		}
		return false;
	}

}
