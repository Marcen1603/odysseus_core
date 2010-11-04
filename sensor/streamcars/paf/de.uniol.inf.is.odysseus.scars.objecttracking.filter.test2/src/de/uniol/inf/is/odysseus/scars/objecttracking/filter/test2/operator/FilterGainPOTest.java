/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test2.operator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataCreationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanGainFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterGainPO;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.test2.FilterFunctionTestData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

/**
 * @author dtwumasi
 *
 */
public class FilterGainPOTest<K> {

	
	private FilterGainPO gainfunctionPO;
	
	private AbstractMetaDataCreationFunction gainFunction;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple;

	private MVRelationalTuple<StreamCarsMetaData<K>> resultTuple;
	
	private MVRelationalTuple<StreamCarsMetaData<K>> expectedTuple;
	
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
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, null,5);

		// the expected tuple

		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew, gain,5);

		gainfunctionPO = new FilterGainPO(); 
		gainFunction = new KalmanGainFunction();
		
		gainfunctionPO.setMetaDataCreationFunction(gainFunction);
		
		gainfunctionPO.setOutputSchema(testData.getSchema());
		
	
	}

	/**
	 * Test method for {@link de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator.FilterGainPO#computeAll(de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple)}.
	 */
	@Test
	public void testComputeAll() {
		
		resultTuple = gainfunctionPO.computeAll(measurementTuple);
		
		assertEquals(expectedTuple,resultTuple);
	}

}
