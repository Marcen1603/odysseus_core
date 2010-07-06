package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases.update;

import java.lang.reflect.Method;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper.ObjectTrackingNestTISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.SimpleNestingFixture;
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

	protected ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO;
	protected ObjectTrackingNestTISweepArea<ObjectTrackingMetadata<Object>> sa;
	protected MVRelationalTuple<ObjectTrackingMetadata<Object>> first;
	protected MVRelationalTuple<ObjectTrackingMetadata<Object>> second;
	
	private Method getGroupingValues;
	protected Method update;

	public Update() {
	    this.setName(
	       "update"
	    );
	}
	
	@Before
	public void setUp() throws Exception {
		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> tuples;
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
		
		this.nestPO = 
			new ObjectTrackingNestPO<ObjectTrackingMetadata<Object>>(
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
		
		this.update = 
			ObjectTrackingNestPO.class.getDeclaredMethod(
				"update", 
				ObjectTrackingNestTISweepArea.class,
				MVRelationalTuple.class
			);
		
		this.update.setAccessible(true);
		
		this.first = tuples.get(0);
		this.second = tuples.get(1);
		
		Object[] gv = 
			(Object[]) this.getGroupingValues.invoke(this.nestPO, first);
		
		this.sa = new ObjectTrackingNestTISweepArea<
			ObjectTrackingMetadata<Object>
		>(gv);		
	}
}
