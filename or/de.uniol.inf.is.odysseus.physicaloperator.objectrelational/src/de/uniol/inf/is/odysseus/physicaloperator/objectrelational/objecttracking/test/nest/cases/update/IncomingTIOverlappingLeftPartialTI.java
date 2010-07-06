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
 * test case four
 * 
 * incoming partial nest TI is overlapping left of partial nest TI
 * 
 * a1   a2  a3  a4
 * 1    2   3   4   [3,7) partial nest A    
 * 1    5   6   7   [0,5) incoming partial nest B
 * 
 * result should be
 * 
 * 1    5   6   7   [0,3) 
 * [1   2   3   4], [1  5   6   7] [3,5)
 * 1    2   3   4   [5,7) 
 * 
 */

public class IncomingTIOverlappingLeftPartialTI extends Update {

    public IncomingTIOverlappingLeftPartialTI() {
        this.setName("testIncomingTIOverlappingLeftPartialTI");
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test 
    public void testIncomingTIOverlappingLeftPartialTI() {
    	
    	ObjectTrackingMetadata<Object> firstMeta = 
    		this.first.getMetadata();
    	
    	firstMeta.setStart(new PointInTime(3));
    	firstMeta.setEnd(new PointInTime(7));
    	
    	ObjectTrackingMetadata<Object> secondMeta = 
    		this.second.getMetadata();
    	
    	secondMeta.setStart(new PointInTime(0));
    	secondMeta.setEnd(new PointInTime(5));
    	
        try {
            this.update.invoke(this.nestPO, sa, first);
            this.update.invoke(this.nestPO, sa, second);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        Iterator<ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>> 
        iter = sa.iterator();
        
        ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> 
        first = iter.next();
        
        assertEquals(first.getMetadata().getStart(), new PointInTime(0));
        assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
        	firstPartialNest = first.getNest();
        
        assertTrue(firstPartialNest.size() == 1);
        assertEquals(firstPartialNest.get(0), this.second);
        
        ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> 
        	second = iter.next();
        
        assertEquals(second.getMetadata().getStart(), new PointInTime(3));
        assertEquals(second.getMetadata().getEnd(), new PointInTime(5));
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
        	secondPartialNest = second.getNest();
        
        assertTrue(secondPartialNest.size() == 2);
        assertEquals(secondPartialNest.get(0), this.first);
        assertEquals(secondPartialNest.get(1), this.second);
        
        ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> 
        	third = iter.next();  
        
        assertEquals(third.getMetadata().getStart(), new PointInTime(5));
        assertEquals(third.getMetadata().getEnd(), new PointInTime(7));
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
        	thirdPartialNest = third.getNest();
        
        assertTrue(thirdPartialNest.size() == 1);
        assertEquals(thirdPartialNest.get(0), this.first);
    }
}
