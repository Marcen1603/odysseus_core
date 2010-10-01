/*
 * SlidingDeltaWindowPO.java
 *
 * Created on 13. November 2007, 13:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.planmanagement.IWindow;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.PosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.predicate.BeforePredicate;
import de.uniol.inf.is.odysseus.pnapproach.base.predicate.LiesInPNPredicate;
import de.uniol.inf.is.odysseus.pnapproach.base.sweeparea.DefaultPNSweepArea;

/**
 * This is the physical sliding delta window po. It returns elements
 * after a period of time (delta) and is blocking all the other time.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
public class SlidingPeriodicWindowPNPO<R extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPipe<R, R> implements IWindow {
    
	/**
	 * This operator needs a sweeparea if the pos-neg approach
	 * will be used.
	 */
	private DefaultPNSweepArea<IMetaAttributeContainer<? extends IPosNeg>> sa;
	
	private long windowSize;
	private long windowAdvance;
	
	/**
	 * This is the number of slides that have been processed.
	 */
	private long slideNo;
	
    /** Creates a new instance of SlidingDeltaWindowPO */
    public SlidingPeriodicWindowPNPO(long windowSize, long windowAdvance) {
        this.windowSize = windowSize;
        this.windowAdvance = windowAdvance;
        
    	this.sa = new DefaultPNSweepArea<IMetaAttributeContainer<? extends IPosNeg>>();
    	
    	this.sa.setRemovePredicate(BeforePredicate.getInstance());
    	// the query predicate will be set every time the
    	// query method will be used, because it has to be updated
    	// with the new timestamps. This is a workaround for
    	// the fact that we need a predicate that compares
    	// a pn element to a timeinterval element

    	this.slideNo = 0;
    }
    
    public SlidingPeriodicWindowPNPO(SlidingPeriodicWindowPNPO<R> original){
        this.sa = original.sa;
        this.slideNo = original.slideNo;
        this.windowSize = original.windowSize;
        this.windowAdvance = original.windowAdvance;
    }
    
    @Override
	public SlidingPeriodicWindowPNPO<R> clone(){
        return new SlidingPeriodicWindowPNPO<R>(this);
    }
    
    @Override
    public OutputMode getOutputMode() {
    	return OutputMode.MODIFIED_INPUT;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public void process_next(R next, int port){	
		long t_s = next.getMetadata().getTimestamp().getMainPoint();
		long delta = this.windowAdvance;
		long winSize = this.windowSize;
		//first check if elements have to be removed and/or delivered
		if((t_s /delta ) >= this.slideNo){
			this.slideNo = t_s/delta;
			
			// first remove all old elements. Other than in the interval based approach
			// in the p/n approach only the elements can be removed, that have the following
			// timestamp
			// timstamp < (t_s/delta) * delta - winSize
			long remove_t_start = (this.slideNo * delta) - winSize - 1; // -1 because the predicate uses beforeOrEquals()-method
			PointInTime p_remove = new PointInTime(remove_t_start);
			IPosNeg pn = new PosNeg();
			pn.setTimestamp(p_remove);
			IMetaAttributeContainer<IPosNeg> ref_elem = new MetaAttributeContainer<IPosNeg>(pn);
			
			Iterator<IMetaAttributeContainer<? extends IPosNeg>> iter_remove = this.sa.extractElements(ref_elem, Order.RightLeft);
			// write the negative elements
			while(iter_remove.hasNext()){
				// the element has to be copied, because it will be edited
				R retval;
				retval = (R)iter_remove.next().clone();
				retval.getMetadata().setElementType(ElementType.NEGATIVE);
				// edit the timeinterval
				retval.getMetadata().setTimestamp(new PointInTime(remove_t_start));
				this.transfer(retval);
			} 
			
			// return all elements with a start timestamp 
			// (t_s/delta) * delta - winsize <= start timestamp < (t_s/ delta) * delta
			long comp_t_start = this.slideNo * delta - winSize;
			long comp_t_end = this.slideNo * delta;
			PointInTime p_start = new PointInTime(comp_t_start);
			PointInTime p_end = new PointInTime(comp_t_end);
			
			// the query predicate must be updated every time, because the start and end time
			// stamps continuously change
			LiesInPNPredicate p_query = new LiesInPNPredicate(p_start, p_end);
			this.sa.setQueryPredicate(p_query);
			
			// now write all positive elements
			// you have to use Order.RightLeft, because the LiesInPNPredicate only evaluates
			// left element in the method evaluate(T left, T right), so the left element
			// must be an element from the sweep area.
			Iterator<IMetaAttributeContainer<? extends IPosNeg>> iter = this.sa.query(null, Order.RightLeft);
			while(iter.hasNext()){
				R retval;
				retval = (R) iter.next().clone();
				retval.getMetadata().setElementType(ElementType.POSITIVE);
				// edit the time interval
				retval.getMetadata().setTimestamp(p_start);
				this.transfer(retval);
			}

		}
		
		this.sa.insert(next);
    }
    
    
    public final void process_close(){
    }

	@Override
	public long getWindowSize() {
		return this.windowSize;
	}
	
	@Override
	public WindowType getWindowType() {
		return WindowType.TIME_BASED;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		//TODO punctuation kann genutzt werden ...
	}

}
