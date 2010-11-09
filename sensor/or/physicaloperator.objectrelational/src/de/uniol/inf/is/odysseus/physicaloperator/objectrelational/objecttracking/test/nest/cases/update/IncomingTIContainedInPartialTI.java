package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update;


import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper.ObjectTrackingPartialNest;

/**
*
* test case one
* 
* incoming partial nest TI is contained in the partial nest TI
* 
* a1   a2  a3  a4
* 1    2   3   4   [0,10) partial nest A   
* 1    5   6   7   [3,7) incoming partial nest B
* 
* result should be
* 
* [1   2   3   4]  [0,3)
* [1   2   3   4], [1  5   6   7] [3,7)
* [1   2   3   4]  [7,10)
*
*/
public class IncomingTIContainedInPartialTI extends Update {
    public IncomingTIContainedInPartialTI() {
        super();
        this.setName("testIncomingTIContainedInPartialTI");
    }

   @Override
@Before
   public void setUp() throws Exception {
       super.setUp();
   }

   @Test 
   public void testIncomingTIContainedInPartialTI() {
	   ObjectTrackingMetadata<Object> firstMeta = 
		   this.first.getMetadata();
	   
       firstMeta.setStart(new PointInTime(0));
       firstMeta.setEnd(new PointInTime(10));
       
	   ObjectTrackingMetadata<Object> secondMeta = 
		   this.second.getMetadata();       
       
	   secondMeta.setStart(new PointInTime(3));
	   secondMeta.setEnd(new PointInTime(7));
	   
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
       assertEquals(first.getMetadata().getEnd(), new PointInTime(3));
       
       List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> firstPartialNest = 
           first.getNest();
       
       assertTrue(firstPartialNest.size() == 1);
       assertEquals(firstPartialNest.get(0), this.first);
       
       ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> second = 
    	   iter.next();
       
       assertEquals(second.getMetadata().getStart(), new PointInTime(3));
       assertEquals(second.getMetadata().getEnd(), new PointInTime(7));
       
       List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> secondPartialNest = 
           second.getNest();
       
       assertTrue(secondPartialNest.size() == 2);
       assertEquals(secondPartialNest.get(0), this.first);
       assertEquals(secondPartialNest.get(1), this.second);
       
       ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> third = 
    	   iter.next();  
       
       assertEquals(third.getMetadata().getStart(), new PointInTime(7));
       assertEquals(third.getMetadata().getEnd(), new PointInTime(10));
       
       List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> thirdPartialNest = 
           third.getNest();
       
       assertTrue(thirdPartialNest.size() == 1);
       assertEquals(thirdPartialNest.get(0), this.first);
   }
   
}
