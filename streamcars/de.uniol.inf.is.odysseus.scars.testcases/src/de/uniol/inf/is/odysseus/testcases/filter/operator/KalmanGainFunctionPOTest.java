/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.operator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.testcases.FilterPOTestData;

import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanGainFunctionPO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;



/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class KalmanGainFunctionPOTest {

	private KalmanGainFunctionPO gainfunctionPO;
	

	
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
		
		measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew);
		
		// the expected tuple
		
		expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew);
		
		Connection[] objConList = (Connection[]) ((IConnectionContainer) expectedTuple.getMetadata()).getConnectionList().toArray();
		
		double[][] gainExp = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688 }};
		
		((MetaAttributeContainer<StreamCarsMetaData>) objConList[0].getRight()).getMetadata().setGain(gainExp);
		
		
		KalmanGainFunction gainfunction = new KalmanGainFunction();
		
		// create the PO
		
		gainfunctionPO = new KalmanGainFunctionPO();
		
		gainfunctionPO.setFilterFunction(gainfunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		gainfunctionPO.setOldObjListPath(oldObjListPath);
		
		gainfunctionPO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void test() {
	
	resultTuple = gainfunctionPO.computeAll(measurementTuple);
	
	assertEquals(resultTuple, expectedTuple);
	

	}
	
}
