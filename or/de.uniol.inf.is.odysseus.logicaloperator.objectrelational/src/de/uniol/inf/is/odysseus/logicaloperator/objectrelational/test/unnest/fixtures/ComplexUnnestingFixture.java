package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.test.unnest.fixtures;


import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class ComplexUnnestingFixture extends SimpleUnnestingFixture {
    
    public SDFAttributeList getInputSchema() {
        SDFAttribute[] attrs = {         
            this.getAx(),
            this.getN()
        };
        return new SDFAttributeList(attrs);
    }
    
    public SDFAttribute getA2() {
        SDFAttribute A2 = new SDFAttribute("input", "a2");
        A2.setDatatype(SDFDatatypeFactory.getDatatype("Set"));
        return A2;
    }
    
    public SDFAttribute getA2A1() {
        SDFAttribute A2A1 = new SDFAttribute("input", "a2a1");
        A2A1.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return A2A1;
    }
    
    public SDFAttribute getA2A2() {
        SDFAttribute A2A2 = new SDFAttribute("input", "a2a2");
        A2A2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        return A2A2;
    }
}
