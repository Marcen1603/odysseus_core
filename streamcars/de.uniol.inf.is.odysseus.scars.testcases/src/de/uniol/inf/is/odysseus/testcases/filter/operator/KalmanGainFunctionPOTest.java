/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.operator;


import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.testcases.FilterPOTestData;
import de.uniol.inf.is.odysseus.testcases.FilterFunctionTestData;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.AbstractFilterPO;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanGainFunctionPO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;



/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class KalmanGainFunctionPOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	private KalmanGainFunctionPO gainfunctionPO;
	

	
	private MVRelationalTuple<M> measurementTuple;
	
	private MVRelationalTuple<M> expectedTuple;
	
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
		
		this.measurementTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew);
		
		// Expected Data
		
		speedOld = 0.9;
		
		posOld = 1.7;
		
		speedNew = 1.0;
		
		posNew = 2.0;
		
		this.expectedTuple = testData.generateTestTuple(speedOld, posOld, covarianceOld, speedNew, posNew, covarianceNew);
		
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
	
	//gainfunctionPO.process_next(measurementTuple, 0);
	
	

	}
	
}
