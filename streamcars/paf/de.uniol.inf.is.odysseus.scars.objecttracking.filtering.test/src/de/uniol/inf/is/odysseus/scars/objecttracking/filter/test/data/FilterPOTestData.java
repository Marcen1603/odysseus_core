/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.HashConstants;

/**
 * @author dtwumasi
 *
 */
public class FilterPOTestData {
	
	private HashMap<Integer,Object> testData;
	
	public FilterPOTestData() {
		this.testData = generateTestData();
	}
	
	private HashMap<Integer,Object> generateTestData() {
	
		HashMap<Integer,Object> generate = new HashMap<Integer,Object>();
		
		// the covariance of the old measurement
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		generate.put(HashConstants.OLD_COVARIANCE, covarianceOld);
		
		// the covariance of the new measurement
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		generate.put(HashConstants.NEW_COVARIANCE, covarianceNew);
		
		// the gain
		double[][] gain = { {0.2,0.2}, {0.1,0.4}};
		generate.put(HashConstants.GAIN, gain);
		
		// the old measurement
		double[] measurementOld = {0.9,1.7};
		generate.put(HashConstants.OLD_MEASUREMENT, measurementOld);
		
		// the new measurement
		double[] measurementNew = {1.0,2.0};
		generate.put(HashConstants.NEW_MEASUREMENT, measurementNew);
		
		return generate;
	}
	
	/**
	 * @return the testData
	 */
	public HashMap<Integer,Object> getTestData() {
		return testData;
	}
}
