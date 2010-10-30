/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.function.gainfunction;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;


/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class KalmanGainFunctionTest<K> extends TestCase  {

	private AbstractMetaDataCreationFunction gainfunction;



	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;

	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;


	/**
	 * @throws java.lang.Exception
	 */

	@Override
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

		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,1);



		IConnection[] objConList = new IConnection[expectedTuple.getMetadata().getConnectionList().toArray().length];
		ArrayList<IConnection> tmpConList = expectedTuple.getMetadata().getConnectionList();

		for(int i = 0; i < objConList.length; i++) {
			objConList[i] = tmpConList.get(i);
		}


	/*	MVRelationalTuple<StreamCarsMetaData<K>> test = (MVRelationalTuple<StreamCarsMetaData<K>>) objConList[0].getRight();
		
		test.getMetadata().setGain(gain);
		
		objConList[0].setRight(test);
	*/	
		
		gainfunction = new KalmanGainFunction(); 

	}

	@Test
	public  void test() {
		
	IConnection connected = measurementTuple.getMetadata().getConnectionList().get(0);
	
	gainfunction.compute(connected,measurementTuple, null);
	
	assertEquals(expectedTuple,measurementTuple);


	}

}
