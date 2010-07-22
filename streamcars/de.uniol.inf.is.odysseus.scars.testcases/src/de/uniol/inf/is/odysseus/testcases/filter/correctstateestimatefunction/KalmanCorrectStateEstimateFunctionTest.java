/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.correctstateestimatefunction;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.ICorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateEstimateFunctionTest {

	
	ICorrectStateEstimateFunction correctStateEstimateFunction; 
	double[] result = new double[2];
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	HashMap<String,Object> parameters = new HashMap<String,Object>();;
	
	
	
	// the old measurement

	double[] measurementOld = {0.9,1.7};
	
		
	// the new measurement
	double[] measurementNew = {1.0,2.0};
	
	// the gain
	double[][] gain = { {0.2,0.2}, {0.1,0.4}};
	
	parameters.put("measurementOld", measurementOld);
	parameters.put("measurementNew",measurementNew);
	
	
	this.correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction();
	
	this.correctStateEstimateFunction.setParameters(parameters);
	
	
	}
	@Test
	public void test() {
		double[] expected = {1.7,1.82};
	
		
		this.result=this.correctStateEstimateFunction.correctStateEstimate();
		
		assertEquals(expected[0], result[0]);
		assertEquals(expected[1], result[1]);
		
	}
}
