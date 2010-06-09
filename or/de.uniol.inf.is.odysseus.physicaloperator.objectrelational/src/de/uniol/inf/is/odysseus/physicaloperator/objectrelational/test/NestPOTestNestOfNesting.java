package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.SetEntry;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class NestPOTestNestOfNesting {

	ArrayList<ObjectRelationalTuple<TimeInterval>> result;
	
	@Before
	public void setUp() throws Exception {
		
		NestPOTestFixtureFactory fixtures;
		NestPO<TimeInterval> nestPO;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;
		
		List<ObjectRelationalTuple<TimeInterval>> inputTuples;

		fixtures = new NestPOTestFixtureNestOfNesting();
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
	    	System.out.println(tuple);
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

	@SuppressWarnings("unchecked")
    @Test 
	public void testNestOfNesting() {
		System.out.println("output");
		for(ObjectRelationalTuple<TimeInterval> tuple : this.result) {
		    
			SetEntry<ObjectRelationalTuple<TimeInterval>>[] entries = 
			    (SetEntry[]) tuple.getAttribute(1);
			
			ObjectRelationalTuple t = (ObjectRelationalTuple) entries[0].getValue();
			System.out.println(t);
	
			SetEntry[] entries2 = (SetEntry[]) t.getAttribute(0);			
			System.out.println(entries2[0]);
		}
		
		
	}
	
}
