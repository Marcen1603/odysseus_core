package de.uniol.inf.is.odysseus.testcases.filter.operator;

import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanCorrectStateEstimatePO;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.testcases.FilterPOTestData;
import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;




/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class KalmanCorrectStateEstimatePOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	private KalmanCorrectStateEstimatePO correctStateEstimatePO;
	

	
	private MVRelationalTuple<M> measurementTuple;
	
	private MVRelationalTuple<M> expectedTuple;
	
	private MVRelationalTuple<M> resultTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterPOTestData<M> testData = new FilterPOTestData<M>();
		
		// Measurement Data
		
		double speedOld = 0.9;
		
		double posOld = 1.7;
		
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		double speedNew = 1.0;
		
		double posNew = 2.0;
		
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew);
		
		// the expected tuple
		
		double speedOldExp = 0.98;
		
		double posOldExp = 1.83;
		
		expectedTuple = testData.generateTestTuple(speedOldExp, posOldExp, covarianceOld, speedNew, posNew, covarianceNew);
		
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[] objConList = (Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>[]) expectedTuple.getMetadata().getConnectionList().toArray();
		
		KalmanCorrectStateEstimateFunction estimatefunction = new KalmanCorrectStateEstimateFunction();
		
		// create the PO
		
		correctStateEstimatePO = new KalmanCorrectStateEstimatePO();
		
		correctStateEstimatePO.setFilterFunction(estimatefunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		correctStateEstimatePO.setOldObjListPath(oldObjListPath);
		
		correctStateEstimatePO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void test() {
	
	resultTuple = correctStateEstimatePO.computeAll(measurementTuple);
	
	assertEquals(resultTuple, expectedTuple);
	

	}
	
}