package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.cases;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.SetEntry;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.UnnestPO;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.fixtures.Factory;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.fixtures.UnnestOfNestWithNestingsFixture;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class UnnestOfNestWithNestings extends TestCase {

    private List<ObjectRelationalTuple<TimeInterval>> inputTuples;
    private List<ObjectRelationalTuple<TimeInterval>> result;
    
    public UnnestOfNestWithNestings() {
        this.setName("unnest");
    }
    
    @Before
    public void setUp() throws Exception {
        
        Factory fixtures;
        UnnestPO<TimeInterval> unnestPO;
        
        SDFAttributeList inputSchema;
        SDFAttributeList outputSchema;      
        SDFAttribute nestingAttribute;
        
        fixtures = new UnnestOfNestWithNestingsFixture();
        inputSchema = fixtures.getInputSchema();
        outputSchema = fixtures.getOutputSchema();
        nestingAttribute = fixtures.getNestingAttribute();
        this.inputTuples = fixtures.getInputTuples();

        unnestPO = new UnnestPO<TimeInterval>(
            inputSchema, 
            outputSchema, 
            nestingAttribute      
        );

        this.result = 
            new ArrayList<ObjectRelationalTuple<TimeInterval>>();
        
        for(ObjectRelationalTuple<TimeInterval> tuple : this.inputTuples) {
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
        
        ObjectRelationalTuple<TimeInterval> first, second;
        ObjectRelationalTuple<TimeInterval> firstSubTupleIn;
        ObjectRelationalTuple<TimeInterval> firstSubTupleOut; 
        ObjectRelationalTuple<TimeInterval> firstSubSubTupleIn;
        ObjectRelationalTuple<TimeInterval> secondSubTuple;
               
        first = this.result.get(0);
        second = this.result.get(1);
        
        firstSetIn = (SetEntry[]) this.inputTuples.get(0).getAttribute(1);
        firstSetOut = (SetEntry[]) first.getAttribute(4);
        
        firstSubTupleIn = 
            (ObjectRelationalTuple<TimeInterval>) firstSetIn[0].getValue();
        
        firstSubTupleOut = 
            (ObjectRelationalTuple<TimeInterval>) firstSetOut[0].getValue();
        
        secondSubTuple = 
            (ObjectRelationalTuple<TimeInterval>) firstSetIn[1].getValue();
        
        firstSubSubSetIn = (SetEntry[]) firstSubTupleIn.getAttribute(1);
        firstSubSubTupleIn = (ObjectRelationalTuple<TimeInterval>) 
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
