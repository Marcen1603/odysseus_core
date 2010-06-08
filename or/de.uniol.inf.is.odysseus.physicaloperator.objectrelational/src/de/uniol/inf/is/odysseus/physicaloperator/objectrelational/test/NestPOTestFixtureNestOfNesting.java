package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.SetEntry;
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
public class NestPOTestFixtureNestOfNesting 
	implements NestPOTestFixtureFactory {

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
			SDFDatatypeFactory.getDatatype("Nest")
		);

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
				SDFDatatypeFactory.getDatatype("Nest")
		);

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
	public List<ObjectRelationalTuple<TimeInterval>> getInputTuples() {

		List<ObjectRelationalTuple<TimeInterval>> inputTuples;	
		
		inputTuples = new ArrayList<ObjectRelationalTuple<TimeInterval>>();
		
		ObjectRelationalTuple<TimeInterval> firstTuple;
		
		SetEntry<ObjectRelationalTuple<TimeInterval>> nest[] = new SetEntry[2];

		String[] firstNestedValues = new String[3];
		firstNestedValues[0] = "2";
		firstNestedValues[1] = "3";
		firstNestedValues[2] = "4";
		
		ObjectRelationalTuple<TimeInterval> firstNested = 
			new ObjectRelationalTuple<TimeInterval>(firstNestedValues);
		
		String[] secondNestedValues = new String[3];
		secondNestedValues[0] = "5";
		secondNestedValues[1] = "6";
		secondNestedValues[2] = "7";
		
		ObjectRelationalTuple<TimeInterval> secondNested = 
			new ObjectRelationalTuple<TimeInterval>(secondNestedValues);
		
		nest[0] = new SetEntry(firstNested);
		nest[1] = new SetEntry(secondNested); 
		
		Object attributeValues[] = new Object[4];
		
		attributeValues[0] = "1";
		attributeValues[1] = nest;
		attributeValues[2] = "3";
		attributeValues[3] = "4";
		
		firstTuple = 
			new ObjectRelationalTuple<TimeInterval>(
				attributeValues
			);
		
		firstTuple.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
		
		ObjectRelationalTuple<TimeInterval> secondTuple;
		
		SetEntry[] nest2 = new SetEntry[2];
				
		String[] firstNestedValues2 = new String[3];
		firstNestedValues2[0] = "2";
		firstNestedValues2[1] = "3";
		firstNestedValues2[2] = "4";
		
		ObjectRelationalTuple<TimeInterval> firstNested2 = 
			new ObjectRelationalTuple<TimeInterval>(firstNestedValues2);
		
		String[] secondNestedValues2 = new String[3];
		secondNestedValues2[0] = "5";
		secondNestedValues2[0] = "6";
		secondNestedValues2[0] = "7";
		
		ObjectRelationalTuple<TimeInterval> secondNested2 = 
			new ObjectRelationalTuple<TimeInterval>(secondNestedValues2);
		
		nest2[0] = new SetEntry(firstNested2);
		nest2[1] = new SetEntry(secondNested2); 
		
		Object attributeValues2[] = new Object[4];
		
		attributeValues2[0] = "1";
		attributeValues2[1] = nest;
		attributeValues2[2] = "3";
		attributeValues2[3] = "4";
		
		secondTuple = 
			new ObjectRelationalTuple<TimeInterval>(
				attributeValues2
			);
		
		secondTuple.setMetadata(
			new TimeInterval(
				new PointInTime(0),
				new PointInTime(10)
			)
		);
				
		inputTuples.add(firstTuple);
		inputTuples.add(secondTuple);
		
		return inputTuples;
	}
}
