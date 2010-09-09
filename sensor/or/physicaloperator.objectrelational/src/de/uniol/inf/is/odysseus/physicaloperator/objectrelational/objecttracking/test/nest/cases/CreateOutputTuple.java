package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper.ObjectTrackingPartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CreateOutputTuple extends TestCase {

	private List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
	private ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO; 
	private Method getGroupingValues;
	private Method createOutputTuple;
	
	public CreateOutputTuple() {
	    this.setName("createTuple");
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
		
		this.nestPO = new ObjectTrackingNestPO<ObjectTrackingMetadata<Object>>(
				inputSchema, 
				outputSchema, 
				nestingAttribute, 
				groupingAttributes
		);
		
		this.getGroupingValues = 
			ObjectTrackingNestPO.class.getDeclaredMethod(
				"getGroupingValues", 
				MVRelationalTuple.class
			);
		this.getGroupingValues.setAccessible(true);
		
		this.createOutputTuple = 
			ObjectTrackingNestPO.class.getDeclaredMethod(
				"createOutputTuple",
				Object[].class,
				ObjectTrackingPartialNest.class
			);
		this.createOutputTuple.setAccessible(true);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void createTuple() {				
		ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> p = 
			new ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>(this.inputTuples.get(0));
		MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple = null;
		
		p.add(this.inputTuples.get(1));
		
		Object[] gv;
		try {
			gv = (Object[]) this.getGroupingValues.invoke(
			this.nestPO, 
			this.inputTuples.get(0));
			
			tuple = (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
			this.createOutputTuple.invoke(this.nestPO, gv, p);
	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(tuple != null) {
			assertEquals(tuple.getAttribute(0), "1");			
			Object[] nest = tuple.getAttribute(1);
			for(int i = 0; i < nest.length; i++) {
				assertTrue(
					nest[i] instanceof SetEntry
				);
			}
		}
	}
}
