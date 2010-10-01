package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.cases;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.NestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.helper.PartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures.SimpleNestingFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * This test case covers the merging in the update process of a 
 * sweep area of a group. 
 * 
 * start.
 * we got a partial [5,10) with following relational tuples
 * 
 * a1	a2	a3	a4
 * 1    	2	3	4 	[5,10)
 * 1	    4	5	6 	[5,10)
 * 
 * and a partial [0,10) with the following relational tuples
 * 
 * a1	a2	a3	a4
 * 1    	5	6	7	[0,10)
 * 1	    8	9	10	[0,10)
 * 
 * end:
 * if everything works correctly the end partial [5,10) should contain
 * 
 * a1	a2	a3	a4
 * 1    	2	3	4	[5,10)
 * 1    	4	5	6	[5,10)
 * 1	    5	6	7	[5,10)
 * 1	    8	9	10	[5,10)
 * 
 * @author Jendrik Poloczek
 * 
 */
public class Merge extends TestCase {
	
	private NestPO<TimeInterval> nestPO;
	private PartialNest<TimeInterval> result;
	private	List<ObjectRelationalTuple<TimeInterval>> tuples;
	
	private ArrayList<ObjectRelationalTuple<TimeInterval>> inputTuplesPartialA;
	private ArrayList<ObjectRelationalTuple<TimeInterval>> inputTuplesPartialB;
	
	private Method merge;

	public Merge() {
	    this.setName("merge");
	}
	
	@SuppressWarnings("unchecked")
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
		
		this.nestPO = new NestPO<TimeInterval>(
				inputSchema,
				outputSchema,
				nestingAttribute,
				groupingAttributes
		);
		
	    String[] inputDataPartialA = new String[] {
				"1;2;3;4,0;5",
				"1;5;6;7,0;5",
			  };  
	    
		inputTuplesPartialA = 
		    new ArrayList<ObjectRelationalTuple<TimeInterval>>();
		
		String dataAndInterval[];
		String timeInterval[];
		
	    for(int i = 0; i < inputDataPartialA.length; i++) {
		    
		    dataAndInterval = inputDataPartialA[i].split(",");
		    timeInterval = dataAndInterval[1].split(";");
		    
	    	ObjectRelationalTuple<TimeInterval> tuple = 
	    		new ObjectRelationalTuple<TimeInterval>(
					inputSchema,
					dataAndInterval[0],
					';'
				);
	    	
	    	long start = new Long(timeInterval[0]);
	    	long end = new Long(timeInterval[1]);
	    	
	    	tuple.setMetadata(
	    		new TimeInterval(
	    			new PointInTime(start), 
	    			new PointInTime(end)
	    		)
	    	);
	    	inputTuplesPartialA.add(tuple);
	    }
	    
	    String[] inputDataPartialB = new String[] {
				"1;5;6;7,0;10",
				"1;8;9;10,0;10",
			  };  
	    
		inputTuplesPartialB = 
		    new ArrayList<ObjectRelationalTuple<TimeInterval>>();
		
	    for(int i = 0; i < inputDataPartialB.length; i++) {
		    
		    dataAndInterval = inputDataPartialB[i].split(",");
		    timeInterval = dataAndInterval[1].split(";");
		    
	    	ObjectRelationalTuple<TimeInterval> tuple = 
	    		new ObjectRelationalTuple<TimeInterval>(
					inputSchema,
					dataAndInterval[0],
					';'
				);
	    	
	    	long start = new Long(timeInterval[0]);
	    	long end = new Long(timeInterval[1]);
	    	
	    	tuple.setMetadata(
	    		new TimeInterval(
	    			new PointInTime(start), 
	    			new PointInTime(end)
	    		)
	    	);
	    	inputTuplesPartialB.add(tuple);
	    }
	    
		PartialNest<TimeInterval> partialA;
		PartialNest<TimeInterval> partialB;

		partialA = new PartialNest<TimeInterval>(
			inputTuplesPartialA, 
			new TimeInterval(
				new PointInTime(5),
				new PointInTime(10)
			)
		);
		
		partialB = new PartialNest<TimeInterval>(
			inputTuplesPartialB,
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		this.merge = 
			NestPO.class.getDeclaredMethod(
				"merge", 
				PartialNest.class,
				PartialNest.class
			);
		
		this.merge.setAccessible(true);
		
		this.result = 
			(PartialNest<TimeInterval>) 
			this.merge.invoke(this.nestPO, partialA, partialB);			   
	}
	
	@Test public void merge() {
        this.tuples = result.getNest();
        
        assertEquals(this.result.getSize(), new Integer(3));
        
        assertEquals(this.result.getMetadata().getStart(), new PointInTime(5));
        assertEquals(this.result.getMetadata().getEnd(), new PointInTime(10));  
        
        for(ObjectRelationalTuple<TimeInterval> t : this.tuples) {
            assertEquals(t.getMetadata().getStart(), new PointInTime(5));
            assertEquals(t.getMetadata().getEnd(), new PointInTime(10));    
        }
	}
}
