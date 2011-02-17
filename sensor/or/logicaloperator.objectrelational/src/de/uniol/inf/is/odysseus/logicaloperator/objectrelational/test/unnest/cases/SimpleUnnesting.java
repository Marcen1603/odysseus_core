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
    logicaloperator.objectrelational.test.unnest.cases;


import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.unnest.fixtures.SimpleUnnestingFixture;

import de.uniol.inf.is.odysseus.logicaloperator.objectrelational.UnnestAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Asserting correct transformation from input to output schema
 * when simply unnesting a nest
 * 
 * Example: Attribute(Sub attributes)
 * 
 * start (input schema):
 * Ax N(A1,A2) 
 * 
 * end (output schema):
 * Ax A1    A2
 * 
 * @author Jendrik Poloczek
 */
public class SimpleUnnesting extends TestCase {

    private SDFAttributeList outputSchema;
    private UnnestAO unnestAo;
    private Method calcOutputSchema;
    private SimpleUnnestingFixture fixture;
    
    public SimpleUnnesting() {
        this.setName("simpleUnnesting");
    }
    
    @Before
    public void setUp() throws Exception {
        SDFAttributeList inputSchema;
        
        this.fixture = new SimpleUnnestingFixture();
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
    public void simpleUnnesting() {
        assertEquals(this.outputSchema.get(0), this.fixture.getAx());
        assertEquals(this.outputSchema.get(1), this.fixture.getA1());
        assertEquals(this.outputSchema.get(2), this.fixture.getA2());
    }    
}
