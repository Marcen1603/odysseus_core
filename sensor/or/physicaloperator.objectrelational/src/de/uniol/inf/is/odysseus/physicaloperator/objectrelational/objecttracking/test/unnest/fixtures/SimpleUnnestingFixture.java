package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.unnest.fixtures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
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
    public List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
    	getInputTuples() 
    {
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
        
        SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>>[] set = 
            new SetEntry[2];
        
        inputTuples = 
        	new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
        
        Object valuesForV2_1_1[] = {"v2_s1_a1","v2_s1_a2"};
        Object valuesForV2_1_2[] = {"v2_s2_a1","v2_s2_a2"};
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> t1sub1 = 
            new MVRelationalTuple<ObjectTrackingMetadata<Object>>(
                valuesForV2_1_1
            );
    
        ObjectTrackingMetadata<Object> t1sub1Meta = 
        	new ObjectTrackingMetadata<Object>();
        
        t1sub1Meta.setStart(new PointInTime(0));
        t1sub1Meta.setEnd(new PointInTime(10));
        
        t1sub1.setMetadata(t1sub1Meta);
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> t1sub2 = 
            new MVRelationalTuple<ObjectTrackingMetadata<Object>>(              
                valuesForV2_1_2
            );               
        
        ObjectTrackingMetadata<Object> t1sub2Meta = 
        	new ObjectTrackingMetadata<Object>();
        
        t1sub2Meta.setStart(new PointInTime(5));
        t1sub2Meta.setEnd(new PointInTime(10));
        
        t1sub2.setMetadata(t1sub2Meta);
        
        set[0] = new SetEntry(t1sub1);
        set[1] = new SetEntry(t1sub2);
        
        Object[] t1val = {"v1", set, "v3", "v4"};
        
        MVRelationalTuple<ObjectTrackingMetadata<Object>> t1 = new 
            MVRelationalTuple<ObjectTrackingMetadata<Object>>(        
                t1val
            );
    
        ObjectTrackingMetadata<Object> t1Meta = 
        	new ObjectTrackingMetadata<Object>();
        
        t1Meta.setStart(new PointInTime(0));
        t1Meta.setEnd(new PointInTime(10));
        
        t1.setMetadata(t1Meta);
        
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
