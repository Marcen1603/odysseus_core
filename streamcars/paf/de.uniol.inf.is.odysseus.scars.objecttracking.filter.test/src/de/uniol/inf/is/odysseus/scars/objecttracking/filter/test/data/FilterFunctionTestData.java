/**
 * 
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.test.data;


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
 * @param <K>
 *
 */
public class FilterFunctionTestData<K> {

	
	public FilterFunctionTestData() {
	
	}	
	
	public MVRelationalTuple<StreamCarsMetaData<K>> generateDataTuple(double speed, double pos, double[][] covariance, double[][] gain) {
		
		Object[] attributes = {speed,pos};
		
		// MVRelationalTuple to hold the data
		
		MVRelationalTuple<StreamCarsMetaData<K>> Tuple = new MVRelationalTuple<StreamCarsMetaData<K>>(attributes);
		
		
		StreamCarsMetaData<K> meta = new StreamCarsMetaData<K>();
		
		meta.setCovariance(covariance);
		
		ArrayList<int[]> paths = new ArrayList<int[]>();
		
		paths.add(new int[] {0});
		paths.add(new int[] {1});
		
		meta.setAttributePaths(paths);
		
		if (gain!=null) meta.setGain(gain);
		
		Tuple.setMetadata(meta);
		
		return Tuple;
	}
	
	public MVRelationalTuple<StreamCarsMetaData<K>> generateListTuple(Object[] list) {
		
		MVRelationalTuple<StreamCarsMetaData<K>> tupleList = new MVRelationalTuple<StreamCarsMetaData<K>>(list);
		
		return tupleList;
		
	}
	
	public MVRelationalTuple<StreamCarsMetaData<K>> generateTestTuple(double speedOld, double posOld, double[][] covarianceOld, double speedNew, double posNew, double[][] covarianceNew, double[][] gain, int number ) {
		
		
		Object [] oldAttributeList = new Object[number];
		
		Object [] newAttributeList = new Object[number];
		
		for (int i=0; i<=number-1; i++ ) {
			// MVRelationalTuple to hold the old data
			MVRelationalTuple<StreamCarsMetaData<K>> oldTuple = generateDataTuple(speedOld, posOld, covarianceOld, gain);
		
			oldAttributeList[i] = oldTuple;
		
			// MVRelationalTuple to hold the new data
			MVRelationalTuple<StreamCarsMetaData<K>> newTuple = generateDataTuple(speedNew, posNew, covarianceNew, null);
		
			newAttributeList[i] = newTuple;
		
		}
		
		
		MVRelationalTuple<StreamCarsMetaData<K>> oldList = generateListTuple(oldAttributeList);
		
		MVRelationalTuple<StreamCarsMetaData<K>> newList = generateListTuple(newAttributeList);
		
		// the main MVRelationalTuple
		
		Object[] measurements = {newList,oldList};
		
		MVRelationalTuple<StreamCarsMetaData<K>> measurementTuple = new MVRelationalTuple<StreamCarsMetaData<K>>(measurements);
			
		StreamCarsMetaData<K> streamCars= new StreamCarsMetaData<K>();
		
		// connections and path
		
		ConnectionList conList = new ConnectionList();
		
		ArrayList<int[]> pathsMeas = new ArrayList<int[]>();
		
		for (int i=0; i<=number-1; i++) {
		
			Connection  con = new Connection (newList.getAttribute(i),oldList.getAttribute(i),5.0);
		
			conList.add(i, con);
			
			pathsMeas.add(new int[] {i});
		}
		
		streamCars.setConnectionList(conList);
		
		streamCars.setAttributePaths(pathsMeas);
	
		
		measurementTuple.setMetadata(streamCars);
	
		return measurementTuple;
		
		}

	public SDFAttributeList getSchema() {
		
			SDFAttributeList object = new SDFAttributeList();
			object.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
			
			SDFAttribute oldList = new SDFAttribute("old.list");
			oldList.setDatatype(SDFDatatypeFactory.getDatatype("List"));
			
			SDFAttribute newList = new SDFAttribute("new.list");
			newList.setDatatype(SDFDatatypeFactory.getDatatype("List"));
			
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