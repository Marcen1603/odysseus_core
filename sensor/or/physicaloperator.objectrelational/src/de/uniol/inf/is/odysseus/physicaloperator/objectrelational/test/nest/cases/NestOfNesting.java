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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.NestOfNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class NestOfNesting extends TestCase {

	ArrayList<ObjectRelationalTuple<TimeInterval>> result;
	
	public NestOfNesting() {
	   this.setName("nestOfNesting");
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		
		Factory fixtures;
		NestPO<TimeInterval> nestPO;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;
		
		List<ObjectRelationalTuple<TimeInterval>> inputTuples;

		fixtures = new NestOfNestingFixture();
		inputSchema = fixtures.getInputSchema();
		outputSchema = fixtures.getOutputSchema();
		groupingAttributes = fixtures.getGroupingAttributes();
		nestingAttribute = fixtures.getNestingAttribute();
		inputTuples = fixtures.getInputTuples();

		nestPO = new NestPO<TimeInterval>(
			inputSchema, 
			outputSchema, 
			nestingAttribute, 
			groupingAttributes
		);

	    this.result = 
	    	new ArrayList<ObjectRelationalTuple<TimeInterval>>();
	    
	    for(ObjectRelationalTuple<TimeInterval> tuple : inputTuples) {	
	    	nestPO.processNextTest(tuple, 0);
	    }
	    
	    /*
	     * Sending the isDone signal and returning the lasted evaluated 
	     * partials. Then add those to the result list.
	     */
	    
	    nestPO.allTuplesSent();
	    
	    while(!nestPO.isDone()) {
	    	result.add(nestPO.deliver());
	    }	    
	}

	/**
	 * @TODO implement the test.
	 */
	
	@SuppressWarnings("unchecked")
    @Test 
	public void nestOfNesting() {	
		for(ObjectRelationalTuple<TimeInterval> tuple : this.result) {
		    
			SetEntry<ObjectRelationalTuple<TimeInterval>>[] entries = 
			    (SetEntry[]) tuple.getAttribute(1);
			
			@SuppressWarnings("unused")
            ObjectRelationalTuple t = (ObjectRelationalTuple) entries[0].getValue();		
			//SetEntry[] entries2 = (SetEntry[]) t.getAttribute(0);					
		}
		
		
	}
	
}
