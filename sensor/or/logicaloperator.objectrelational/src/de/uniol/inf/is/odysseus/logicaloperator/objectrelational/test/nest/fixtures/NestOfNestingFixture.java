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
    logicaloperator.objectrelational.test.nest.fixtures;


import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author Jendrik Poloczek
 */
public class NestOfNestingFixture extends SimpleNestingFixture {
 
   public SDFAttribute getA2() {
        SDFAttribute a2 = new SDFAttribute("input", "a2");
        a2.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        
        SDFAttribute[] subAttrs = {this.getA2A1(), this.getA2A2()};
        a2.setSubattributes(new SDFAttributeList(subAttrs));
        return a2;
   }
    
   public SDFAttribute getA2A1() {
        SDFAttribute a2a1 = new SDFAttribute("input", "a2a1");
        a2a1.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        return a2a1;
   }
    
   public SDFAttribute getA2A2() {
        SDFAttribute a2a2 = new SDFAttribute("input", "a2a2");
        a2a2.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        return a2a2;
   }
}
