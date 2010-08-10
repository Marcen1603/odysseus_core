/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.correctstateestimatefunction;


import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data.FilterFunctionTestData;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateEstimateFunctionTest {

	
	KalmanCorrectStateEstimateFunction correctStateEstimateFunction; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	FilterFunctionTestData testdata = new FilterFunctionTestData();

	this.correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction(testdata.getTestData());
	
	}
	@Test
	public void test() {
		
		double[] expected = {0.98,1.83};
		
		double[] result=this.correctStateEstimateFunction.compute();
		
		assertEquals(expected[0], result[0],0);
		assertEquals(expected[1], result[1],0);
		
	}
}
