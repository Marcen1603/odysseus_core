/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.correctstatecovariancefunction;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.ICorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;



import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateCovarianceFunctionTest {

	
	KalmanCorrectStateCovarianceFunction kalmanCorrectStateCovarianceFunction; 
	double[][] result = new double[2][2];
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	HashMap<Integer,Object> parameters = new HashMap<Integer,Object>();;
	
	
	
	// the covariance of the old measurement

	double[][] covarianceOld = { {5.0,15.0}, {15.0,10.0} };
	
	// the gain
	double[][] gain = { {0.2,0.2}, {0.1,0.4}};
	
	parameters.put(HashConstants.OLD_COVARIANCE, covarianceOld);
	parameters.put(HashConstants.GAIN,gain);
	
	
	// create function and set parameters
	this.kalmanCorrectStateCovarianceFunction = new KalmanCorrectStateCovarianceFunction();
	
	this.kalmanCorrectStateCovarianceFunction.setParameters(parameters);
	
	
	}
	@Test
	public void test() {
		double[][] expected = { {1.0,10.0}, {8.5,4.5} };
	
		
		this.result=this.kalmanCorrectStateCovarianceFunction.correctStateCovariance();
		
		assertArrayEquals(expected,result);
		
	}
}
