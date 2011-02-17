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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.cases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.UnnestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.fixtures.SimpleUnnestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class Unnest extends TestCase {

    private List<ObjectRelationalTuple<TimeInterval>> inputTuples;
    private List<ObjectRelationalTuple<TimeInterval>> result;
    
    public Unnest() {
        this.setName("unnest");
    }
    
    @Override
	@Before
    public void setUp() throws Exception {
        
        Factory fixtures;
        UnnestPO<TimeInterval> unnestPO;
        
        SDFAttributeList inputSchema;
        SDFAttributeList outputSchema;      
        SDFAttribute nestingAttribute;
        
        fixtures = new SimpleUnnestingFixture();
        inputSchema = fixtures.getInputSchema();
        outputSchema = fixtures.getOutputSchema();
        nestingAttribute = fixtures.getNestingAttribute();
        this.inputTuples = fixtures.getInputTuples();

        unnestPO = new UnnestPO<TimeInterval>(
            inputSchema, 
            outputSchema, 
            nestingAttribute      
        );

        this.result = 
            new ArrayList<ObjectRelationalTuple<TimeInterval>>();
        
        for(ObjectRelationalTuple<TimeInterval> tuple : this.inputTuples) {
            unnestPO.processNextTest(tuple, 0);
        }
        
        while(!unnestPO.isDone()) {
            result.add(unnestPO.deliver());
        }               
    }

    @SuppressWarnings("unchecked")
    @Test
    public void unnest() {
        SetEntry[] set;
        ObjectRelationalTuple<TimeInterval> firstSubTuple, secondSubTuple;
        ObjectRelationalTuple<TimeInterval> first, second;
        
        first = this.result.get(0);
        second = this.result.get(1);
        set = (SetEntry[]) this.inputTuples.get(0).getAttribute(1);
        
        firstSubTuple = (ObjectRelationalTuple<TimeInterval>) set[0].getValue();
        secondSubTuple = (ObjectRelationalTuple<TimeInterval>) set[1].getValue();
        
        assertEquals(first.getAttribute(0), inputTuples.get(0).getAttribute(0));
        assertEquals(first.getAttribute(1), inputTuples.get(0).getAttribute(2));
        assertEquals(first.getAttribute(2), inputTuples.get(0).getAttribute(3));
        assertEquals(first.getAttribute(3), firstSubTuple.getAttribute(0));
        assertEquals(first.getAttribute(4), firstSubTuple.getAttribute(1));       
        
        assertEquals(second.getAttribute(0), inputTuples.get(0).getAttribute(0));
        assertEquals(second.getAttribute(1), inputTuples.get(0).getAttribute(2));
        assertEquals(second.getAttribute(2), inputTuples.get(0).getAttribute(3));
        assertEquals(second.getAttribute(3), secondSubTuple.getAttribute(0));
        assertEquals(second.getAttribute(4), secondSubTuple.getAttribute(1));       
    }
}
