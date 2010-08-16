/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filtering.test.data;


import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * @author dtwumasi
 *
 */
public class FilterFunctionTestData {

	
	public FilterFunctionTestData() {
	
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
		
		if (gain!=null) metaOld.setGain(gain);
		
		oldTuple.setMetadata(metaOld);
		
		
		
		// MVRelationalTuple to hold the new data
		
		Object[] attributesNew = {speedNew,posNew};
		
		MVRelationalTuple<StreamCarsMetaData> newTuple = new MVRelationalTuple<StreamCarsMetaData>(attributesNew);
		
		StreamCarsMetaData metaNew = new StreamCarsMetaData();
		
		metaNew.setCovariance(covarianceNew);
		
		ArrayList<int[]> pathsNew = new ArrayList<int[]>();
		
		pathsNew.add(new int[] {0});
		pathsNew.add(new int[] {1});
		
		metaNew.setAttributePaths(pathsNew);
		
		newTuple.setMetadata(metaNew);
		
		// MVRelationalTuples to hold the Tuples
		
		MVRelationalTuple<StreamCarsMetaData> oldList = new MVRelationalTuple<StreamCarsMetaData>(1);
		oldList.setAttribute(0, oldTuple);
		
		MVRelationalTuple<StreamCarsMetaData> newList = new MVRelationalTuple<StreamCarsMetaData>(1);
		newList.setAttribute(0, newTuple);

		
		// the main MVRelationalTuple
		
		Object[] measurements = {newList,oldList};
		
		MVRelationalTuple<StreamCarsMetaData> measurementTuple = new MVRelationalTuple<StreamCarsMetaData>(measurements);
			
		// connections
		
		ConnectionList conList = new ConnectionList();
		
		Connection  con = new Connection (newTuple,oldTuple,5.0);
		
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