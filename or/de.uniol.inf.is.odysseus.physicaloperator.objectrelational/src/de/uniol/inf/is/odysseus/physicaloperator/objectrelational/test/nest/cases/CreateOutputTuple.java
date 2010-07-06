package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.PartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CreateOutputTuple extends TestCase {

	private List<ObjectRelationalTuple<TimeInterval>> inputTuples;
	private NestPO<TimeInterval> nestPO;
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
		
		this.nestPO = new NestPO<TimeInterval>(
				inputSchema, 
				outputSchema, 
				nestingAttribute, 
				groupingAttributes
		);
		
		this.getGroupingValues = 
			NestPO.class.getDeclaredMethod(
				"getGroupingValues", 
				ObjectRelationalTuple.class
			);
		this.getGroupingValues.setAccessible(true);
		
		this.createOutputTuple = 
			NestPO.class.getDeclaredMethod(
				"createOutputTuple",
				Object[].class,
				PartialNest.class
			);
		this.createOutputTuple.setAccessible(true);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void createTuple() {				
		PartialNest<TimeInterval> p = 
			new PartialNest<TimeInterval>(this.inputTuples.get(0));
		ObjectRelationalTuple<TimeInterval> tuple = null;
		
		p.add(this.inputTuples.get(1));
		
		Object[] gv;
		try {
			gv = (Object[]) this.getGroupingValues.invoke(
			this.nestPO, 
			this.inputTuples.get(0));
			
			tuple = (ObjectRelationalTuple<TimeInterval>) 
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
