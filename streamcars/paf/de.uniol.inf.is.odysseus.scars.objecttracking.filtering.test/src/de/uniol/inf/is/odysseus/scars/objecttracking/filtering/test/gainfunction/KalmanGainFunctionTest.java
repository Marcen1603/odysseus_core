/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.gainfunction;

import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data.FilterFunctionTestData;


import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author dtwumasi
 *
 */
public class KalmanGainFunctionTest extends TestCase {

	KalmanGainFunction kalmanGainFunction;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
	FilterFunctionTestData testdata = new FilterFunctionTestData();
		
	//kalmanGainFunction = new KalmanGainFunction(testdata.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain));
	
	}
	
	@Test
	public void test() {
		
		double[][] expected = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688} };
	
	//	double[][] result=this.kalmanGainFunction.compute(testdata.);
		
	//	assertArrayEquals(expected,result);
	}
	


}
