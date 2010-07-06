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
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Testing a total merge with time intervals are exactly the 
 * same. With two tuples. Using the parameters from 
 * 
 * start:
 * 	
 * a1	a2	a3	a4
 * 1	2	3	4 	[0,10)
 * 1	5	6	7	[0,10)
 * 2	2	3	4	[15,30)
 * 2	5	6	7	[20,30)
 * 
 * inputSchema is a1 ... a4 are strings
 * outputSchema is a1, n1. a1 is string 
 * 				and nesting got sub attributes a2, a3, a4. 
 * 
 * groupingAttributes contains a1.
 * nestingAttribute is n1
 * 
 * outcome: 
 * 
 * a1	n1
 * 1	[2	3	4 [0,10)][5	6	7	[0,10)] [0,10)
 * 2	[2	3	4 [15,30)] [15,20)
 * 2	[2	3	4 [20,30)][2	5	6	7 [20,30)] [20,30)
 * 
 * @author Jendrik Poloczek
 */

public class Nest extends TestCase {

	ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>> result;
	
	public Nest() {
	    this.setName("nest");
	}
	
	/**
	 * Setting up input and output schema and nesting attribute and 
	 * grouping attributes; setting up relational tuples etc.
	 * 
	 * @throws Exception
	 */
	
	@Before
	public void setUp() throws Exception {
		
		Factory fixtures;
		ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;
		
		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;

		fixtures = new SimpleNestingFixture();
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
	 * The grouping values will be asserted.
	 */
	
	@SuppressWarnings("unchecked")
    @Test public void nest() {	    
		MVRelationalTuple<ObjectTrackingMetadata<Object>> checkTuple = result.get(0);
	    assertEquals(checkTuple.getAttribute(0),"1");
	    checkTuple = result.get(1);
	    assertEquals(checkTuple.getAttribute(0),"2");
	    checkTuple = result.get(2);
	    assertEquals(checkTuple.getAttribute(0),"2");	    
	    
      
        Object nest[] = checkTuple.getAttribute(1);
                
        MVRelationalTuple<ObjectTrackingMetadata<Object>> nest0 = 
            ((SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>>) nest[0]).getValue();
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> nest1 = 
            ((SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>>) nest[1]).getValue();
                
        assertEquals(nest0.getAttributeCount(), 3);
        assertEquals(nest0.getAttribute(0), "2");
        assertEquals(nest0.getAttribute(1), "3");
        assertEquals(nest0.getAttribute(2), "4");
        assertEquals(nest1.getAttributeCount(), 3);
        assertEquals(nest1.getAttribute(0), "5");
        assertEquals(nest1.getAttribute(1), "6");
        assertEquals(nest1.getAttribute(2), "7");
        
        checkTuple = result.get(1);
        nest = checkTuple.getAttribute(1);
        assertEquals(nest0.getAttributeCount(), 3);
        assertEquals(nest0.getAttribute(0), "2");
        assertEquals(nest0.getAttribute(1), "3");
        assertEquals(nest0.getAttribute(2), "4");
        
        checkTuple = result.get(2);
        nest = checkTuple.getAttribute(1);
        assertEquals(nest0.getAttributeCount(), 3);
        assertEquals(nest0.getAttribute(0), "2");
        assertEquals(nest0.getAttribute(1), "3");
        assertEquals(nest0.getAttribute(2), "4");
        assertEquals(nest1.getAttributeCount(), 3);     
        assertEquals(nest1.getAttribute(0), "5");
        assertEquals(nest1.getAttribute(1), "6");
        assertEquals(nest1.getAttribute(2), "7");
	}
}