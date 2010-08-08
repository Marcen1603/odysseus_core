package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.cases;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.ObjectTrackingNestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.helper.ObjectTrackingPartialNest;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures.SimpleNestingFixture;
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
 * 1	    5	6	7 	[5,10)
 * 
 * and a partial [0,10) with the following relational tuples
 * 
 * a1	a2	a3	a4
 * 1    	5	6	7	[0,10)
 * 
 * end:
 * if everything works correctly the end partial [5,10) should contain
 * 
 * a1	a2	a3	a4
 * 1    	2	3	4	[5,10)
 * 1    	5	6	7	[5,10)
 * 1	    5	6	7	[5,10)
 * 
 * @author Jendrik Poloczek
 * 
 */
public class Merge extends TestCase {
	
	private ObjectTrackingNestPO<ObjectTrackingMetadata<Object>> nestPO;
	private ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> result;
	private	List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> tuples;	
	private ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuplesPartialA;
	private ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuplesPartialB;
	
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
		
		this.nestPO = 
			new ObjectTrackingNestPO<ObjectTrackingMetadata<Object>>(
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
		    new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
		
		String dataAndInterval[];
		String timeInterval[];
		
	    for(int i = 0; i < inputDataPartialA.length; i++) {
		    
		    dataAndInterval = inputDataPartialA[i].split(",");
		    timeInterval = dataAndInterval[1].split(";");
		    
	    	MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple = 
	    		new MVRelationalTuple<ObjectTrackingMetadata<Object>>(
					inputSchema,
					dataAndInterval[0],
					';'
				);
	    	
	    	long start = new Long(timeInterval[0]);
	    	long end = new Long(timeInterval[1]);
	    	
	    	ObjectTrackingMetadata<Object> meta =  
	    		new ObjectTrackingMetadata<Object>();
	    	
	    	meta.setStart(new PointInTime(start));
	    	meta.setEnd(new PointInTime(end));
	    	
	    	tuple.setMetadata(meta);
	    	
	    	inputTuplesPartialA.add(tuple);
	    }
	    
	    String[] inputDataPartialB = new String[] {
				"1;5;6;7,0;10"
			  };  
	    
		inputTuplesPartialB = 
		    new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
		
	    for(int i = 0; i < inputDataPartialB.length; i++) {
		    
		    dataAndInterval = inputDataPartialB[i].split(",");
		    timeInterval = dataAndInterval[1].split(";");
		    
	    	MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple = 
	    		new MVRelationalTuple<ObjectTrackingMetadata<Object>>(
					inputSchema,
					dataAndInterval[0],
					';'
				);
	    	
	    	long start = new Long(timeInterval[0]);
	    	long end = new Long(timeInterval[1]);
	    	
	    	ObjectTrackingMetadata<Object> meta = 
	    		new ObjectTrackingMetadata<Object>();
	    	
	    	meta.setStart(new PointInTime(start));
	    	meta.setEnd(new PointInTime(end));
	    	
	    	tuple.setMetadata(meta);
	    	
	    	inputTuplesPartialB.add(tuple);
	    }
	    
		ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>> partialA;
		MVRelationalTuple<ObjectTrackingMetadata<Object>> tupleB;

		ObjectTrackingMetadata<Object> metaPartialA = 
			new ObjectTrackingMetadata<Object>();
		
		metaPartialA.setStart(new PointInTime(5));
		metaPartialA.setEnd(new PointInTime(10));
		
		partialA = 
			new ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>(
				inputTuplesPartialA, 
				metaPartialA
			);
		
		ObjectTrackingMetadata<Object> metaPartialB = 
			new ObjectTrackingMetadata<Object>();
		
		metaPartialB.setStart(new PointInTime(0));
		metaPartialB.setEnd(new PointInTime(10));
		
		tupleB = 
			inputTuplesPartialB.get(0);
			
		this.merge = 
			ObjectTrackingNestPO.class.getDeclaredMethod(
				"merge", 
				ObjectTrackingPartialNest.class,
				MVRelationalTuple.class
			);
		
		this.merge.setAccessible(true);
		
		this.result = 
			(ObjectTrackingPartialNest<ObjectTrackingMetadata<Object>>) 
			this.merge.invoke(this.nestPO, partialA, tupleB);			   
	}
	
	@Test public void merge() {
        this.tuples = result.getNest();        
        
        assertEquals(this.result.getSize(), new Integer(3));
        
        assertEquals(this.result.getMetadata().getStart(), new PointInTime(5));
        assertEquals(this.result.getMetadata().getEnd(), new PointInTime(10));  
        
        for(MVRelationalTuple<ObjectTrackingMetadata<Object>> t : this.tuples) {
            assertEquals(t.getMetadata().getStart(), new PointInTime(5));
            assertEquals(t.getMetadata().getEnd(), new PointInTime(10));             
        }
	}
}
