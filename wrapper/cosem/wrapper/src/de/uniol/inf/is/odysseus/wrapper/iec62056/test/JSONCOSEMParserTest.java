package de.uniol.inf.is.odysseus.wrapper.iec62056.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.JSONCOSEMParser;

/**
 * 
 * @author Jens Plümer
 *
 */
public class JSONCOSEMParserTest {

	public static final String DATA_1 = "EXAMPLE_1.json";
	public static final String DATA_2 = "EXAMPLE_2.json";

	public InputStreamReader reader;
	public JSONCOSEMParser cosemParser;

	public void setup(String pathToFile) {
		try {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reader = new InputStreamReader(new FileInputStream(new File(pathToFile)));
			SDFAttribute[] attributes = new SDFAttribute[7];
			attributes[0] = new SDFAttribute("source1", "1", SDFDatatype.STRING);
			attributes[1] = new SDFAttribute("source1", "2", SDFDatatype.DOUBLE);
			attributes[2] = new SDFAttribute("source1", "3", SDFDatatype.INTEGER);
			attributes[3] = new SDFAttribute("source1", "4", SDFDatatype.INTEGER);
			attributes[4] = new SDFAttribute("source1", "5", SDFDatatype.STRING);
			attributes[5] = new SDFAttribute("source1", "6", SDFDatatype.LONG);
			attributes[6] = new SDFAttribute("source1", "7", SDFDatatype.STRING);
			@SuppressWarnings("static-access")
			SDFSchema schema = new SDFSchemaFactory().createNewTupleSchema("schema1", attributes);
			cosemParser = new JSONCOSEMParser(reader, schema);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
	}

	@After
	public void terminate() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void parseTest1() {
		setup(DATA_1);
		String expected = "[smartmetergateway_0|0.972|30|-3|000|0|sm_011]";
		String actual = cosemParser.parse();
		assertNotNull("result was null", actual);
		assertEquals(expected, actual);
	}
	
//	@Test
//	public void parseTest2() {
//		setup(DATA_2);
//		StringBuilder builder = new StringBuilder();
//		String expected = "[smartmetergateway_0|0.972|30|-3|000|0|sm_011]";
//		while(cosemParser.isDone()) {
//			builder.append(cosemParser.parsePullInputStream());
//		}
//		String actual = builder.toString();
//		System.out.println(actual);
//		assertNotNull("result was null", actual);
//		assertEquals(expected, actual);
//	}
}
