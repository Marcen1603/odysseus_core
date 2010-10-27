package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.NestOfNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class NestOfNesting extends TestCase {

	ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>> result;
	
	public NestOfNesting() {
	   this.setName("nestOfNesting");
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		
		Factory fixtures;
		ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;
		
		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;

		fixtures = new NestOfNestingFixture();
		inputSchema = fixtures.getInputSchema();
		outputSchema = fixtures.getOutputSchema();
		groupingAttributes = fixtures.getGroupingAttributes();
		nestingAttribute = fixtures.getNestingAttribute();
		inputTuples = fixtures.getInputTuples();

		nestPO = new ObjectTrackingNestPO<ObjectTrackingMetadata<Object>>(
			inputSchema, 
			outputSchema, 
			nestingAttribute, 
			groupingAttributes
		);

	    this.result = 
	    	new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
	    
	    for(MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple : inputTuples) {	
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
		for(MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple : this.result) {
		    
			SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>>[] entries = 
			    (SetEntry[]) tuple.getAttribute(1);
			
			@SuppressWarnings("unused")
            MVRelationalTuple<ObjectTrackingMetadata<Object>> t = 
            	(MVRelationalTuple<ObjectTrackingMetadata<Object>>) entries[0].getValue();		
			//SetEntry[] entries2 = (SetEntry[]) t.getAttribute(0);					
		}
		
		
	}
	
}
