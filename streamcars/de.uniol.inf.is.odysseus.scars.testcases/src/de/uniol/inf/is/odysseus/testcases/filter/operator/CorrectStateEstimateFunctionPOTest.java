/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.operator;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.CorrectStateEstimateFunctionPO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.testcases.FilterAOTestData;



/**
 * @author dtwumasi
 *
 */
public class CorrectStateEstimateFunctionPOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	
	private CorrectStateEstimateFunctionPO correctStateEstimatePO;
	
	private MVRelationalTuple<M> measurementTuple;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		// get a measurement Tuple
		
		FilterAOTestData<M> measurement = new FilterAOTestData<M>();
		
		this.measurementTuple = measurement.getTestData();
		
		KalmanCorrectStateEstimateFunction correctStateEstimateFunction = new KalmanCorrectStateEstimateFunction();
	
		// create the PO
		
		correctStateEstimatePO = new CorrectStateEstimateFunctionPO();
		
		correctStateEstimatePO.setEstimateFunction(correctStateEstimateFunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		correctStateEstimatePO.setOldObjListPath(oldObjListPath);
		
		correctStateEstimatePO.setNewObjListPath(newObjListPath);
	
	}
	
	@Test
	public void test() {
		
	}
}
