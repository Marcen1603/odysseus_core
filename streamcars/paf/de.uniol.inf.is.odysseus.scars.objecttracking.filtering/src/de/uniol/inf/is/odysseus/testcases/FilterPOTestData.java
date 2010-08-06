/**
 * 
 */
package de.uniol.inf.is.odysseus.testcases;


import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author dtwumasi
 *
 */
public class FilterPOTestData {

	
	
	private MVRelationalTuple<StreamCarsMetaData> expectedTuple;
	
	public FilterPOTestData() {
	
	}	
	
		
	public MVRelationalTuple<StreamCarsMetaData> generateTestTuple(double speedOld, double posOld, double[][] covarianceOld, double speedNew, double posNew, double[][] covarianceNew, double[][] gain ) {
		
		Object[] attributesOld = {speedOld,posOld};
		
		// MVRelationalTuple to hold the data
		
		MVRelationalTuple<StreamCarsMetaData> oldTuple = new MVRelationalTuple<StreamCarsMetaData>(attributesOld);
		
		
		StreamCarsMetaData metaOld = new StreamCarsMetaData();
		
		metaOld.setCovariance(covarianceOld);
		
		ArrayList<int[]> pathsOld = new ArrayList<int[]>();
		
		pathsOld.add(new int[] {0});
		pathsOld.add(new int[] {1});
		
		metaOld.setAttributePaths(pathsOld);
		
		oldTuple.setMetadata(metaOld);
		
		
		Object[] attributesNew = {speedNew,posNew};
		
		// MVRelationalTuple to hold the new data
		MVRelationalTuple<StreamCarsMetaData> newTuple = new MVRelationalTuple<StreamCarsMetaData>(attributesNew);
		
		
		StreamCarsMetaData metaNew = new StreamCarsMetaData();
		
		metaNew.setCovariance(covarianceNew);
		
		ArrayList<int[]> pathsNew = new ArrayList<int[]>();
		
		pathsNew.add(new int[] {0});
		pathsNew.add(new int[] {1});
		
		metaNew.setAttributePaths(pathsNew);
		
		if (gain!=null) metaNew.setGain(gain);
		
		newTuple.setMetadata(metaNew);
		
		// MVRelationalTuples to hold the old Tuples
		
		MVRelationalTuple<StreamCarsMetaData> oldList = new MVRelationalTuple<StreamCarsMetaData>(oldTuple);
			
		MVRelationalTuple<StreamCarsMetaData> newList = new MVRelationalTuple<StreamCarsMetaData>(newTuple);
		
		// the main MVRelationalTuple
		
		Object[] measurements = {newList,oldList};
		
		MVRelationalTuple<StreamCarsMetaData> measurementTuple = new MVRelationalTuple<StreamCarsMetaData>(measurements);
			
		// connections
		
		ConnectionList conList = new ConnectionList();
		
		Connection  con = new Connection (oldTuple,newTuple,5.0);
		
		conList.add(0, con);
	
		StreamCarsMetaData streamCars= new StreamCarsMetaData();
		
		ArrayList<int[]> pathsMeas = new ArrayList<int[]>();
		
		pathsMeas.add(new int[] {0});
		pathsMeas.add(new int[] {1});
		
		streamCars.setAttributePaths(pathsMeas);
		streamCars.setConnectionList(conList);
		
		measurementTuple.setMetadata(streamCars);
	
		
		
		return measurementTuple;
		
		}

	public SDFAttributeList getSchema() {
		
			SDFAttributeList object = new SDFAttributeList();
		
			SDFAttribute oldList = new SDFAttribute("old.list");
			oldList.setDatatype(SDFDatatypeFactory.getDatatype("List"));
			
			SDFAttribute newList = new SDFAttribute("new.list");
			oldList.setDatatype(SDFDatatypeFactory.getDatatype("List"));
			
			SDFAttribute newObject = new SDFAttribute("NewObject");
			newObject.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
			
			SDFAttribute oldObject = new SDFAttribute("OldObject");
			oldObject.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
			
			SDFAttribute posOld = new SDFAttribute("posOld");
			posOld.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
	
			
			SDFAttribute speedOld = new SDFAttribute("speedOld");
			speedOld.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
			
			SDFAttribute posNew = new SDFAttribute("posNew");
			posNew.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
	
			
			SDFAttribute speedNew = new SDFAttribute("speedNew");
			speedNew.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
			
			
		
			
			newObject.addSubattribute(posNew);
			newObject.addSubattribute(speedNew);
			
			newList.addSubattribute(newObject);
			
			object.add(newList);
			
			
			
			
			
			oldObject.addSubattribute(speedOld);
			oldObject.addSubattribute(posOld);
			
			oldList.addSubattribute(oldObject);
		
			object.add(oldList);
			
			
			return object;
		}
		
		
		
	

}