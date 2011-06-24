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
package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.unnest.fixtures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class UnnestOfNestWithNestingsFixture 
    implements Factory {

    private SDFAttributeList getInputNestSchema() {
        SDFAttribute inputAttributes[];
        
        inputAttributes = new SDFAttribute[2];
        
        SDFAttribute a2sub1 = new SDFAttribute("input", "a2sub1");
        a2sub1.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        
        inputAttributes[0] = a2sub1;
        
        SDFAttribute a2sub2 = new SDFAttribute("input", "a2sub2");
        a2sub2.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Set"));
        a2sub2.setSubattributes(this.getInputNestInNestSchema());
        
        inputAttributes[1] = a2sub2;
        
        return new SDFAttributeList(inputAttributes);
    }

    private SDFAttributeList getInputNestInNestSchema() {
        SDFAttribute inputAttributes[];
        
        inputAttributes = new SDFAttribute[2];
        
        SDFAttribute a2suba1 = new SDFAttribute("input", "a2suba1");
        a2suba1.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        
        inputAttributes[0] = a2suba1;
        
        SDFAttribute a2suba2 = new SDFAttribute("input", "a2suba2");
        a2suba2.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("String"));
        
        inputAttributes[1] = a2suba2;
        
        return new SDFAttributeList(inputAttributes);
    }
    
    @Override
    public SDFAttributeList getInputSchema() {
        SDFAttributeList inputSchema;
        SDFAttribute[] inputAttributes;
        
        inputAttributes = new SDFAttribute[4];
        
        inputAttributes[0] = new SDFAttribute("input","a1");
        inputAttributes[0].setDatatype(
            SDFDatatypeFactory.createAndReturnDatatype("String")
        );       
        
        inputAttributes[1] = this.getNestingAttribute();
 
        inputAttributes[1].setSubattributes(this.getInputNestSchema());
        
        inputAttributes[2] = new SDFAttribute("input","a3");
        inputAttributes[2].setDatatype(
            SDFDatatypeFactory.createAndReturnDatatype("String")
        );
        
        inputAttributes[3] = new SDFAttribute("input","a4");
        inputAttributes[3].setDatatype(
            SDFDatatypeFactory.createAndReturnDatatype("String")
        );
        
        inputSchema = new SDFAttributeList(inputAttributes);
        
        return inputSchema;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> 
    	getInputTuples() {
        
        List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
        MVRelationalTuple<ObjectTrackingMetadata<Object>> a2suba1subTuple;

        SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>>[] 
        set, subset1, subset2;
    
        set = new SetEntry[2];
        subset1 = new SetEntry[1];
        subset2 = new SetEntry[1];
        
        Object valuesForA2suba1Tuple[] = {"v2_s1_a2_s1_a1", "v2_s1_a2_s1_a2"};
        
        a2suba1subTuple = 
        	new MVRelationalTuple<ObjectTrackingMetadata<Object>>(               
                valuesForA2suba1Tuple        
        	);
        
        ObjectTrackingMetadata<Object> a2suba1subTupleMeta = 
        	new ObjectTrackingMetadata<Object>();
        
        a2suba1subTupleMeta.setStart(new PointInTime(5));
        a2suba1subTupleMeta.setStart(new PointInTime(10));      
        
        a2suba1subTuple.setMetadata(a2suba1subTupleMeta);
        
        subset1[0] = new SetEntry(a2suba1subTuple);
        
        inputTuples = 
        	new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
        
        Object valuesForV2_1_1[] = {"v2_s1_a1", subset1};
        Object valuesForV2_1_2[] = {"v2_s2_a1", subset2};
        
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
            SDFDatatypeFactory.createAndReturnDatatype("Set")
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
                SDFDatatypeFactory.createAndReturnDatatype("String")
        );
        
        outputAttributes[1] = new SDFAttribute("output","a3");
        outputAttributes[1].setDatatype(
                SDFDatatypeFactory.createAndReturnDatatype("String")
        );

        outputAttributes[2] = new SDFAttribute("output","a4");
        outputAttributes[2].setDatatype(
                SDFDatatypeFactory.createAndReturnDatatype("String")
        );
        
        outputAttributes[3] = new SDFAttribute("output", "a2s1");
        outputAttributes[3].setDatatype(
                SDFDatatypeFactory.createAndReturnDatatype("String")
        );
        
        outputAttributes[4] = new SDFAttribute("output", "a2s2");
        outputAttributes[4].setDatatype(
                SDFDatatypeFactory.createAndReturnDatatype("Set")
        );
                
        outputSchema = new SDFAttributeList(outputAttributes);
        return outputSchema;
    }
}
