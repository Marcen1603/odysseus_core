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
package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test.unnest.fixtures;


import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author Jendrik Poloczek
 */
public class SimpleUnnestingFixture {
    
    public SDFAttributeList getInputSchema() {
        SDFAttribute[] attrs = {         
            this.getAx(),
            this.getN()
        };
        return new SDFAttributeList(attrs);
    }
    
    public String getNestAttributeName() {
        return "n";
    }
    
    public SDFAttribute getN() {        
        SDFAttribute n = new SDFAttribute("input", "n");
        n.setDatatype(SDFDatatypeFactory.getDatatype("Set"));
        
        SDFAttribute[] subAttrs = 
            {this.getA1(), this.getA2()};
        
        n.setSubattributes(new SDFAttributeList(subAttrs));
        return n;
    }
    
    public SDFAttribute getA1() {
        SDFAttribute A1 = new SDFAttribute("input", "a1");
        A1.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return A1;
    }
    
    public SDFAttribute getA2() {
        SDFAttribute A2 = new SDFAttribute("input", "a2");
        A2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return A2;
    }
    
    public SDFAttribute getAx() {
        SDFAttribute ax = new SDFAttribute("input", "ax");
        ax.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return ax;
    }
}
