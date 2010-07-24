/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases;


import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;

/**
 * @author dtwumasi
 *
 */
public class FilterAOTestData<M extends IGain & IProbability & IConnectionContainer<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>> {

	private MVRelationalTuple<M> testTuple;
	
	public FilterAOTestData() {
	
		this.testTuple = generateTestData();	
	
	}

	private MVRelationalTuple<M> generateTestData() {
		
		
		// attributes for the old object
		
		double speedOld = 0.9;
		double posOld = 1.7;
		
		Object[] attributesOld = {speedOld,posOld};
		
		// MVRelationalTuple to hold the data
		
		MVRelationalTuple<M> oldTuple = new MVRelationalTuple<M>(attributesOld);
		
		// set positions
		oldTuple.setMeasurementValuePositions(new int[] {0,1});
		
		// covariance
		double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		
		StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>,Double> metaold = new StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>, Double>();
		
		metaold.setCovariance(covarianceOld);
		
	//	oldTuple.setMetadata(metaold);		
					
		// attributes for the new object
		
		double speedNew = 1.0;
		double posNew = 2.0;
		
		Object[] attributesNew = {speedNew,posNew};
		
		// MVRelationalTuple to hold the new data
		MVRelationalTuple<M> newTuple = new MVRelationalTuple<M>(attributesNew);
		
		// set positions
		newTuple.setMeasurementValuePositions(new int[] {0,1});
		
		// the covariance of the new measurement
		double[][] covarianceNew = { {3.0,21.0}, {21,7.0} };
		
		StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>,Double> metanew = new StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>,Double>();
		
		metanew.setCovariance(covarianceNew);
		
		//newTuple.setMetadata(metanew);
		
		// MVRelationalTuples to hold the old Tuples
		
		MVRelationalTuple<M> oldList = new MVRelationalTuple<M>(oldTuple);
		
		oldList.setMeasurementValuePositions(new int[] {0});
		
		MVRelationalTuple<M> newList = new MVRelationalTuple<M>(newTuple);
		
		newList.setMeasurementValuePositions(new int[] {0});
		
		// the main MVRelationalTuple
		
		Object[] measurements = {newList,oldList};
		
		MVRelationalTuple<M> measurementTuple = new MVRelationalTuple<M>(measurements);
		
		measurementTuple.setMeasurementValuePositions(new int[] {0,1});
		
		// connections
		
		ConnectionList<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> conList = new ConnectionList<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>();
		
		Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double>  con = new Connection<MVRelationalTuple<M>, MVRelationalTuple<M>, Double> (oldTuple,newTuple,5.0);
		
		conList.add(0, con);
	
		StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>,Double> streamCars= new StreamCarsMetaData<MVRelationalTuple<M>,MVRelationalTuple<M>,Double>(conList);
		
		//measurementTuple.setMetadata((M) streamCars);
	
		return measurementTuple;
		
		
		
		
	}

	/**
	 * @return the testData
	 */
	public MVRelationalTuple<M> getTestData() {
		return testTuple;
	}

}
