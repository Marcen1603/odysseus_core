/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.operator;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanCorrectStateEstimatePO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.testcases.FilterPOTestData;



/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateEstimatePOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	
	private KalmanCorrectStateEstimatePO correctStateEstimatePO;
	
	private MVRelationalTuple<M> measurementTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		// get a measurement Tuple
		
		FilterPOTestData<M> measurement = new FilterPOTestData<M>();
		
		//this.measurementTuple = measurement.getTestData();
		
		KalmanCorrectStateEstimateFunction correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction();
	
		// create the PO
		
		correctStateEstimatePO = new KalmanCorrectStateEstimatePO();
		
		correctStateEstimatePO.setFilterFunction(correctStateEstimateFunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		correctStateEstimatePO.setOldObjListPath(oldObjListPath);
		
		correctStateEstimatePO.setNewObjListPath(newObjListPath);
	
	}
	
	@Test
	public void test() {
		
	}
}
