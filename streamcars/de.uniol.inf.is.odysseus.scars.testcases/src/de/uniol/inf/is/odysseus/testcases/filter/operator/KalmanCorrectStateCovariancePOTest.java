/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases.filter.operator;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import de.uniol.inf.is.odysseus.filtering.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.KalmanCorrectStateCovariancePO;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.testcases.FilterPOTestData;


/**
 * @author dtwumasi
 *
 */
public class KalmanCorrectStateCovariancePOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	private KalmanCorrectStateCovariancePO correctStateCovariancePO;
	
	private MVRelationalTuple<M> measurementTuple;
		
		/**
		 * @throws java.lang.Exception
		 */
		@Before
		public void setUp() throws Exception {
		
			FilterPOTestData<M> measurement = new FilterPOTestData<M>();
			
			//this.measurementTuple = measurement.getTestData();
			
			KalmanCorrectStateCovarianceFunction correctStateCovarianceFunction = new KalmanCorrectStateCovarianceFunction();
		
			// create the PO
			
			correctStateCovariancePO = new KalmanCorrectStateCovariancePO();
			
			correctStateCovariancePO.setFilterFunction(correctStateCovarianceFunction);
			
			int[] oldObjListPath = {0};
			
			int[] newObjListPath = {1};
				
			correctStateCovariancePO.setOldObjListPath(oldObjListPath);
			
			correctStateCovariancePO.setNewObjListPath(newObjListPath);
		
		}
	
	
	
	@Test
	public void test() {
		
	}
}
