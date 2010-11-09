package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.TwoGroupingAttributesNestingFixture;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TwoGroupingAttributeNest extends TestCase {
	
	ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>> result;
	
	public TwoGroupingAttributeNest() {
	    this.setName("nest");
	}
	
	/**
	 * 
	 * Setting up input and output schema and nesting attribute and 
	 * grouping attributes; setting up relational tuples etc.
	 * 
	 * @throws Exception
	 * 
	 */
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

		fixtures = new TwoGroupingAttributesNestingFixture();
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
	
	@Test 
	@SuppressWarnings("unused")
	public void nest() {
	    for( MVRelationalTuple<ObjectTrackingMetadata<Object>> t : this.result) {
	    	
	    }	 
	}
}
