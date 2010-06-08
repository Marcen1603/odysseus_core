package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.PartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.SetEntry;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class NestPOTestCreateOutputTuple {

	private List<ObjectRelationalTuple<TimeInterval>> inputTuples;
	private NestPO<TimeInterval> nestPO;
	private Method getGroupingValues;
	private Method createOutputTuple;
	
	@Before
	public void setUp() throws Exception {
		
		NestPOTestFixtureFactory fixtures;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;		
		SDFAttributeList groupingAttributes;
		SDFAttribute nestingAttribute;

		fixtures = new NestPOTestFixtureSimpleNesting();
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
