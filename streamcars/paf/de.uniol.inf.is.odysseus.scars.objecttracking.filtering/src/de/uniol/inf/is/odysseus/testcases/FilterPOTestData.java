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
public class FilterPOTestData<M extends IProbability & IConnectionContainer> {

	
	
	private MVRelationalTuple<M> expectedTuple;
	
	public FilterPOTestData() {
	
		
		//this.expectedTuple=generateExpectedTuple(this.testTuple.clone());
		//	double speedOld = 0.98;
		//	double posOld = 1.83;
		//double[][] covarianceOld = { {-10.320000000000002,26.12}, {26.12,0.48} };
		// double[][] covarianceOld = { {5.0,50.0}, {50.0,10.0} };
		//double[][] gain = { {0.7064220183486238,-0.009174311926605505}, {-0.02854230377166156,0.7074413863404688 }};
		// double speedNew = 1.0;
	//	double posNew = 2.0;
	// //	double[][] covarianceNew = { {3.0,21.0}, {21,7.0} };
	}
		
	public MVRelationalTuple generateTestTuple(double speedOld, double posOld, double[][] covarianceOld, double speedNew, double posNew, double[][] covarianceNew ) {
		
		Object[] attributesOld = {speedOld,posOld};
		
		// MVRelationalTuple to hold the data
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = new MVRelationalTuple<StreamCarsMetaData>(attributesOld);
		
		
		StreamCarsMetaData metaOld = new StreamCarsMetaData();
		
		metaOld.setCovariance(covarianceOld);
		
		oldTuple.setMetadata(metaOld);		
					
		Object[] attributesNew = {speedNew,posNew};
		
		// MVRelationalTuple to hold the new data
		MVRelationalTuple<StreamCarsMetaData> newTuple = new MVRelationalTuple<StreamCarsMetaData>(attributesNew);
		
		
		StreamCarsMetaData metaNew = new StreamCarsMetaData();
		
		metaNew.setCovariance(covarianceNew);
		
		newTuple.setMetadata(metaNew);
		
		// MVRelationalTuples to hold the old Tuples
		
		MVRelationalTuple<M> oldList = new MVRelationalTuple<M>(oldTuple);
			
		MVRelationalTuple<M> newList = new MVRelationalTuple<M>(newTuple);
		
		// the main MVRelationalTuple
		
		Object[] measurements = {newList,oldList};
		
		MVRelationalTuple<StreamCarsMetaData> measurementTuple = new MVRelationalTuple<StreamCarsMetaData>(measurements);
			
		// connections
		
		ConnectionList conList = new ConnectionList();
		
		Connection  con = new Connection (oldTuple,newTuple,5.0);
		
		conList.add(0, con);
	
		StreamCarsMetaData streamCars= new StreamCarsMetaData(conList);
		
		measurementTuple.setMetadata(streamCars);
	
		
		
		return measurementTuple;

		}

}