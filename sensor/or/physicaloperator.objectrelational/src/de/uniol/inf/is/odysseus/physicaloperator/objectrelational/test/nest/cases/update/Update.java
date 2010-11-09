package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases.update;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.NestTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * This test covers all overlapping possibilities in 
 * updating the sweep area. Fixture is internal, 
 * NestPOTestFixtureFactory only needed to fake parameters for nestPO.
 * The same two partials are used, but with different 
 * time intervals.
 * 
 * @author Jendrik Poloczek
 * 
 */
public class Update extends TestCase {

	protected NestPO<TimeInterval> nestPO;
	protected NestTISweepArea sa;
	protected ObjectRelationalTuple<TimeInterval> first;
	protected ObjectRelationalTuple<TimeInterval> second;
	
	private Method getGroupingValues;
	protected Method update;

	public Update() {
	    this.setName(
	       "update"
	    );
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		List<ObjectRelationalTuple<TimeInterval>> tuples;
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
		tuples = fixtures.getInputTuples();
		
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
		
		this.update = 
			NestPO.class.getDeclaredMethod(
				"update", 
				NestTISweepArea.class,
				ObjectRelationalTuple.class
			);
		
		this.update.setAccessible(true);
		
		this.first = tuples.get(0);
		this.second = tuples.get(1);
		
		Object[] gv = 
			(Object[]) this.getGroupingValues.invoke(this.nestPO, first);
		
		this.sa = new NestTISweepArea(gv);		
	}
}
