/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.gainfunction;

import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.testcases.FilterFunctionTestData;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanGainFunctionTest {

	KalmanGainFunction kalmanGainFunction;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	FilterFunctionTestData testdata = new FilterFunctionTestData();
		
	kalmanGainFunction = new KalmanGainFunction(testdata.getTestData());
	
	}
	
	@Test
	public void test() {
		
		double[][] expected = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688} };
	
		double[][] result=this.kalmanGainFunction.compute();
		
		assertArrayEquals(expected,result);
	}
}
