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
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

import de.uniol.inf.is.odysseus.filtering.HashConstants;
import de.uniol.inf.is.odysseus.filtering.IGainFunction;
import de.uniol.inf.is.odysseus.filtering.KalmanGainFunction;
import de.uniol.inf.is.odysseus.filtering.physicaloperator.GainFunctionPO;
import de.uniol.inf.is.odysseus.metadata.base.MetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;


/**
 * @author dtwumasi
 * @param <T>
 * @param <M>
 *
 */
public class GainFunctionPOTest<C extends IProbability & IConnectionContainer<MVRelationalTuple<C>,MVRelationalTuple<C>, Double>, M extends IProbability>{

	private GainFunctionPO gainfunctionPO;
	

	
	private MVRelationalTuple<C> measurementTuple;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		

		
		// attributes for the old object
		
		Object[] attributesOld = new Object[2];
		
		
		double speedOld = 0.9;
		double posOld = 1.7;
		
		attributesOld[0] = speedOld;
		attributesOld[1] = posOld;
		
		MVRelationalTuple<M> oldTuple = new MVRelationalTuple<M>(attributesOld);
		
		// set positions
		int[] mvPosOld = {0,1};
		oldTuple.setMeasurementValuePositions(mvPosOld);
		
		// covariance
		double[][] covarianceOld = { {5.0,15.0}, {15.0,10.0} };
		
		Probability POld = new Probability(covarianceOld);
		
		oldTuple.setMetadata((M) POld);
		
		// attributes for the new object
		
		Object[] attributesNew = new Object[2];
		
		double speedNew = 1.0;
		double posNew = 2.0;
		
		attributesNew[0] = speedNew;
		attributesNew[1] = posNew;
		
		MVRelationalTuple<M> newTuple = new MVRelationalTuple<M>(attributesNew);
		
		// set positions
		int[] mvPosNew = {0,1};
		newTuple.setMeasurementValuePositions(mvPosNew);
		
		// the covariance of the new measurement
		double[][] covarianceNew = { {3.0,21.0}, {21,7.0} };
		
		Probability PNew = new Probability(covarianceNew);
		
		oldTuple.setMetadata((M) PNew);
		
		// the main MVRelationalTuple
		
		Object[] measurements = {newTuple,oldTuple};
		
		measurementTuple = new MVRelationalTuple<C>(measurements);
		
		// connections
		
		ConnectionList<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> conList = new ConnectionList<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>();
		
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>  con = new Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> (oldTuple,newTuple,5.0);
		
		conList.add(0, con);
	
		StreamCarsMetaData<MVRelationalTuple,MVRelationalTuple,Double> streamCars= new StreamCarsMetaData(conList);
		
		measurementTuple.setMetadata((C) streamCars);
	
		
		
		HashMap<Integer,Object> parameters = new HashMap<Integer,Object>();;
		
		parameters.put(HashConstants.OLD_COVARIANCE, covarianceOld);
		parameters.put(HashConstants.NEW_COVARIANCE,covarianceNew);
		
		
		KalmanGainFunction gainfunction = new KalmanGainFunction();
		
		gainfunction.setParameters(parameters);
		
		
		gainfunctionPO = new GainFunctionPO();
		
		gainfunctionPO.setGainFunction(gainfunction);
		
		int[] oldObjListPath = {0};
		
		int[] newObjListPath = {0};
			
		gainfunctionPO.setOldObjListPath(oldObjListPath);
		
		gainfunctionPO.setNewObjListPath(newObjListPath);
	
	
	}
	
	@Test
	public  void test() {
	
		
	
	
	gainfunctionPO.process_next(measurementTuple, 0);
	// the old measurement

	double[] measurementOld = {0.9,1.7};
	
		
	// the new measurement
	double[] measurementNew = {1.0,2.0};
	
	
	
	
	//MVRelationalTuple<M> = new MVRelationalTuple<M>;
	double[][] test;
	//test = oldTuple.getMetadata().getCovariance();
	
	double[] expected;
	//expected = oldTuple.getMeasurementValues();
	//assertEquals(expected[0],measurementOld[0],0.0);
	}
	
}
