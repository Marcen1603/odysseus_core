package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents a node in a hoeffding tree. It represents leafs and
 * inner nodes discriminated by a boolean
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class HoeffdingNode<T extends IMetaAttribute> implements IClassifier<T>,
		Comparable<HoeffdingNode<T>> {

	/**
	 * whether the node is a leaf
	 */
	private boolean leaf;;

	/**
	 * the data cube to store the class distributions
	 */
	private DataCube<T> dataCube;

	/**
	 * the evaluation measure to find the best attribute to split the node
	 */
	private AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure;

	/**
	 * the attributes the node can be split on
	 */
	private List<Integer> splitAttributes;

	/**
	 * the child nodes
	 */
	private List<HoeffdingNode<T>> children;

	/**
	 * the attribute the node has been split on
	 */
	private int testAttribute;

	/**
	 * the value of the parent's split attribute
	 */
	protected Object testValue;

	/**
	 * the relative frequency of the majority class in the class distribution
	 */
	private double relativeFrequency;

	/**
	 * the majority class of the node
	 */
	private Object majorityClass;

	/**
	 * the class distribution of the inner node
	 */
	private HashMap<Object, Integer> classCountVector;

	/**
	 * create a new hoeffding node
	 */
	public HoeffdingNode() {
	}

	/**
	 * create a new hoeffding node as a copy of another hoeffding node
	 * 
	 * @param hoeffdingNode
	 *            the node to copy
	 */
	public HoeffdingNode(HoeffdingNode<T> hoeffdingNode) {
		this.dataCube = hoeffdingNode.dataCube;
		this.children = hoeffdingNode.children;
		this.splitAttributes = hoeffdingNode.splitAttributes;
		this.classCountVector = hoeffdingNode.classCountVector;
		this.leaf = hoeffdingNode.leaf;
		this.testAttribute = hoeffdingNode.testAttribute;
		this.majorityClass = hoeffdingNode.majorityClass;
		this.relativeFrequency = hoeffdingNode.relativeFrequency;
		this.testValue = hoeffdingNode.testValue;
		this.attributeEvaluationMeasure = hoeffdingNode.attributeEvaluationMeasure;
	}

	/**
	 * add a tuple to the node
	 * 
	 * @param tuple
	 *            the tuple to add
	 * @return whether the tree has changed
	 */
	public boolean addTuple(RelationalClassificationObject<T> tuple) {
		if (leaf) {
			return addTupleToLeaf(tuple);
		} else {
			return addTupleToInnerNode(tuple);
		}
	}

	/**
	 * add a tuple to an inner node
	 * 
	 * @param tuple
	 *            the tuple to add
	 * @return whether the tree has changed
	 */
	private boolean addTupleToInnerNode(RelationalClassificationObject<T> tuple) {

		// add the tuple to the class distribution
		boolean changed = false;
		incrementClass(tuple.getClassLabel());
		Object newMajority = calculateMajorityClass(classCountVector);
		relativeFrequency = calculateRelativeFrequency(classCountVector);
		if (!newMajority.equals(majorityClass)) {

			// the majority class has changed
			majorityClass = newMajority;
			changed = true;
		}
		// get the child to add the tuple to
		HoeffdingNode<T> child = getChild(tuple);
		boolean newChild = false;
		if (child == null) {

			// create a new child because no fitting one exists
			newChild = true;
			child = createLeaf(
					tuple.getClassificationAttributes()[getTestAttribute()],
					newMajority, relativeFrequency);
			children.add(child);
		}

		// add the tuple to the fitting child
		boolean childChanged = child.addTuple(tuple);
		return changed || childChanged || newChild;
	}

	/**
	 * add a tuple to a leaf node
	 * 
	 * @param tuple
	 *            the tuple to add
	 * @return whether the tree has changed
	 */
	private boolean addTupleToLeaf(RelationalClassificationObject<T> tuple) {

		// add the tuple
		boolean majorityChanged = false;
		dataCube.addTuple(tuple, splitAttributes);
		Object cubeMajorityClass = dataCube.getMajorityClass();
		relativeFrequency = calculateRelativeFrequency(dataCube
				.getClassCountVector());

		if (!cubeMajorityClass.equals(majorityClass)) {
			// the majority class has changed

			majorityClass = cubeMajorityClass;
			majorityChanged = true;
		}

		// split the node if possible
		boolean splitted = split();
		return majorityChanged || splitted;
	}

	/**
	 * calculate the majority class of a class distribution
	 * 
	 * @param dataVector
	 *            the class distribution
	 * @return the majority class
	 */
	public Object calculateMajorityClass(HashMap<Object, Integer> dataVector) {
		int maxCount = 0;
		Object majorityClass = null;
		for (Entry<Object, Integer> entry : dataVector.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxCount = entry.getValue();
				majorityClass = entry.getKey();
			}
		}
		return majorityClass;
	}

	/**
	 * calculate the relative frequency that a tuple belongs to the majority
	 * class
	 * 
	 * @param dataVector
	 *            the class distribution
	 * @return the relative frequency
	 */
	public double calculateRelativeFrequency(HashMap<Object, Integer> dataVector) {
		int count = 0;
		for (Entry<Object, Integer> entry : dataVector.entrySet()) {
			count += entry.getValue();
		}
		return ((double) dataVector.get(calculateMajorityClass(dataVector)))
				/ count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public HoeffdingNode<T> clone() {
		return new HoeffdingNode<T>(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int compareTo(HoeffdingNode<T> node) {
		if (node.testValue instanceof Comparable) {
			return ((Comparable) testValue).compareTo(node.getTestValue());
		} else {
			return 0;
		}
	}

	/**
	 * create a leaf for a given value
	 * 
	 * @param compareValue
	 *            he value of the split attribute
	 * @param majorityClass
	 *            the childs majority class
	 * @param relativeFrequency
	 *            the relative frequency of the majority class
	 * @return
	 */
	private HoeffdingNode<T> createLeaf(Object compareValue,
			Object majorityClass, double relativeFrequency) {
		HoeffdingNode<T> leaf = new HoeffdingNode<T>();
		leaf.setLeaf(true);
		leaf.setAttributeEvaluationMeasure(attributeEvaluationMeasure);
		leaf.setMajorityClass(majorityClass);
		leaf.setRelativeFrequency(relativeFrequency);
		leaf.setTestValue(compareValue);
		leaf.setSplitAttributes(new ArrayList<Integer>(splitAttributes));
		leaf.getSplitAttributes().remove((Object) testAttribute);
		leaf.initDataCube(splitAttributes.size() - 1);
		return leaf;
	}

	/**
	 * get the evaluation measure
	 * 
	 * @return the evaluation measure
	 */
	public AbstractAttributeEvaluationMeasure<T> getAttributeEvaluationMeasure() {
		return attributeEvaluationMeasure;
	}

	/**
	 * get the child node fitting to a given tuple
	 * 
	 * @param tuple
	 *            the tuple to find a fitting node
	 * @return the fitting child, null if no fitting child exists
	 */
	private HoeffdingNode<T> getChild(RelationalClassificationObject<T> tuple) {
		HoeffdingNode<T> path = null;
		Object compareValue = tuple.getClassificationAttributes()[testAttribute];
		for (HoeffdingNode<T> child : children) {
			if (compareValue.equals(child.getTestValue())) {
				path = child;
				break;
			}
		}
		return path;
	}

	/**
	 * get the child nodes
	 * 
	 * @return the child nodes
	 */
	public List<HoeffdingNode<T>> getChildren() {
		return children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.datamining.classification.IClassifier#getClassLabel
	 * (de.uniol.inf.is.odysseus.datamining.classification.
	 * RelationalClassificationObject)
	 */
	@Override
	public Object getClassLabel(RelationalClassificationObject<T> tuple) {
		if (leaf) {
			return majorityClass;
		} else {
			HoeffdingNode<T> path = getChild(tuple);

			return path == null ? majorityClass : path.getClassLabel(tuple);
		}
	}

	/**
	 * get the data cube containing the class distributions
	 * 
	 * @return the data cube
	 */
	public DataCube<T> getDataCube() {
		return dataCube;
	}

	/**
	 * get a snapshot of an inner node
	 * 
	 * @return the snapshot
	 */
	private HoeffdingNode<T> getInnerNodeSnapshot() {
		HoeffdingNode<T> node = new HoeffdingNode<T>();
		node.setLeaf(false);
		node.setTestAttribute(getTestAttribute());
		node.setTestValue(getTestValue());
		node.setMajorityClass(getMajorityClass());
		node.setRelativeFrequency(getRelativeFrequancy());
		List<HoeffdingNode<T>> nodeChildren = new ArrayList<HoeffdingNode<T>>();
		for (HoeffdingNode<T> child : children) {
			nodeChildren.add(child.getSnapshot());
		}
		node.setChildren(nodeChildren);
		return node;
	}

	/**
	 * get a snapshot of a leaf node
	 * 
	 * @return the snapshot
	 */
	private HoeffdingNode<T> getLeafSnapshot() {
		HoeffdingNode<T> node = new HoeffdingNode<T>();
		node.setLeaf(true);
		node.setTestValue(getTestValue());
		node.setMajorityClass(getMajorityClass());
		node.setRelativeFrequency(getRelativeFrequancy());
		return node;
	}

	/**
	 * get the nodes majority class
	 * 
	 * @return the majority class
	 */
	public Object getMajorityClass() {
		return majorityClass;
	}

	/**
	 * get the relative frequency of the majority class in the class
	 * distribution
	 * 
	 * @return the relative frequency of the majority class
	 */
	public double getRelativeFrequancy() {
		return relativeFrequency;
	}

	/**
	 * get a snapshot of the node
	 * 
	 * @return the snapshot
	 */
	public HoeffdingNode<T> getSnapshot() {
		if (leaf) {
			return getLeafSnapshot();
		} else {
			return getInnerNodeSnapshot();
		}
	}

	/**
	 * get the attributes possible to split the node
	 * 
	 * @return he attribute positions
	 */
	public List<Integer> getSplitAttributes() {
		return splitAttributes;
	}

	/**
	 * get the attribute used to splizt the node
	 * 
	 * @return the position of the attribute
	 */
	public int getTestAttribute() {
		return testAttribute;
	}

	/**
	 * get the value of the parent's split attribute
	 * 
	 * @return the split value
	 */
	public Object getTestValue() {
		return testValue;
	}

	/**
	 * increment the class distribution of an inner node
	 * 
	 * @param objectClass
	 *            the class to increment the distribution
	 */
	private void incrementClass(Object objectClass) {
		Integer classCount = classCountVector.get(objectClass);
		if (classCount == null) {
			classCount = 0;
		}
		classCountVector.put(objectClass, classCount + 1);
	}

	/**
	 * initialize the data cube
	 * 
	 * @param length
	 *            the number of possible split attributes
	 */
	public void initDataCube(int length) {
		dataCube = new DataCube<T>(length);
	}

	/**
	 * determine whether the node is a leaf
	 * 
	 * @return whether the node is a leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * set the evaluation measure to be used to get the best splitting attribute
	 * 
	 * @param attributeEvaluationMeasure
	 *            the evaluation measure to set
	 */
	public void setAttributeEvaluationMeasure(
			AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;
	}

	/** set the child nodes
	 * @param children the child nodes to set
	 */
	public void setChildren(List<HoeffdingNode<T>> children) {
		this.children = children;
	}

	/**set the data cube
	 * @param dataCube the data cube to set
	 */
	public void setDataCube(DataCube<T> dataCube) {
		this.dataCube = dataCube;
	}

	/**set whether the node is a leaf
	 * @param leaf the node is a leaf?
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	/** set the majority class of the node
	 * @param majorityClass the majority class to set
	 */
	public void setMajorityClass(Object majorityClass) {
		this.majorityClass = majorityClass;
	}

	/**
	 * set the relative frequency of the majority class in the class
	 * distribution
	 * 
	 * @param relativeFrequency
	 *            the relative frequency to set
	 */
	public void setRelativeFrequency(double relativeFrequency) {
		this.relativeFrequency = relativeFrequency;
	}

	/** set the attributes that are possible to split the node
	 * @param splitAttributes the positions of the attributes
	 */
	public void setSplitAttributes(List<Integer> splitAttributes) {
		this.splitAttributes = splitAttributes;
	}

	/**set the attribute used to split the node
	 * @param testAttribute the position of the attribute
	 */
	public void setTestAttribute(int testAttribute) {
		this.testAttribute = testAttribute;
	}

	/**set the value of the parent's split attribute
	 * @param testValue the split value
	 */
	public void setTestValue(Object testValue) {
		this.testValue = testValue;
	}

	/**
	 * split the node if possible
	 * 
	 * @return whether the node has been split
	 */
	private boolean split() {

		// get the split attribute
		Integer splitAttribute = attributeEvaluationMeasure
				.getSplitAttribute(dataCube);

		if (splitAttribute == null) {
			// do not split the node
			return false;
		} else {
			// set the split attribute
			testAttribute = splitAttributes.get(splitAttribute);

			// create the child nodes
			children = new ArrayList<HoeffdingNode<T>>();
			for (Entry<Object, HashMap<Object, Integer>> attributeEntry : dataCube
					.getClassCountLayer(splitAttribute).entrySet()) {
				children.add(createLeaf(attributeEntry.getKey(),
						calculateMajorityClass(attributeEntry.getValue()),
						calculateRelativeFrequency(attributeEntry.getValue())));
			}

			// transform the node into an inner node
			classCountVector = dataCube.getClassCountVector();
			dataCube = null;
			setLeaf(false);

			return true;
		}
	}

}
