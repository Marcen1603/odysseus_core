package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.objecttracking.test.nest.fixtures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objectrelational.base.SetEntry;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * 
 * start:
 * 
 * a1	a2	 a3	a4
 * 1	[2	3	4][5	6	7]	3	4 [0,10)
 * 1	[1	1	1][2	4	5]	8	9 [0,10)
 * 
 * end:
 * 
 * a1	n1
 * 1	[[2	3	4][5	6	7]	3	4][[1	1	1][2	4	5]	8	9] [0,10)
 * 
 * @author Jendrik Poloczek
 * 
 */
public class NestOfNestingFixture 
	implements Factory {

	@Override
	public SDFAttributeList getGroupingAttributes() {
		/*
		 * Next go the parameters for the nest operator: the 
		 * toNestAttributes and the nestingAttribute. The complement
		 * of toNestAttributes is grouped by equality. 
		 */

		SDFAttribute groupingAttributesArray[] = new SDFAttribute[1]; 
		groupingAttributesArray[0] = new SDFAttribute("input","a1");

		return new SDFAttributeList(groupingAttributesArray);
	}

	@Override
	public SDFAttributeList getInputSchema() {
		/* 
		 * Setting up the input schema and output schema
		 * with some attributes. the nesting attribute is 
		 * where the sets go in.
		 */

		SDFAttribute inputAttributes[] = new SDFAttribute[4]; 

		inputAttributes[0] = new SDFAttribute("input","a1");
		inputAttributes[0].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);

		inputAttributes[1] = new SDFAttribute("input","a2");
		inputAttributes[1].setDatatype(
			SDFDatatypeFactory.getDatatype("Set")
		);
		
		inputAttributes[1].setSubattributes(this.getInputNestSchema());

		inputAttributes[2] = new SDFAttribute("input","a3");
		inputAttributes[2].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);

		inputAttributes[3] = new SDFAttribute("input","a4");
		inputAttributes[3].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);
	
		return new SDFAttributeList(inputAttributes);
	}
	
	public SDFAttributeList getOutputNestSchema() {
	    
	    SDFAttribute inputAttributes[] = new SDFAttribute[3];
	    
        inputAttributes[0] = new SDFAttribute("input","a2");
        inputAttributes[0].setDatatype(
            SDFDatatypeFactory.getDatatype("Set")
        );
        
        inputAttributes[0].setSubattributes(this.getInputNestSchema());

        inputAttributes[1] = new SDFAttribute("input","a3");
        inputAttributes[1].setDatatype(
            SDFDatatypeFactory.getDatatype("String")
        );

        inputAttributes[2] = new SDFAttribute("input","a4");
        inputAttributes[2].setDatatype(
            SDFDatatypeFactory.getDatatype("String")
        );
        
        return new SDFAttributeList(inputAttributes);
	}

	private SDFAttributeList getInputNestSchema() {
		SDFAttribute inputAttributes[] = new SDFAttribute[3]; 

		inputAttributes[0] = new SDFAttribute("input","a1");
		inputAttributes[0].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);

		inputAttributes[1] = new SDFAttribute("input","a2");
		inputAttributes[1].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);

		inputAttributes[2] = new SDFAttribute("input","a3");
		inputAttributes[2].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);
		return new SDFAttributeList(inputAttributes);		
	}
	
	@Override
	public SDFAttribute getNestingAttribute() {
		return new SDFAttribute("output", "nesting");
	}

	@Override
	public SDFAttributeList getOutputSchema() {

		SDFAttribute outputAttributes[] = new SDFAttribute[2];

		SDFAttribute outA1 = new SDFAttribute("output","a1");
		SDFAttribute outA2 = new SDFAttribute("output","nesting");

		SDFAttribute outA2S1 = new SDFAttribute("output", "a2");
		outA2S1.setDatatype(
				SDFDatatypeFactory.getDatatype("Set")
		);
		outA2S1.setSubattributes(this.getOutputNestSchema());

		SDFAttribute outA2S2 = new SDFAttribute("output", "a3");
		outA2S2.setDatatype(
				SDFDatatypeFactory.getDatatype("String")
		);

		SDFAttribute outA2S3 = new SDFAttribute("output", "a4");
		outA2S3.setDatatype(
				SDFDatatypeFactory.getDatatype("String")
		);

		outA2.addSubattribute(outA2S1);
		outA2.addSubattribute(outA2S2);
		outA2.addSubattribute(outA2S3);

		outputAttributes[0] = outA1;
		outputAttributes[1] = outA2;

		return new SDFAttributeList(outputAttributes);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> getInputTuples() {

		List<MVRelationalTuple<ObjectTrackingMetadata<Object>>> inputTuples;
		MVRelationalTuple<ObjectTrackingMetadata<Object>> firstTuple;
		SetEntry<MVRelationalTuple<ObjectTrackingMetadata<Object>>> nest[];
		
		inputTuples = new ArrayList<MVRelationalTuple<ObjectTrackingMetadata<Object>>>();
		nest = new SetEntry[2];

		String[] firstNestedValues = new String[3];
		firstNestedValues[0] = "2";
		firstNestedValues[1] = "3";
		firstNestedValues[2] = "4";
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> firstNested = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>>(				
				(Object[]) firstNestedValues
			);
		
		String[] secondNestedValues = new String[3];
		secondNestedValues[0] = "5";
		secondNestedValues[1] = "6";
		secondNestedValues[2] = "7";
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> secondNested = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>>(
				(Object[]) secondNestedValues
			);
		
		nest[0] = new SetEntry(firstNested);
		nest[1] = new SetEntry(secondNested); 
		
		Object attributeValues[] = new Object[4];
		
		attributeValues[0] = "1";
		attributeValues[1] = nest;
		attributeValues[2] = "3";
		attributeValues[3] = "4";
		
		firstTuple = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>>(
				attributeValues
			);
		
		ObjectTrackingMetadata<Object> meta = 
			new ObjectTrackingMetadata<Object>();
		
		meta.setStart(new PointInTime(0));
		meta.setEnd(new PointInTime(10));	
		
		firstTuple.setMetadata(meta);
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> secondTuple;
		
		SetEntry[] nest2 = new SetEntry[2];
				
		String[] firstNestedValues2 = new String[3];
		firstNestedValues2[0] = "1";
		firstNestedValues2[1] = "1";
		firstNestedValues2[2] = "1";
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> firstNested2 = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>>(			 
			    (Object[]) firstNestedValues2			    
			);
		        
		String[] secondNestedValues2 = new String[3];
		secondNestedValues2[0] = "2";
		secondNestedValues2[1] = "4";
		secondNestedValues2[2] = "5";
		
		MVRelationalTuple<ObjectTrackingMetadata<Object>> secondNested2 = 
		    new MVRelationalTuple<ObjectTrackingMetadata<Object>>(		        
		        (Object[]) secondNestedValues2
		    );
		
		nest2[0] = new SetEntry(firstNested2);
		nest2[1] = new SetEntry(secondNested2); 
		
		Object attributeValues2[] = new Object[4];
		
		attributeValues2[0] = "1";
		attributeValues2[1] = nest2;
		attributeValues2[2] = "8";
		attributeValues2[3] = "9";
		
		secondTuple = 
			new MVRelationalTuple<ObjectTrackingMetadata<Object>> (			 
				(Object[]) attributeValues2
			);
		
		ObjectTrackingMetadata<Object> meta2 = 
			new ObjectTrackingMetadata<Object>();
		
		meta2.setStart(new PointInTime(0));
		meta2.setEnd(new PointInTime(10));
		
		secondTuple.setMetadata(meta2);
		
		inputTuples.add(firstTuple);
		inputTuples.add(secondTuple);
		
		return inputTuples;
	}
}
