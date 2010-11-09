package de.uniol.inf.is.odysseus.physicaloperator.objectrelational.test.nest.fixtures;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.objectrelational.ObjectRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * 
 * start:
 * 
 * a1	a2	a3	a4
 * 1	2	3	4 	[0,10)
 * 1	5	6	7	[0,10)
 * 2	2	3	4	[15,30)
 * 2	5	6	7	[20,30)
 * 
 * inputSchema is a1 ... a4 are strings
 * outputSchema is a1, n1. a1 is string 
 * 				and nesting got sub attributes a2, a3, a4. 
 * 
 * groupingAttributes contains a1.
 * nestingAttribute is n1
 * 
 * outcome: 
 * 
 * a1	n1
 * 1	[2	3	4 [0,10)][5	6	7	[0,10)]
 * 2	[2	3	4 [15,20)][5	6	7	[15,20)]
 * 2	[2	5	6	7 [20,30]]
 * 
 * @author Jendrik Poloczek
 * 
 */
public class SimpleNestingFixture 
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
			SDFDatatypeFactory.getDatatype("String")
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
		outA1.setDatatype(
		    SDFDatatypeFactory.getDatatype("String")
		);
	
		SDFAttribute outA2 = new SDFAttribute("output","n1");
		outA2.setDatatype(
		    SDFDatatypeFactory.getDatatype("Set")
		);

		SDFAttribute outA2S1 = new SDFAttribute("output", "a2");
		outA2S1.setDatatype(
			SDFDatatypeFactory.getDatatype("String")
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

	@Override
	public List<ObjectRelationalTuple<TimeInterval>> getInputTuples() {

		SDFAttributeList inputSchema = this.getInputSchema();
		
		String[] inputData = new String[] {
				"1;2;3;4,0;10",
				"1;5;6;7,0;10",
				"2;2;3;4,15;30",
				"2;5;6;7,20;30"
		};  


		ArrayList<ObjectRelationalTuple<TimeInterval>> inputTuples = 
			new ArrayList<ObjectRelationalTuple<TimeInterval>>();

		String dataAndInterval[];
		String timeInterval[];

		for(int i = 0; i < inputData.length; i++) {
		    
		    dataAndInterval = inputData[i].split(",");
		    timeInterval = dataAndInterval[1].split(";");
		    
			ObjectRelationalTuple<TimeInterval> tuple = 
				new ObjectRelationalTuple<TimeInterval>(
					inputSchema,
					dataAndInterval[0],
					';'
				);
			
			long start = new Long(timeInterval[0]);
			long end = new Long(timeInterval[1]);
			
			tuple.setMetadata(
				new TimeInterval(
					new PointInTime(start), 
					new PointInTime(end)
				)
			);
			inputTuples.add(tuple);
		}
		return inputTuples;
	}

}
