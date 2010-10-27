package de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases;


import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.fixtures.NestOfNestingFixture;

import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.NestAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * Asserting correct transformation from input 
 * schema to output schema with previous nest. 
 * 
 * Example: Attribute (Sub attributes)
 * 
 * start (input schema):
 * A1   A2(A21, A22)  Ax (input schema)
 * 
 * end (output schema): 
 * Ax   N (A1,  A2(A21,A22))
 * 
 * @author Jendrik Poloczek
 */
public class NestOfNesting extends TestCase  {    
    
    private Method calcOutputSchema;
    private NestAO nestAo;
    private SDFAttributeList inputSchema, outputSchema;
    private NestOfNestingFixture fixture;
    
    public NestOfNesting() {
        this.setName("nestOfNesting");
    }
    
    @Override
	@Before
    public void setUp() throws Exception {               
        this.nestAo = new NestAO();
        this.fixture = new NestOfNestingFixture();
        this.inputSchema = fixture.getInputSchema();
                
        this.nestAo.setNestAttributeName(this.fixture.getNestAttributeName());
        this.nestAo.setNestingAttributes(this.fixture.getNestingAttributes());
        
        this.calcOutputSchema = 
            NestAO.class.getDeclaredMethod(
                "calcOutputSchema", 
                SDFAttributeList.class
            );
        this.calcOutputSchema.setAccessible(true);
        this.outputSchema =
            (SDFAttributeList) this.calcOutputSchema.invoke(
                this.nestAo, 
                this.inputSchema
            );
    }

    @Test
    public void nestOfNesting() {                      
        SDFAttributeList firstSubAttrOut;
        SDFAttributeList firstSubSubAttrOut;
        SDFAttribute axOut, nOut;
        SDFAttribute[] firstSubAttrIn;
                
        firstSubAttrIn = new SDFAttribute[2];
        
        assertEquals(this.outputSchema.size(), 2);
        
        axOut = this.outputSchema.get(0);
        nOut = this.outputSchema.get(1);       
        
        assertEquals(axOut, this.fixture.getAx());
        assertEquals(nOut.getDatatype(), SDFDatatypeFactory.getDatatype("Set"));
        
        firstSubAttrIn[0] = this.fixture.getA1();
        firstSubAttrIn[1] = this.fixture.getA2();
        
        firstSubAttrOut = nOut.getSubattributes();        
        assertEquals(firstSubAttrOut, new SDFAttributeList(firstSubAttrIn));
              
        firstSubSubAttrOut = firstSubAttrOut.get(1).getSubattributes();
        
        assertEquals(firstSubSubAttrOut.get(0), this.fixture.getA2A1());
        assertEquals(firstSubSubAttrOut.get(1), this.fixture.getA2A2());
    }
}
