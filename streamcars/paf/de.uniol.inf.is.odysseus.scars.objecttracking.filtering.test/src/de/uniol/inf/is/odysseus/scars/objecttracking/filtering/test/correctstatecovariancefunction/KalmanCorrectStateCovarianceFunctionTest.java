/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.correctstatecovariancefunction;


import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data.FilterFunctionTestData2;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateCovarianceFunctionTest {

	
	KalmanCorrectStateCovarianceFunction kalmanCorrectStateCovarianceFunction; 
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	FilterFunctionTestData2 testdata = new FilterFunctionTestData2();

	this.kalmanCorrectStateCovarianceFunction = new KalmanCorrectStateCovarianceFunction(testdata.getTestData());
	
	
	}
	@Test
	public void test() {

		double[][] expected = { {-10.320000000000002,26.12}, {26.12,0.48} };
	
		
		//double[][] result=this.kalmanCorrectStateCovarianceFunction.compute();
		
		//assertArrayEquals(expected,result);
		
	}
}
