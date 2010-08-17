package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.correctstateestimatefunction;

import java.util.ArrayList;


import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;



import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateEstimateFunction;

import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data.FilterPOTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <T>
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanCorrectStateEstimateFunctionTest<K> extends TestCase {

	private AbstractDataUpdateFunction correctStateEstimateFunction;
	

	
	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> resultTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterFunctionTestData<K> testData = new FilterFunctionTestData<K>();
		
		// Measurement Data
		
		double speedOld = 0.9;
		
		double posOld = 1.7;
		
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		double speedNew = 1.0;
		
		double posNew = 2.0;
		
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		
		double[][] gain = { {0.2,0.2}, {0.1,0.4}};
		
		
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,1);
		
		// the expected tuple
		
		double speedOldExp = 0.98;
		
		double posOldExp = 1.83;
		
		
		
		expectedTuple = testData.generateTestTuple(speedOldExp, posOldExp, covarianceOld, speedNew, posNew, covarianceNew,gain,1);
		
		correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction();
		
		
	
	
	}
	
	@Test
	public  void test() {
	
		
		Connection connected = measurementTuple.getMetadata().getConnectionList().get(0);
		
		ArrayList<int[]> attributePathsOld = measurementTuple.getMetadata().getAttributePaths();
		
		ArrayList<int[]> attributePathsNew = measurementTuple.getMetadata().getAttributePaths();
		
		int i=0;
		
		correctStateEstimateFunction.compute(connected, attributePathsNew, attributePathsOld,i);
	
		assertEquals(expectedTuple,measurementTuple);
	

	}
	
}