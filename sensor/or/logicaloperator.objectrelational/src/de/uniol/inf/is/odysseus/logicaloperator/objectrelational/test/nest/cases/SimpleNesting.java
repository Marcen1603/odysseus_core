/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.cases;


import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.fixtures.SimpleNestingFixture;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.NestAO;

/**
 * Asserting correct transformation from input to output schema
 * when simply nesting some attributes.
 * 
 * Example: Attribute(Sub attributes)
 * 
 * start (input schema):
 * A1   A2  Ax 
 * 
 * end (output schema):
 * Ax   N(A1,A2)
 * 
 * @author Jendrik Poloczek
 */
public class SimpleNesting extends TestCase {    
    
    private SDFAttributeList outputSchema;
    private NestAO nestAo;
    private Method calcOutputSchema;
    private SimpleNestingFixture fixture;
    
    public SimpleNesting() {
        this.setName("simpleNesting");
    }
    
    @Before
    public void setUp() {
        SDFAttributeList inputSchema;       
        this.fixture = new SimpleNestingFixture();
        inputSchema = this.fixture.getInputSchema();
        
        this.nestAo = new NestAO();
        this.nestAo.setNestAttributeName(this.fixture.getNestAttributeName());
        this.nestAo.setNestingAttributes(this.fixture.getNestingAttributes());
        
        try {
            this.calcOutputSchema = 
                NestAO.class.getDeclaredMethod(
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
                    this.nestAo, 
                    inputSchema
                );
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    
    @Test 
    public void simpleNesting() { 
        SDFAttribute axOut, nOut;
        SDFAttribute[] nestingAttributesIn;
        
        nestingAttributesIn = new SDFAttribute[2];       
        axOut = this.outputSchema.get(0);
        nOut = this.outputSchema.get(1);
     
        assertEquals(axOut, this.fixture.getAx());
        assertEquals(nOut.getDatatype(), SDFDatatypeFactory.getDatatype("Set"));
        
        nestingAttributesIn[0] = fixture.getA1();
        nestingAttributesIn[1] = fixture.getA2();
        
        assertEquals(
           nOut.getSubattributes(), 
           new SDFAttributeList(nestingAttributesIn)
        );
    }
}
