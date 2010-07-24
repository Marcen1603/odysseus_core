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
import de.uniol.inf.is.odysseus.testcases.FilterAOTestData;
import de.uniol.inf.is.odysseus.testcases.FilterFunctionTestData;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.GainFunctionPO;
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
public class GainFunctionPOTest<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	private GainFunctionPO gainfunctionPO;
	

	
	private MVRelationalTuple<M> measurementTuple;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		FilterAOTestData<M> measurement = new FilterAOTestData<M>();
		
		this.measurementTuple = measurement.getTestData();
		
		KalmanGainFunction gainfunction = new KalmanGainFunction();
	
		// create the PO
		
		gainfunctionPO = new GainFunctionPO();
		
		gainfunctionPO.setGainFunction(gainfunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {1};
			
		gainfunctionPO.setOldObjListPath(oldObjListPath);
		
		gainfunctionPO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void test() {
	
	gainfunctionPO.process_next(measurementTuple, 0);
	
	

	}
	
}
