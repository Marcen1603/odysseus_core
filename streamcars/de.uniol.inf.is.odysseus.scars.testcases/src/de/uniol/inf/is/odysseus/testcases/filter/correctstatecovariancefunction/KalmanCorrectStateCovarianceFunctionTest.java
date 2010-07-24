/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.correctstatecovariancefunction;


import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.testcases.FilterFunctionTestData;

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
	
	FilterFunctionTestData testdata = new FilterFunctionTestData();

	this.kalmanCorrectStateCovarianceFunction = new KalmanCorrectStateCovarianceFunction(testdata.getTestData());
	
	
	}
	@Test
	public void test() {

		double[][] expected = { {-6.0,38.0}, {29.5,1} };
	
		
		double[][] result=this.kalmanCorrectStateCovarianceFunction.compute();
		
		assertArrayEquals(expected,result);
		
	}
}
