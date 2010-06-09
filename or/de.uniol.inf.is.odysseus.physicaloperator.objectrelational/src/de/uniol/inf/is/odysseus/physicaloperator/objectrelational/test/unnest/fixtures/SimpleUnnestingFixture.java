package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.unnest.fixtures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.SetEntry;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class SimpleUnnestingFixture 
    implements Factory {

    private SDFAttributeList getInputNestSchema() {
        SDFAttribute inputAttributes[];
        
        inputAttributes = new SDFAttribute[2];
        
        SDFAttribute a2sub1 = new SDFAttribute("input", "a2sub1");
        a2sub1.setDatatype(SDFDatatypeFactory.getDatatype("String"));
        
        inputAttributes[0] = a2sub1;
        
        SDFAttribute a2sub2 = new SDFAttribute("input", "a2sub2");
        a2sub2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
    
        inputAttributes[1] = a2sub2;
        
        return new SDFAttributeList(inputAttributes);
    }
    
    @Override
    public SDFAttributeList getInputSchema() {
        SDFAttributeList inputSchema;
        SDFAttribute[] inputAttributes;
        
        inputAttributes = new SDFAttribute[4];
        
        inputAttributes[0] = new SDFAttribute("input","a1");
        inputAttributes[0].setDatatype(
            SDFDatatypeFactory.getDatatype("String")
        );       
        
        inputAttributes[1] = this.getNestingAttribute();
 
        inputAttributes[1].setSubattributes(this.getInputNestSchema());
        
        inputAttributes[2] = new SDFAttribute("input","a3");
        inputAttributes[2].setDatatype(
            SDFDatatypeFactory.getDatatype("String")
        );
        
        inputAttributes[3] = new SDFAttribute("input","a4");
        inputAttributes[3].setDatatype(
            SDFDatatypeFactory.getDatatype("String")
        );
        
        inputSchema = new SDFAttributeList(inputAttributes);
        
        return inputSchema;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ObjectRelationalTuple<TimeInterval>> getInputTuples() {
        
        List<ObjectRelationalTuple<TimeInterval>> inputTuples;
        
        SetEntry<ObjectRelationalTuple<TimeInterval>>[] set = 
            new SetEntry[2];
        
        inputTuples = new ArrayList<ObjectRelationalTuple<TimeInterval>>();
        
        Object valuesForV2_1_1[] = {"v2_s1_a1","v2_s1_a2"};
        Object valuesForV2_1_2[] = {"v2_s2_a1","v2_s2_a2"};
        
        ObjectRelationalTuple<TimeInterval> t1sub1 = 
            new ObjectRelationalTuple<TimeInterval>(
                this.getInputNestSchema(),
                valuesForV2_1_1
            );
    
        t1sub1.setMetadata(
            new TimeInterval(
                new PointInTime(0),
                new PointInTime(10)
            )
        );       
        
        ObjectRelationalTuple<TimeInterval> t1sub2 = 
            new ObjectRelationalTuple<TimeInterval>(
                this.getInputNestSchema(),
                valuesForV2_1_2
            );
        
        t1sub2.setMetadata(
            new TimeInterval(
                new PointInTime(5),
                new PointInTime(10)
            )
        );
        
        set[0] = new SetEntry(t1sub1);
        set[1] = new SetEntry(t1sub2);
        
        Object[] t1val = {"v1", set, "v3", "v4"};
        
        ObjectRelationalTuple<TimeInterval> t1 = new 
            ObjectRelationalTuple<TimeInterval>(
                this.getInputSchema(),
                t1val
            );
    
        t1.setMetadata(
            new TimeInterval(
                new PointInTime(0),
                new PointInTime(10)
            )
        );
        
        inputTuples.add(t1);
        
        return inputTuples;
    }

    @Override
    public SDFAttribute getNestingAttribute() {
        SDFAttribute nestAttr = new SDFAttribute("input", "n1");
        nestAttr.setDatatype(
            SDFDatatypeFactory.getDatatype("Set")
        );
        nestAttr.setSubattributes(this.getInputNestSchema());
        return nestAttr;
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        SDFAttributeList outputSchema;
        SDFAttribute outputAttributes[];
        
        outputAttributes = new SDFAttribute[5];
        
        outputAttributes[0] = new SDFAttribute("output","a1");
        outputAttributes[0].setDatatype(
                SDFDatatypeFactory.getDatatype("String")
        );
        
        outputAttributes[1] = new SDFAttribute("output","a3");
        outputAttributes[1].setDatatype(
                SDFDatatypeFactory.getDatatype("String")
        );

        outputAttributes[2] = new SDFAttribute("output","a4");
        outputAttributes[2].setDatatype(
                SDFDatatypeFactory.getDatatype("String")
        );
        
        outputAttributes[3] = new SDFAttribute("output", "a2s1");
        outputAttributes[3].setDatatype(
                SDFDatatypeFactory.getDatatype("String")
        );
        
        outputAttributes[4] = new SDFAttribute("output", "a2s2");
        outputAttributes[4].setDatatype(
                SDFDatatypeFactory.getDatatype("String")
        );
                
        outputSchema = new SDFAttributeList(outputAttributes);
        return outputSchema;
    }
}
