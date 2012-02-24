package de.uniol.inf.is.odysseus.core.server.sparql.physicalops.interval.window;

import java.util.Iterator;
import java.util.LinkedList;
import com.hp.hpl.jena.graph.Triple;

import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.PointInTime;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.intervalbased.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.sweeparea.intervalbased.UpdateAwareTISweepArea;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.interval.object.predicates.UpdatePredicate;
import de.uniol.inf.is.odysseus.core.server.queryexecution.po.sparql.object.MetaTriple;
import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.WindowAO;
import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.StartEndPredicate;
import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.streaming.sparql.StartEndPredicateWindow;

/**
 * This is a start end predicate window reduced by a sliding tupel window.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 *
 */
public class StartEndPredicateAndSlidingTupelWindowPO extends
				AbstractPipe<MetaTriple<? extends ITimeInterval>, MetaTriple<? extends ITimeInterval>>{

	/**
	 * the logical counterpart for this operator
	 */
	private AbstractLogicalOperator ao;
	
	/**
	 * indicates whether a window is open or not
	 */
	private boolean started;
	
    /**
     * in this variable the element will be stored that
     * started the last window. this is necessary to check
     * if the closing element has the same blank node
     * if the same blank nodes were used in start and end
     * predicate
     */
    private Triple startTriple;
    
    /**
     * this is the start predicate for the window
     */
    private StartEndPredicate startP;
    
    /**
     * this is the end predicate for the window
     */
    private StartEndPredicate endP;
    
    /**
     * this tells you, if the subject of the start and
     * end triple have to be the same, because of 
     * using the same variables.
     */
    private boolean sameSubject;
    
    /**
     * this tells you, if the predicate of the start and
     * end triple have to be the same, because of 
     * using the same variables.
     */
    private boolean samePredicate;
    
    /**
     * this tells you, if the object of the start and
     * end triple have to be the same, because of 
     * using the same variables.
     */ 
    private boolean sameObject;
	
	private DefaultTISweepArea<MetaTriple<? extends ITimeInterval>> sa;
	
    /**
     * this array list contains elements, that have not been
     * submitted to the next operator
     * getInputNext will only be called if this list is empty, otherwise
     * the next element from this list will be returned
     */
    private LinkedList<MetaTriple<? extends ITimeInterval>> nextElems;
    
	/**
	 * list for ordering the output elements by their timestamps
	 *
	 */
	private DefaultTISweepArea<MetaTriple<? extends ITimeInterval>> orderingList;
    
    /** Creates a new instance of StartEndPredicateAndSlidingDeltaWindowPO */
    public StartEndPredicateAndSlidingTupelWindowPO(AbstractLogicalOperator logical) {
        this.ao = logical;
        this.started = false;
        this.nextElems = new LinkedList<MetaTriple<? extends ITimeInterval>>();
        this.sa = new UpdateAwareTISweepArea(((WindowAO)((StartEndPredicateWindow)logical).getReducingWindow()).getWindowSize());
        
        // creating the remove predicate
        UpdatePredicate p_remove = UpdatePredicate.getInstance();
        this.sa.setRemovePredicate(p_remove);
        
        this.startP = ((StartEndPredicateWindow)this.ao).getStartPredicate();
        this.endP = ((StartEndPredicateWindow)this.ao).getEndPredicate();
        
        if(startP.getTriple().getSubject().isVariable() && endP.getTriple().getSubject().isVariable() &&
        		startP.getTriple().getSubject().equals(endP.getTriple().getSubject())){
        	this.sameSubject = true;
        }
        else{
        	this.sameSubject = false;
        }
        
        if(startP.getTriple().getPredicate().isVariable() && endP.getTriple().getPredicate().isVariable() &&
        		startP.getTriple().getPredicate().equals(endP.getTriple().getPredicate())){
        	this.samePredicate = true;
        }
        else{
        	this.samePredicate = false;
        }
        
        if(startP.getTriple().getObject().isVariable() && endP.getTriple().getObject().isVariable() &&
        		startP.getTriple().getObject().equals(endP.getTriple().getObject())){
        	this.sameObject = true;
        }
        else{
        	this.sameObject = false;
        }     
        
        this.orderingList = new DefaultTISweepArea<MetaTriple<? extends ITimeInterval>>();
    }
    
    public StartEndPredicateAndSlidingTupelWindowPO(
            StartEndPredicateAndSlidingTupelWindowPO original){
        this.ao = original.ao;
        this.started = original.started;
        this.sa = original.sa;
        this.nextElems = original.nextElems;
        this.startP = original.startP;
        this.endP = original.endP;
        this.sameSubject = original.sameSubject;
        this.samePredicate = original.samePredicate;
        this.sameObject = original.sameObject;
        this.orderingList = original.orderingList;

    }
    
    public StartEndPredicateAndSlidingTupelWindowPO clone(){
    	return new StartEndPredicateAndSlidingTupelWindowPO(this);
    }
    
    public synchronized void process_next(MetaTriple<? extends ITimeInterval> object, int port, boolean isReadOnly) {
    	if(isReadOnly){
    		object = (MetaTriple<? extends ITimeInterval>)object.clone();
    	}

//            for testing
//            if(cargo.getObject().isLiteral() && cargo.getObject().getLiteralLexicalForm().equals("3051")){
//            	cargo = new Triple(cargo.getSubject(), cargo.getPredicate(), Node.createLiteral("3051", null, XSDDatatype.XSDinteger));
//            }
        // the following to if-clauses
        // should never be true both,
        // but if it is so the window has to be closed
        if(!started && startP.evaluate(object)){
        	started = true;
        	this.startTriple = object;
        }
        if(started && endP.evaluate(object)){
        	if(started && (this.sameSubject || this.sameObject || this.samePredicate) ){
        		boolean s_Ok = false, p_Ok = false, o_Ok = false;
        		if((this.sameSubject && this.startTriple.getSubject().equals(object.getSubject())) || !this.sameSubject){
        			s_Ok = true;
        		}
        		if((this.samePredicate && this.startTriple.getPredicate().equals(object.getPredicate()))|| !this.samePredicate){
        			p_Ok = true;
        		}
        		if((this.sameObject && this.startTriple.getPredicate().equals(object.getObject()))|| !this.sameObject){
        			o_Ok = true;
        		}
        		
        		if(s_Ok && p_Ok && o_Ok){
        			started = false;
        			this.startTriple = null;
        		}
        	}
        	else{
        		started = false;
        		this.startTriple = null;
        	}

        }
        
        if(started){
        	Iterator<MetaTriple<? extends ITimeInterval>> results = this.sa.extractElements(object, Order.LeftRight);
        	while(results.hasNext()){
        		MetaTriple<? extends ITimeInterval> e_hat = results.next();
        		
        		// the end timestamp of an element from the
        		// sweep area has to be set to the start timestamp
        		// of the new element in every case
        		// 1. if the new element is an update, the new
        		// element will be valid from now on
        		// 2. if the new element is not an update, the old
        		// one has been returned, because it has been in 
        		// the sweep area for more than maxSize elements,
        		// so the end of validity of this element will be set
        		// to the start of the new element.
    			e_hat.getMetadata().setEnd(object.getMetadata().getStart());
    			this.orderingList.insert(e_hat);

        	}
        	// the end of the validity of the new element can not be determined
        	// until it will be returned from the sweep area
    		this.sa.insert(object);
    		
    		// now write all elements to the list nextElems, that have a start timestamp, that
    		// is smaller than the smallest timestamp of all elements in the sweep area
    		PointInTime minTs = this.sa.getMinTs();
        	// reordering is not necessary any more, because the ordering list
        	// is a sweep area now, that will always be implemented
			boolean foundValidElement = false;
			while(!this.orderingList.isEmpty() && !foundValidElement){
				if(this.orderingList.peek().getMetadata().getStart().before(minTs)){
					this.nextElems.addLast(this.orderingList.poll());
				}
				else{
					foundValidElement = true;
				}
			}
    	}
        // this window is closed, so remove all the elements from the sweep
        // area and do not add the new element
        else{
        	Iterator<MetaTriple<? extends ITimeInterval>> results = this.sa.extractAllElements();
        	while(results.hasNext()){
        		MetaTriple<? extends ITimeInterval> e_hat = results.next();
    			e_hat.getMetadata().setEnd(object.getMetadata().getStart());
    			this.orderingList.insert(e_hat);
        	}
        	
        	// reordering is not necessary any more, because the ordering list
        	// is a sweep area now, that will always be implemented
        	while(!this.orderingList.isEmpty()){
        		this.nextElems.addLast(this.orderingList.poll());
        	}
        }
        while(!this.nextElems.isEmpty()){
        	this.transfer(this.nextElems.removeFirst());
        }
    }
        
    public void process_close(){
    }
}