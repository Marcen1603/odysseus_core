package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.update;


import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.PartialNest;

/**
 * test case five
 * 
 * incoming partial nest TI contains whole partial nest TI
 * 
 * 1    2   3   4   [3,5)
 * 1    5   6   7   [0,10)
 * 
 * result should be
 * 
 * 1    5   6   7   [0,3)
 * [1   2   3   4], [1  5   6   7] [3,5)
 * 1    5   6   7   [5, 10)
 *
 */
public class IncomingTIContainsPartialTI extends Update {

    public IncomingTIContainsPartialTI() {
        this.setName("testIncomingTIContainsPartialTI");
    }
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test 
    public void testIncomingTIContainsPartialTI() {
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
        
        Iterator<PartialNest<TimeInterval>> iter = sa.iterator();
        
        PartialNest<TimeInterval> first = iter.next();
        
        assertEquals(first.getMetadata().getStart(), new PointInTime(0));
        assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
        
        List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
            first.getNest();
        
        assertTrue(firstPartialNest.size() == 1);
        assertEquals(firstPartialNest.get(0), this.second);
        
        PartialNest<TimeInterval> second = iter.next();
        
        assertEquals(second.getMetadata().getStart(), new PointInTime(3));
        assertEquals(second.getMetadata().getEnd(), new PointInTime(5));
        
        List<ObjectRelationalTuple<TimeInterval>> secondPartialNest = 
            second.getNest();
        
        assertTrue(secondPartialNest.size() == 2);
        assertEquals(secondPartialNest.get(0), this.second);
        assertEquals(secondPartialNest.get(1), this.first);
        
        PartialNest<TimeInterval> third = iter.next();  
        
        assertEquals(third.getMetadata().getStart(), new PointInTime(5));
        assertEquals(third.getMetadata().getEnd(), new PointInTime(10));
        
        List<ObjectRelationalTuple<TimeInterval>> thirdPartialNest = 
            third.getNest();
        
        assertTrue(thirdPartialNest.size() == 1);
        assertEquals(thirdPartialNest.get(0), this.second);
    }
}
