package de.uniol.inf.is.odysseus.wrapper.iec62056.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import de.uniol.inf.is.odysseus.wrapper.iec62056.parser.JSONCOSEMParser;

/**
 * 
 * @author Jens Plümer
 *
 */
public class JSONCOSEMParserTest {

	public static final String DATA_1 = "EXAMPLE_1.json";
	public static final String DATA_2 = "EXAMPLE_2.json";
	public static final String DATA_3 = "EXAMPLE_3.json";
	public static final String DATA_4 = "EXAMPLE_4.json";
	
	public static final String RESULT_1 = 
			  "[smartmetergateway_109|sm_21|3.424839|-3|11700|30|000]";
	public static final String RESULT_2 = 
			  "[smartmetergateway_123|sm_041|1.182|-3|30600|30|000]"
			+ "[smartmetergateway_123|sm_043|1.0575|-3|30600|30|000]"
			+ "[smartmetergateway_123|sm_042|0.7155|-3|30600|30|000]";
	

	public InputStreamReader reader;
	public JSONCOSEMParser cosemParser;

	@Rule
	public TestWatcher testWatcher = new TestWatcher() {
		protected void failed(Throwable e, Description description) {
			System.out.println("" + description.getMethodName() + " \n\tfailed " + e.getMessage());
			super.failed(e, description);
		}

	};

	/**
	 * Parses the given file and checks if the result is equal with {@code expected}.
	 * 
	 * @param pathToFile
	 * @param expected
	 */
	public void parsePullBased(String pathToFile, String expected) {
		try {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String actual = "";
			String result = "";
			/*
			 * Initiate all needed resources; - reader that reads the given
			 * input file - StringBuilder that defines a schema - String-Array
			 * that defines some parser-specific tokens
			 */
			reader = new InputStreamReader(new FileInputStream(new File(pathToFile)));
			cosemParser = new JSONCOSEMParser(reader,
					new StringBuilder("[smgw_device_name|logical_name|value|scaler|capture_time|unit|status]"),
					new String[] { "logical_name", "objects", "smgw_device_name" });
			/*
			 * Call cosemParse.parser() as long as every smart meter has been
			 * processed and concatenate all results to one string that can be
			 * compared with RESULT_X.
			 */
			while (!cosemParser.isDone()) {
				result = cosemParser.parse();
				if (result != null) {
					actual += result;
				}
			}
			assertNotNull("result was null", actual);
			assertEquals(expected, actual);
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
		parsePullBased(DATA_1, RESULT_1);
	}

	@Test
	public void parseTest2() {
		parsePullBased(DATA_2, RESULT_1);
	}

	@Test
	public void parseTest3() {
		parsePullBased(DATA_3, RESULT_2);
	}

	@Test
	public void parseTest4() {
		parsePullBased(DATA_4, RESULT_2);
	}

}
