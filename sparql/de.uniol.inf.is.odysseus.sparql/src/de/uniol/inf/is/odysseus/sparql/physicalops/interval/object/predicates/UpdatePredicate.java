/*
 * StreamBufferUpdatePredicate.java
 *
 * Created on 14. November 2007, 11:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.sparql.physicalops.interval.object.predicates;

import de.uniol.inf.is.odysseus.queryexecution.po.base.object.ITimeInterval;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.MetaTriple;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.util.SPARQL_Util;

/**
 * This checks if an element s can be an update of s_hat.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
public class UpdatePredicate extends AbstractPredicate<MetaTriple<? extends ITimeInterval>>{
    
	private static UpdatePredicate theInstance = new UpdatePredicate();
	
    /** Creates a new instance of StreamBufferUpdatePredicate */
    private UpdatePredicate() {
    }
    
    public boolean evaluate(MetaTriple<? extends ITimeInterval> s){
    	throw new UnsupportedOperationException();
    }
    
    /**
     * @return true if s and s_hat are triples and have the same subject and
     * and predicate and s_hat is before s, false otherwise.
     */
    public boolean evaluate(MetaTriple<? extends ITimeInterval> s, MetaTriple<? extends ITimeInterval> s_hat){
    	return SPARQL_Util.isPossibleUpdate(s, s_hat);
    }
    
    public static UpdatePredicate getInstance(){
    	return theInstance;
    }
    
    public UpdatePredicate clone(){
    	return theInstance;
    }
    
}
