package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.operator;

import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanCorrectStateEstimatePO;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data.FilterPOTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <T>
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanCorrectStateEstimatePOTest extends TestCase {

	private KalmanCorrectStateEstimatePO<StreamCarsMetaData> correctStateEstimatePO;
	

	
	private MVRelationalTuple<StreamCarsMetaData> measurementTuple;
	
	private MVRelationalTuple<StreamCarsMetaData> expectedTuple;
	
	private MVRelationalTuple<StreamCarsMetaData> resultTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterPOTestData testData = new FilterPOTestData();
		
		// Measurement Data
		
		double speedOld = 0.9;
		
		double posOld = 1.7;
		
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		double speedNew = 1.0;
		
		double posNew = 2.0;
		
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		
		double[][] gain = { {0.2,0.2}, {0.1,0.4}};
		
		
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain);
		
		// the expected tuple
		
		double speedOldExp = 0.98;
		
		double posOldExp = 1.83;
		
		
		
		expectedTuple = testData.generateTestTuple(speedOldExp, posOldExp, covarianceOld, speedNew, posNew, covarianceNew,gain);
		

		KalmanCorrectStateEstimateFunction estimatefunction = new KalmanCorrectStateEstimateFunction();
		
		// create the PO
		
		correctStateEstimatePO = new KalmanCorrectStateEstimatePO<StreamCarsMetaData>();
		
		correctStateEstimatePO.setFilterFunction(estimatefunction);
		
		correctStateEstimatePO.setSchema(testData.getSchema());
		
		int[] oldObjListPath = {1};
		
		int[] newObjListPath = {0};
			
		correctStateEstimatePO.setOldObjListPath(oldObjListPath);
		
		correctStateEstimatePO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void test() {
	
	resultTuple = correctStateEstimatePO.computeAll(measurementTuple);
	
	assertEquals(expectedTuple,resultTuple);
	

	}
	
}