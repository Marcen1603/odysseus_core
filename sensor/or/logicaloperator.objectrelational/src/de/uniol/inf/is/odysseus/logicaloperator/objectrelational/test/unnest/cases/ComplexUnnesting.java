package de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.cases;


import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.fixtures.ComplexUnnestingFixture;

import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.UnnestAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Asserting correct transformation from input schema to output
 * schema when unnesting a complex nest (with more internal nests)
 * 
 * Example: Attribute(Sub attributes)
 * 
 * start (input schema):
 * Ax   N(A1, A2(A21,A22))
 * 
 * end (output schema):
 * Ax   A1  A2(A21, A22)
 * 
 * @author Jendrik Poloczek
 */
public class ComplexUnnesting extends TestCase {

    private SDFAttributeList outputSchema;
    private UnnestAO unnestAo;
    private Method calcOutputSchema;
    private ComplexUnnestingFixture fixture;
    
    public ComplexUnnesting() {
        this.setName("complexUnnesting");
    }
    
    @Before
    public void setUp() throws Exception {
        SDFAttributeList inputSchema;
        
        this.fixture = new ComplexUnnestingFixture();
        inputSchema = this.fixture.getInputSchema();
        
        this.unnestAo = new UnnestAO();
        this.unnestAo.setNestAttribute(this.fixture.getN());  
        
        try {
            this.calcOutputSchema = 
                UnnestAO.class.getDeclaredMethod(
                    "calcOutputSchema", 
                    SDFAttributeList.class
                );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.calcOutputSchema.setAccessible(true);
        try {
            this.outputSchema =
                (SDFAttributeList) this.calcOutputSchema.invoke(
                    this.unnestAo, 
                    inputSchema
                );
        } catch (Exception e) {
            e.printStackTrace();
        }                 
    }

    @Test
    public void complexUnnesting() {            
        SDFAttribute axOut, a1Out, a2Out;
        
        assertEquals(this.outputSchema.size(), 3);
        
        axOut = this.outputSchema.get(0);
        a1Out = this.outputSchema.get(1);
        a2Out = this.outputSchema.get(2);
        
        assertEquals(axOut, this.fixture.getAx());
        assertEquals(a1Out, this.fixture.getA1());
        assertEquals(a2Out, this.fixture.getA2());
        
        assertEquals(a2Out.getSubattributes().get(0), this.fixture.getA2A1());
        assertEquals(a2Out.getSubattributes().get(1), this.fixture.getA2A2());
    }
    
}
