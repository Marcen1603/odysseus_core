package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class DataCube<T extends IMetaAttribute> {

	private List<HashMap<Object, HashMap<Object, Integer>>> cube;

	private HashMap<Object, Integer> classCountVector;

	private int attributeCount;

@Override
public DataCube<T> clone() {
	return new DataCube<T>(this);
}
	
	
	public DataCube(int attributeCount) {
		this.attributeCount = attributeCount;
		cube = new ArrayList<HashMap<Object, HashMap<Object, Integer>>>();
		for (int i = 0; i < attributeCount; i++) {
			cube.add(new HashMap<Object, HashMap<Object, Integer>>());
		}
		classCountVector = new HashMap<Object, Integer>();
	}
	
	@SuppressWarnings("unchecked")
	public DataCube(DataCube<T> dataCube) {
		this(dataCube.attributeCount);
		classCountVector = (HashMap<Object, Integer>) dataCube.classCountVector.clone();
		for(int i = 0 ; i < attributeCount; i++){
			copy(cube.get(i), dataCube.cube.get(i));
		}
		
	}


	@SuppressWarnings("unchecked")
	private void copy(HashMap<Object, HashMap<Object, Integer>> destination,
			HashMap<Object, HashMap<Object, Integer>> source) {
		for(Entry<Object, HashMap<Object, Integer>> entry:source.entrySet()){
			destination.put(entry.getKey(), (HashMap<Object, Integer>) entry.getValue().clone());
		}
		
	}


	public int getAttributeCount() {
		return attributeCount;
	}

	public HashMap<Object, Integer> getClassCountVector(){
		return classCountVector;
	}
	
	public HashMap<Object, HashMap<Object, Integer>> getClassCountLayer(Integer attributeId){
		return cube.get(attributeId);
	}

	public void addTuple(RelationalClassificationObject<T> tuple,
			List<Integer> splitAttributes) {
		incrementClass(tuple.getClassLabel());
		for (int i = 0; i < splitAttributes.size(); i++) {
			incrementValue(i, tuple.getClassificationAttributes()[splitAttributes.get(i)],
					tuple.getClassLabel());
		}
	}
	
	public Object getMajorityClass(){
		int maxCount = 0;
		Object majorityClass = null;
		for(Entry<Object, Integer> entry: classCountVector.entrySet()){
			if(entry.getValue()>maxCount){
				maxCount = entry.getValue();
				majorityClass = entry.getKey();
			}
		}
		return majorityClass;
	}

	private void incrementClass(Object objectClass) {
		Integer classCount = classCountVector.get(objectClass);
		if (classCount == null) {
			classCount = 0;
		}
		classCountVector.put(objectClass, classCount + 1);
	}

	private void incrementValue(Integer attributeId, Object attributeValue,
			Object objectClass) {
		Map<Object, Integer> vector = getClassVector(attributeId,
				attributeValue);
		Integer classCount = vector.get(objectClass);
		if (classCount == null) {
			classCount = 0;
		}
		vector.put(objectClass, classCount + 1);
	}

	private HashMap<Object, Integer> getClassVector(Integer attributeId,
			Object attributeValue) {
		HashMap<Object, HashMap<Object, Integer>> layer = cube.get(attributeId);
		HashMap<Object, Integer> vector = layer.get(attributeValue);
		if (vector == null) {
			vector = new HashMap<Object, Integer>();
			layer.put(attributeValue, vector);
		}
		return vector;
	}


}
