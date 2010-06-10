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
