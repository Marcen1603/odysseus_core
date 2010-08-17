package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.correctstatecovariancefunction;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterCovarianceUpdatePO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <T>
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanCorrectStateCovarianceFunctionTest<K> extends TestCase  {

	//private FilterCovarianceUpdatePO<StreamCarsMetaData<K>> correctStateCovariancePO;
	
	private KalmanCorrectStateCovarianceFunction covarianceFunction;
	
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
		
		double[][] gain = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688} };
			
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,1);
		
		
		// the expected tuple
		
		double[][] covarianceOldExp = { {-10.320000000000002,26.12}, {26.12,0.48} };
		
		
		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOldExp, speedNew, posNew, covarianceNew, gain,1);
		
		covarianceFunction = new KalmanCorrectStateCovarianceFunction<K>();
		
		// create the PO
		/*
		correctStateCovariancePO = new FilterCovarianceUpdatePO<StreamCarsMetaData>();
		
		correctStateCovariancePO.setUpdateMetaDataFunction(covariancefunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		correctStateCovariancePO.setOldObjListPath(oldObjListPath);
		
		correctStateCovariancePO.setNewObjListPath(newObjListPath);
	*/
	
	}
	
	@Test
	public  void testh() {
	
	Connection connected = measurementTuple.getMetadata().getConnectionList().get(0);
	
	covarianceFunction.compute(connected);
	
	assertEquals(expectedTuple,measurementTuple);
	

	}
	
}