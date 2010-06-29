package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * tests if both tuples get the same group id.
 * 
 * start:
 * 
 * a1	a2	a3	a4
 * 1    	2	3	4 	[0,10)
 * 1	    5	6	7	[0,10)
 * 
 * inputSchema is a1 ... a4 are strings
 * outputSchema is a1, n1. a1 is string 
 * 				and nesting got sub attributes a2, a3, a4. 
 * 
 * groupingAttributes contains a1.
 * nestingAttribute is n1
 * 
 * @author Jendrik Poloczek
 * 
 */
public class GroupId extends TestCase {

	private List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
	private ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO;
	private Method getGroupId;
	
	public GroupId() {
	    this.setName("groupTheInputTuples");
	}
	
	@Test
	public void groupTheInputTuples() {
		try {
			assertEquals(
				this.getGroupId.invoke(this.nestPO, inputTuples.get(0)),
				this.getGroupId.invoke(this.nestPO, inputTuples.get(1))
			);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		
		Factory fixtures;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;

		fixtures = new SimpleNestingFixture();
		inputSchema = fixtures.getInputSchema();
		outputSchema = fixtures.getOutputSchema();
		groupingAttributes = fixtures.getGroupingAttributes();
		nestingAttribute = fixtures.getNestingAttribute();
		inputTuples = fixtures.getInputTuples();
		
		this.nestPO = 
			new ObjectTrackingNestPO<ObjectTrackingMetadata<Object>>(
				inputSchema, 
				outputSchema, 
				nestingAttribute, 
				groupingAttributes
		);
		
		this.getGroupId = 
			ObjectTrackingNestPO.class.getDeclaredMethod(
				"getGroupId", 
				MVRelationalTuple.class
			);
		
		this.getGroupId.setAccessible(true);
	}
}
