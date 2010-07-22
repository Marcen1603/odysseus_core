/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.scars.filter.gainfunction;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.IGainFunction;
import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanGainFunctionTest {

	
	IGainFunction gainfunction;
	double[][] result = new double[2][2];
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	HashMap<Integer,Object> parameters = new HashMap<Integer,Object>();;
	
	
	
	// the covariance of the old measurement

	double[][] covarianceOld = { {5.0,15.0}, {15.0,10.0} };
	
		
	// the covariance of the new measurement
	double[][] covarianceNew = { {3.0,21.0}, {21,7.0} };
	
	parameters.put(HashConstants.OLD_COVARIANCE, covarianceOld);
	parameters.put(HashConstants.NEW_COVARIANCE,covarianceNew);
	
	
	this.gainfunction = new KalmanGainFunction();
	
	this.gainfunction.setParameters(parameters);
	
	
	}
	@Test
	public void test() {
		double[][] expected = { {0.3922413793103448,0.09051724137931039}, {0.05172413793103449,0.39655172413793105} };
	
		
		this.result=this.gainfunction.computeGain();
		assertArrayEquals(expected,result);
	}
}
