package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents the data cube used to store the clss distribution
 * statistics belonging to a hoeffding node
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class DataCube<T extends IMetaAttribute> {

	/**
	 * the data cube
	 */
	private List<HashMap<Object, HashMap<Object, Integer>>> cube;

	/**
	 * the class distribution of the unpartitioned node
	 */
	private HashMap<Object, Integer> classCountVector;

	/**
	 * the number of attributes the class distributions are stored for
	 */
	private int attributeCount;

	/**
	 * create a data cube as a copy of another data cube
	 * 
	 * @param dataCube
	 *            the data cube to copy
	 */
	@SuppressWarnings("unchecked")
	public DataCube(DataCube<T> dataCube) {
		this(dataCube.attributeCount);
		classCountVector = (HashMap<Object, Integer>) dataCube.classCountVector
				.clone();
		for (int i = 0; i < attributeCount; i++) {
			copy(cube.get(i), dataCube.cube.get(i));
		}

	}

	/**
	 * create a data cube to tore the class distributions for a given number of
	 * attributes
	 * 
	 * @param attributeCount
	 *            the number of attributes
	 */
	public DataCube(int attributeCount) {
		this.attributeCount = attributeCount;
		cube = new ArrayList<HashMap<Object, HashMap<Object, Integer>>>();
		for (int i = 0; i < attributeCount; i++) {
			cube.add(new HashMap<Object, HashMap<Object, Integer>>());
		}
		classCountVector = new HashMap<Object, Integer>();
	}

	/**
	 * add a tuple to the data cube
	 * 
	 * @param tuple
	 *            the to add
	 * @param splitAttributes
	 *            the list of attributes that are stored in the data cube
	 */
	public void addTuple(RelationalClassificationObject<T> tuple,
			List<Integer> splitAttributes) {
		// increment the class distrubution of the unpartitioned node
		incrementClass(tuple.getClassLabel());

		// increment the class distributions of the attributes
		for (int i = 0; i < splitAttributes.size(); i++) {
			incrementValue(
					i,
					tuple.getClassificationAttributes()[splitAttributes.get(i)],
					tuple.getClassLabel());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public DataCube<T> clone() {
		return new DataCube<T>(this);
	}

	/**
	 * deep copy a data cube
	 * 
	 * @param destination
	 *            the destination cube
	 * @param source
	 *            the source cube to copy
	 */
	@SuppressWarnings("unchecked")
	private void copy(HashMap<Object, HashMap<Object, Integer>> destination,
			HashMap<Object, HashMap<Object, Integer>> source) {
		
		for (Entry<Object, HashMap<Object, Integer>> entry : source.entrySet()) {
			destination.put(entry.getKey(), (HashMap<Object, Integer>) entry
					.getValue().clone());
		}

	}

	/**
	 * get the number of attributes the class distributions are stored for
	 * 
	 * @return the number of attributes
	 */
	public int getAttributeCount() {
		return attributeCount;
	}

	/**
	 * get the class distributions of the partitions created by a given
	 * attribute
	 * 
	 * @param attributeId
	 *            the number of the attribute in the cube
	 * @return
	 */
	public HashMap<Object, HashMap<Object, Integer>> getClassCountLayer(
			Integer attributeId) {
		return cube.get(attributeId);
	}

	/**
	 * get the class distribution of the unpartitioned node
	 * 
	 * @return the class distribution
	 */
	public HashMap<Object, Integer> getClassCountVector() {
		return classCountVector;
	}

	/**
	 * get the class distribution of a partition belonging to a given attribute
	 * value
	 * 
	 * @param attributeId
	 *            the number of the attribute
	 * @param attributeValue
	 *            the value of the attribute
	 * @return teh class distribution
	 */
	private HashMap<Object, Integer> getClassVector(Integer attributeId,
			Object attributeValue) {
		HashMap<Object, HashMap<Object, Integer>> layer = cube.get(attributeId);
		HashMap<Object, Integer> vector = layer.get(attributeValue);
		
		// create a new class distribution if non available
		if (vector == null) {
			vector = new HashMap<Object, Integer>();
			layer.put(attributeValue, vector);
		}
		return vector;
	}

	/**
	 * get the majortiy class of the data cube
	 * 
	 * @return the majority class
	 */
	public Object getMajorityClass() {
		int maxCount = 0;
		Object majorityClass = null;
		for (Entry<Object, Integer> entry : classCountVector.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxCount = entry.getValue();
				majorityClass = entry.getKey();
			}
		}
		return majorityClass;
	}

	/**
	 * increment the class distribution of the unpartitioned node
	 * 
	 * @param objectClass
	 */
	private void incrementClass(Object objectClass) {
		Integer classCount = classCountVector.get(objectClass);
		if (classCount == null) {
			classCount = 0;
		}
		classCountVector.put(objectClass, classCount + 1);
	}

	/**
	 * increment the number of tuples seen with a given attribute value
	 * combination and a given class
	 * 
	 * @param attributeId
	 *            the number of the attribute
	 * @param attributeValue
	 *            the value of the attribute
	 * @param objectClass
	 *            the class belonging to the attribute value combination
	 */
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

}
