/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
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
    
    @Override
	@Before
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test 
    public void testIncomingTIEqualPartialTI() {
        this.first.setMetadata(
            new TimeInterval(
                new PointInTime(0),
                new PointInTime(10)
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
        assertEquals(first.getMetadata().getEnd(), new PointInTime(10));
        
        List<ObjectRelationalTuple<TimeInterval>> firstPartialNest = 
            first.getNest();
        
        assertTrue(firstPartialNest.size() == 2);
        assertEquals(firstPartialNest.get(0), this.first);
        assertEquals(firstPartialNest.get(1), this.second);
    }
    


}
