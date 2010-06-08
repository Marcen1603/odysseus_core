package de.uniol.inf.is.odysseus.physicaloperator.objectrelational;

import java.util.ArrayList;

import org.junit.Test;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class UnnestPOTest {
	
	@Test public void testProcessingSets() {
		UnnestPO<TimeInterval> unnestPO;
		
		SDFAttributeList inputSchema;
		SDFAttributeList outputSchema;
		SDFAttributeList subAttributesA2 = new SDFAttributeList();
		
		SDFAttribute nestingAttribute;
	
		SDFAttribute inputAttributes[] = new SDFAttribute[4]; 
		SDFAttribute outputAttributes[] = new SDFAttribute[2]; 
		SDFAttribute groupingAttributesArray[] = new SDFAttribute[3]; 
		
		/* 
		 * Setting up the input schema and output schema
		 * with some attributes. the nesting attribute is 
		 * where the sets go in. But now we're adding a 
		 * previous nesting to attribute a2.
		 */
		
		inputAttributes[0] = new SDFAttribute("input","a1");
		inputAttributes[0].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);
		
		/*
		 * Here it goes, with a2sub1 and a2sub2. The test checks if 
		 * nested tuples will correctly nest. 
		 */
		
		inputAttributes[1] = new SDFAttribute("input","a2");
		
		// first sub attribute
		
		SDFAttribute a2sub1 = new SDFAttribute("input", "a2sub1");
		a2sub1.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		subAttributesA2.add(0, a2sub1);
		
		// second sub attribute
		
		SDFAttribute a2sub2 = new SDFAttribute("input", "a2sub2");
		a2sub2.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		subAttributesA2.add(0, a2sub2);
		
		inputAttributes[1].addSubattribute(a2sub1);
		inputAttributes[1].addSubattribute(a2sub2);
		
		// @TODO SDFDatatype for such nesting. 
		
		inputAttributes[1].setDatatype(
			SDFDatatypeFactory.getDatatype("Nesting") 
		);
		
		inputAttributes[2] = new SDFAttribute("input","a3");
		inputAttributes[2].setDatatype(
				SDFDatatypeFactory.getDatatype("String")
		);
		
		inputAttributes[3] = new SDFAttribute("input","a4");
		inputAttributes[3].setDatatype(
			SDFDatatypeFactory.getDatatype("String")
		);
		
		outputAttributes[0] = new SDFAttribute("output","a1");
		outputAttributes[1] = new SDFAttribute("output","nesting");
		      
		inputSchema = 
			new SDFAttributeList(inputAttributes);
		
		outputSchema = 
			new SDFAttributeList(outputAttributes);
		
		nestingAttribute = new SDFAttribute("output", "nesting");
		
		unnestPO = new UnnestPO<TimeInterval>(inputSchema, 
											outputSchema, 
											nestingAttribute
											);
		
		// First Tuple construction
		
		ArrayList<RelationalTuple<TimeInterval>> subTuplesA2 = 
			new ArrayList<RelationalTuple<TimeInterval>>();
		
		Object valuesForV2_1_1[] = {"v2s1","v2s2"};
		Object valuesForV2_1_2[] = {"v2s1","v2s2"};
		
		RelationalTuple<TimeInterval> t1sub1 = 
			new RelationalTuple<TimeInterval>(
				subAttributesA2,
				valuesForV2_1_1
			);
	
		t1sub1.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		RelationalTuple<TimeInterval> t1sub2 = 
			new RelationalTuple<TimeInterval>(
				subAttributesA2,
				valuesForV2_1_2
			);
		
		t1sub2.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		subTuplesA2.add(t1sub1);
		subTuplesA2.add(t1sub2);
		
		Object[] t1val = {"v1", subTuplesA2, "v3", "v4"};
		
		RelationalTuple<TimeInterval> t1 = new 
			RelationalTuple<TimeInterval>(
				inputSchema,
				t1val
			);
	
		t1.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
	
		// End of first tuple construction
	   
	    System.out.println(unnestPO.processNextTest(t1, 0));	
	}
}
