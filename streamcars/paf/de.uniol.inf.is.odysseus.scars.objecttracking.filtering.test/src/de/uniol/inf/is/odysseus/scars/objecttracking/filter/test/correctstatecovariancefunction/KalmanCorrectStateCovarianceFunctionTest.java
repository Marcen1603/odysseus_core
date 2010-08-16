package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.correctstatecovariancefunction;

import java.util.ArrayList;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.UpdateMetaDataPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <T>
 * @param <StreamCarsMetaData>
 *
 */
public class KalmanCorrectStateCovarianceFunctionTest extends TestCase  {

	private UpdateMetaDataPO<StreamCarsMetaData> correctStateCovariancePO;
	

	
	private MVRelationalTuple<StreamCarsMetaData> measurementTuple;
	
	private MVRelationalTuple<StreamCarsMetaData> expectedTuple;
	
	private MVRelationalTuple<StreamCarsMetaData> resultTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterFunctionTestData testData = new FilterFunctionTestData();
		
		// Measurement Data
		
		double speedOld = 0.9;
		
		double posOld = 1.7;
		
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		double speedNew = 1.0;
		
		double posNew = 2.0;
		
		double[][] covarianceNew = { {3.0,21.0}, {21.0,7.0} };
		
		double[][] gain = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688} };
			
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain);
		
		
		// the expected tuple
		
		double[][] covarianceOldExp = { {-10.320000000000002,26.12}, {26.12,0.48} };
		
		
		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOldExp, speedNew, posNew, covarianceNew, gain);
		
		Connection[] objConList = new Connection[expectedTuple.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = expectedTuple.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}
		
		KalmanCorrectStateCovarianceFunction covariancefunction = new KalmanCorrectStateCovarianceFunction();
		
		// create the PO
		
		correctStateCovariancePO = new UpdateMetaDataPO<StreamCarsMetaData>();
		
		correctStateCovariancePO.setUpdateMetaDataFunction(covariancefunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		correctStateCovariancePO.setOldObjListPath(oldObjListPath);
		
		correctStateCovariancePO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void testh() {
	
	resultTuple = correctStateCovariancePO.computeAll(measurementTuple);
	
	assertEquals(expectedTuple,resultTuple);
	

	}
	
}