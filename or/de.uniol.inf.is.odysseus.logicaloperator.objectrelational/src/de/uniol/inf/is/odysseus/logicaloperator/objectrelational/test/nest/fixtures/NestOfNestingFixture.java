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
        a2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        
        SDFAttribute[] subAttrs = {this.getA2A1(), this.getA2A2()};
        a2.setSubattributes(new SDFAttributeList(subAttrs));
        return a2;
   }
    
   public SDFAttribute getA2A1() {
        SDFAttribute a2a1 = new SDFAttribute("input", "a2a1");
        a2a1.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return a2a1;
   }
    
   public SDFAttribute getA2A2() {
        SDFAttribute a2a2 = new SDFAttribute("input", "a2a2");
        a2a2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return a2a2;
   }
}
