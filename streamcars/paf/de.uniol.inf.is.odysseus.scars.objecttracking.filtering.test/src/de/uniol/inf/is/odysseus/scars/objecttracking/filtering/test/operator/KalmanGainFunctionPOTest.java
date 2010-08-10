/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.operator;

import java.util.ArrayList;


import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanGainFunctionPO;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data.FilterPOTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;


/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class KalmanGainFunctionPOTest extends TestCase  {

	private KalmanGainFunctionPO<StreamCarsMetaData> gainfunctionPO;



	private MVRelationalTuple<StreamCarsMetaData> measurementTuple;

	private MVRelationalTuple<StreamCarsMetaData> expectedTuple;

	private MVRelationalTuple<StreamCarsMetaData> resultTuple;

	/**
	 * @throws java.lang.Exception
	 */
	@SuppressWarnings("unchecked")
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

		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain);



		Connection[] objConList = new Connection[expectedTuple.getMetadata().getConnectionList().toArray().length];
		ArrayList<Connection> tmpConList = expectedTuple.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}


		MVRelationalTuple<StreamCarsMetaData> test = (MVRelationalTuple<StreamCarsMetaData>) objConList[0].getRight();
		
		test.getMetadata().setGain(gain);
		
		objConList[0].setRight(test);
		
		KalmanGainFunction gainfunction = new KalmanGainFunction();

		// create the PO

		gainfunctionPO = new KalmanGainFunctionPO<StreamCarsMetaData>();

		gainfunctionPO.setFilterFunction(gainfunction);

		int[] oldObjListPath = {0};

		int[] newObjListPath = {1};

		gainfunctionPO.setOldObjListPath(oldObjListPath);

		gainfunctionPO.setNewObjListPath(newObjListPath);


	}

	@Test
	public  void test() {

	resultTuple = gainfunctionPO.computeAll(measurementTuple);

	assertEquals(expectedTuple,resultTuple);


	}

}
