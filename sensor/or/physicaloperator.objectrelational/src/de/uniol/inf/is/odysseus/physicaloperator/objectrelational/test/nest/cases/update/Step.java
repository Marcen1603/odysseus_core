package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.update;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;

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
        this.first.setMetadata(
            new TimeInterval(
                new PointInTime(3),
                new PointInTime(5)
            )
        );
        this.second.setMetadata(
            new TimeInterval(
                new PointInTime(0),
                new PointInTime(10)
            )
        );
        
        try {
            this.update.invoke(this.nestPO, sa, first);
            this.update.invoke(this.nestPO, sa, second);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        ObjectRelationalTuple<TimeInterval> next; 
        next = this.second.clone();
        next.setAttribute(1, new String("10"));
        
        next.setMetadata(
            new TimeInterval(
                    new PointInTime(0),
                    new PointInTime(10)
                )
            );
        
        try {
            this.update.invoke(this.nestPO, sa, next);
        } catch(Exception e) {
            e.printStackTrace();
        }   
        
        List<ObjectRelationalTuple<TimeInterval>> firstNest = sa.poll().getNest();
        List<ObjectRelationalTuple<TimeInterval>> secondNest = sa.poll().getNest();
        List<ObjectRelationalTuple<TimeInterval>> thirdNest = sa.poll().getNest();
        
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
