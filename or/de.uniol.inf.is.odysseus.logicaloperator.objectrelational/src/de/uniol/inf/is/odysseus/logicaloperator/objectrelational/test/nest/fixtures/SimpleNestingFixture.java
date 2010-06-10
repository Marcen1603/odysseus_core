package de.uniol.inf.is.odysseus.
    logicaloperator.objectrelational.test.nest.fixtures;


import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author Jendrik Poloczek
 */
public class SimpleNestingFixture {
    
    public SDFAttributeList getInputSchema() {
        SDFAttribute[] attrs = {
            this.getA1(), 
            this.getA2(), 
            this.getAx()
        };
        return new SDFAttributeList(attrs);
    }
    
    public SDFAttributeList getNestingAttributes() {
        SDFAttribute[] attrs = {this.getA1(), this.getA2()};
        return new SDFAttributeList(attrs);
    }
    
    public String getNestAttributeName() {
        return "n";
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
