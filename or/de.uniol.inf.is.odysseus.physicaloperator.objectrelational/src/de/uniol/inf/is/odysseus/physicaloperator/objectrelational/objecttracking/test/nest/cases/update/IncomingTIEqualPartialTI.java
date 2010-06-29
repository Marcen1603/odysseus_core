package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update;


import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper.ObjectTrackingPartialNest;


/**
 * 
 * test case three
 * 
 * incoming partial nest TI is equal to partial nest TI
 * 
 * a1   a2  a3  a4
 * 1    2   3   4   [0,10) partial nest A   
 * 1    5   6   7   [0,10) incoming partial nest B
 * 
 * result should be
 * 
 * [1   2   3   4],[1   5   6   7] [0,10)
 * 
 */
public class IncomingTIEqualPartialTI extends Update {

    public IncomingTIEqualPartialTI() {
        this.setName("testIncomingTIEqualPartialTI");
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test 
    public void testIncomingTIEqualPartialTI() {
    	
    	ObjectTrackingMetadata<Object> firstMeta = 
    		this.first.getMetadata();
    	
    	firstMeta.setStart(new PointInTime(0));
    	firstMeta.setEnd(new PointInTime(10));

    	ObjectTrackingMetadata<Object> secondMeta = 
    		this.second.getMetadata();    	
    	
    	secondMeta.setStart(new PointInTime(0));
    	secondMeta.setEnd(new PointInTime(10));
        
        try {
            this.update.invoke(this.nestPO, sa, first);
            this.update.invoke(this.nestPO, sa, second);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        Iterator<ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>> 
        	iter = sa.iterator();
        
        ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> first = 
        	iter.next();
        
        assertEquals(first.getMetadata().getStart(), new PointInTime(0));
        assertEquals(first.getMetadata().getEnd(), new PointInTime(10));
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
        	firstPartialNest = first.getNest();
        
        assertTrue(firstPartialNest.size() == 2);
        assertEquals(firstPartialNest.get(0), this.first);
        assertEquals(firstPartialNest.get(1), this.second);
    }
    


}
