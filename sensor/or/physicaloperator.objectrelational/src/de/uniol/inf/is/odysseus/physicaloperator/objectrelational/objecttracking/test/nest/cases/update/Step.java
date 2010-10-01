package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;

/**
 * test case six
 * 
 * this test case tests two existent partials with one incoming and 
 * reducing the created incoming partials by union overlapping. 
 * 
 * 1    2   3   4   [3,5)
 * 1    5   6   7   [0,10)
 * 1    10  6   7   [0,10)
 *
 * 
 * result should be
 * 
 * [1   10  6   7], [1  5   6   7]  [0,3)
 * [1   2   3   4], [1  5   6   7], [1  10  6   7] [3,5)
 * [1   10  6   7], [1  5   6   7]  [5, 10)
 *
 */

public class Step extends Update {

    public Step() {
        this.setName("testStep");
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test 
    public void testStep() {
    	
    	ObjectTrackingMetadata<Object> firstMeta = 
    		new ObjectTrackingMetadata<Object>();
    	
    	firstMeta.setStart(new PointInTime(3));
    	firstMeta.setEnd(new PointInTime(5));
    	
    	this.first.setMetadata(firstMeta);
    	
    	ObjectTrackingMetadata<Object> secondMeta = 
    		new ObjectTrackingMetadata<Object>();    	
    	
    	secondMeta.setStart(new PointInTime(0));
    	secondMeta.setEnd(new PointInTime(10));  
    	
    	this.second.setMetadata(secondMeta);
        
        try {
            this.update.invoke(this.nestPO, sa, first);
            this.update.invoke(this.nestPO, sa, second);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> next; 
        next = this.second.clone();
        next.setAttribute(1, new String("10"));
        
        ObjectTrackingMetadata<Object> nextMeta = 
        	new ObjectTrackingMetadata<Object>();
        
        nextMeta.setStart(new PointInTime(0));
        nextMeta.setEnd(new PointInTime(10));
        
        next.setMetadata(nextMeta);
        
        try {
            this.update.invoke(this.nestPO, sa, next);
        } catch(Exception e) {
            e.printStackTrace();
        }   
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> firstNest = sa.poll().getNest();
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> secondNest = sa.poll().getNest();
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> thirdNest = sa.poll().getNest();
        
        assertEquals(firstNest.size(), 2);
        assertEquals(firstNest.get(0), next);
        assertEquals(firstNest.get(1), this.second);
        
        assertEquals(secondNest.size(), 3);
        assertEquals(secondNest.get(0), this.second);
        assertEquals(secondNest.get(1), this.first);
        assertEquals(secondNest.get(2), next);
            
        assertEquals(thirdNest.size(), 2);
        assertEquals(thirdNest.get(0), this.second);
        assertEquals(thirdNest.get(1), next);       
    }
}
