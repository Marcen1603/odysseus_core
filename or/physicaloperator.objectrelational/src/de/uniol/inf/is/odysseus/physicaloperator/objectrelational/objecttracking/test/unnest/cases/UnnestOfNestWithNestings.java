package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.unnest.cases;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.ObjectTrackingUnnestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.
	objecttracking.test.unnest.fixtures.UnnestOfNestWithNestingsFixture;

import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnnestOfNestWithNestings extends TestCase {

    private List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
    private List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> result;
    
    public UnnestOfNestWithNestings() {
        this.setName("unnest");
    }
    
    @Before
    public void setUp() throws Exception {
        
        Factory fixtures;
        ObjectTrackingUnnestPO<ObjectTrackingMetadata<Object>> unnestPO;
        
        SDFAttributeList inputSchema;
        SDFAttributeList outputSchema;      
        SDFAttribute nestingAttribute;
        
        fixtures = new UnnestOfNestWithNestingsFixture();
        inputSchema = fixtures.getInputSchema();
        outputSchema = fixtures.getOutputSchema();
        nestingAttribute = fixtures.getNestingAttribute();
        this.inputTuples = fixtures.getInputTuples();

        unnestPO = new ObjectTrackingUnnestPO<ObjectTrackingMetadata<Object>>(
            inputSchema, 
            outputSchema, 
            nestingAttribute      
        );

        this.result = 
            new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
        
        for(MVRelationalTuple<ObjectTrackingMetadata<Object>> tuple : 
        	this.inputTuples
        ) {
            unnestPO.processNextTest(tuple, 0);
        }
        
        while(!unnestPO.isDone()) {
            result.add(unnestPO.deliver());
        }               
    }

    @SuppressWarnings("unchecked")
    @Test
    public void unnest() {        
        SetEntry[] firstSetIn, firstSetOut;
        SetEntry[] firstSubSubSetIn;
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> first, second;
        MVRelationalTuple<ObjectTrackingMetadata<Object>> firstSubTupleIn;
        MVRelationalTuple<ObjectTrackingMetadata<Object>> firstSubTupleOut; 
        MVRelationalTuple<ObjectTrackingMetadata<Object>> firstSubSubTupleIn;
        MVRelationalTuple<ObjectTrackingMetadata<Object>> secondSubTuple;
               
        first = this.result.get(0);
        second = this.result.get(1);
        
        firstSetIn = (SetEntry[]) this.inputTuples.get(0).getAttribute(1);
        firstSetOut = (SetEntry[]) first.getAttribute(4);
        
        firstSubTupleIn = 
            (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
            	firstSetIn[0].getValue();
        
        firstSubTupleOut = 
            (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
            	firstSetOut[0].getValue();
        
        secondSubTuple = 
            (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
            	firstSetIn[1].getValue();
        
        firstSubSubSetIn = (SetEntry[]) firstSubTupleIn.getAttribute(1);
        firstSubSubTupleIn = (MVRelationalTuple<ObjectTrackingMetadata<Object>>) 
            firstSubSubSetIn[0].getValue();        
        
        assertEquals(first.getAttribute(0), inputTuples.get(0).getAttribute(0));
        assertEquals(first.getAttribute(1), inputTuples.get(0).getAttribute(2));
        assertEquals(first.getAttribute(2), inputTuples.get(0).getAttribute(3));
        assertEquals(first.getAttribute(3), firstSubTupleIn.getAttribute(0));        
        
        assertEquals(firstSubSubTupleIn, firstSubTupleOut);
        
        assertEquals(second.getAttribute(0), inputTuples.get(0).getAttribute(0));
        assertEquals(second.getAttribute(1), inputTuples.get(0).getAttribute(2));
        assertEquals(second.getAttribute(2), inputTuples.get(0).getAttribute(3));
        assertEquals(second.getAttribute(3), secondSubTuple.getAttribute(0));      
    }
}