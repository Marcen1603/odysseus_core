package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public class HoeffdingNode<T extends IMetaAttribute> implements IClassifier<T> {

	private boolean leaf;;

	private DataCube<T> dataCube;

	private AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure;

	private List<Integer> splitAttributes;

	private List<HoeffdingNode<T>> children;

	private int testAttribute;

	protected Object testValue;

	private Object majorityClass;

	public HoeffdingNode() {
	}

	public HoeffdingNode(HoeffdingNode<T> hoeffdingNode) {
		this.dataCube = hoeffdingNode.dataCube;
		this.children = hoeffdingNode.children;
		this.splitAttributes = hoeffdingNode.splitAttributes;
		this.classCountVector = hoeffdingNode.classCountVector;
		this.leaf = hoeffdingNode.leaf;
		this.testAttribute = hoeffdingNode.testAttribute;
		this.majorityClass = hoeffdingNode.majorityClass;
		this.testValue = hoeffdingNode.testValue;
		this.attributeEvaluationMeasure = hoeffdingNode.attributeEvaluationMeasure;
	}

	public boolean addTuple(RelationalClassificationObject<T> tuple) {
		if (leaf) {
			return addTupleToLeaf(tuple);
		} else {
			return addTupleToInnerNode(tuple);
		}
	}

	private void incrementClass(Object objectClass) {
		Integer classCount = classCountVector.get(objectClass);
		if (classCount == null) {
			classCount = 0;
		}
		classCountVector.put(objectClass, classCount + 1);
	}

	public Object calculateMajorityClass() {
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

	private boolean addTupleToInnerNode(RelationalClassificationObject<T> tuple) {
		boolean changed = false;
		incrementClass(tuple.getClassLabel());
		Object newMajority = calculateMajorityClass();
		if (!newMajority.equals(majorityClass)) {
			majorityClass = newMajority;
			changed = true;
		}
		return changed || getChild(tuple, true).addTuple(tuple);
	}

	private HoeffdingNode<T> getChild(RelationalClassificationObject<T> tuple,
			boolean create) {
		HoeffdingNode<T> path = null;
		Object compareValue = tuple.getClassificationAttributes()[testAttribute];
		for (HoeffdingNode<T> child : children) {
			if (compareValue.equals(child.getTestValue())) {
				path = child;
				break;
			}
		}
		if (path == null && create) {
			System.out.println("append " + compareValue);
			path = createLeaf(compareValue);
		}

		return path;
	}

	private HoeffdingNode<T> createLeaf(Object compareValue) {
		HoeffdingNode<T> leaf = new HoeffdingNode<T>();
		leaf.setLeaf(true);
		leaf.setAttributeEvaluationMeasure(attributeEvaluationMeasure);
		leaf.setMajorityClass(getMajorityClass());
		leaf.setTestValue(compareValue);
		leaf.setSplitAttributes(new ArrayList<Integer>(splitAttributes));
		leaf.getSplitAttributes().remove((Object) testAttribute);
		leaf.initDataCube(splitAttributes.size() - 1);
		return leaf;
	}

	private boolean addTupleToLeaf(RelationalClassificationObject<T> tuple) {
		boolean majorityChanged = false;
		dataCube.addTuple(tuple, splitAttributes);
		Object cubeMajorityClass = dataCube.getMajorityClass();
		if (!cubeMajorityClass.equals(majorityClass)) {
			majorityClass = cubeMajorityClass;
			majorityChanged = true;
		}
		return majorityChanged || split();
	}

	private boolean split() {
		Integer splitAttribute = attributeEvaluationMeasure
				.getSplitAttribute(dataCube);
		if (splitAttribute == null) {
			return false;
		} else {
			testAttribute = splitAttributes.get(splitAttribute);
			children = new ArrayList<HoeffdingNode<T>>();
			for (Object compareValue : dataCube.getClassCountLayer(
					splitAttribute).keySet()) {
				children.add(createLeaf(compareValue));
			}
			dataCube = null;
			classCountVector = new HashMap<Object, Integer>();
			setLeaf(false);
			return true;
		}
	}

	@Override
	public HoeffdingNode<T> clone() {
		return new HoeffdingNode<T>(this);
	}

	public AbstractAttributeEvaluationMeasure<T> getAttributeEvaluationMeasure() {
		return attributeEvaluationMeasure;
	}

	public List<HoeffdingNode<T>> getChildren() {
		return children;
	}

	@Override
	public Object getClassLabel(RelationalClassificationObject<T> tuple) {
		if (leaf) {
			return majorityClass;
		} else {
			HoeffdingNode<T> path = getChild(tuple, false);

			return path == null ? majorityClass : path.getClassLabel(tuple);
		}
	}

	public DataCube<T> getDataCube() {
		return dataCube;
	}

	private HoeffdingNode<T> getInnerNodeSnapshot() {
		HoeffdingNode<T> node = new HoeffdingNode<T>();
		node.setLeaf(false);
		node.setTestAttribute(getTestAttribute());
		node.setMajorityClass(getMajorityClass());
		List<HoeffdingNode<T>> nodeChildren = new ArrayList<HoeffdingNode<T>>();
		for (HoeffdingNode<T> child : children) {
			nodeChildren.add(child.getSnapshot());
		}
		node.setChildren(nodeChildren);
		return node;
	}

	private HoeffdingNode<T> getLeafSnapshot() {
		HoeffdingNode<T> node = new HoeffdingNode<T>();
		node.setLeaf(true);
		node.setTestValue(getTestValue());
		node.setMajorityClass(getMajorityClass());
		return node;
	}

	public Object getMajorityClass() {
		return majorityClass;
	}

	public HoeffdingNode<T> getSnapshot() {
		if (leaf) {
			return getLeafSnapshot();
		} else {
			return getInnerNodeSnapshot();
		}
	}

	private HashMap<Object, Integer> classCountVector;

	public List<Integer> getSplitAttributes() {
		return splitAttributes;
	}

	public int getTestAttribute() {
		return testAttribute;
	}

	public Object getTestValue() {
		return testValue;
	}

	public void initDataCube(int length) {
		dataCube = new DataCube<T>(length);
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setAttributeEvaluationMeasure(
			AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;
	}

	public void setChildren(List<HoeffdingNode<T>> children) {
		this.children = children;
	}

	public void setDataCube(DataCube<T> dataCube) {
		this.dataCube = dataCube;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public void setMajorityClass(Object majorityClass) {
		this.majorityClass = majorityClass;
	}

	public void setSplitAttributes(List<Integer> splitAttributes) {
		this.splitAttributes = splitAttributes;
	}

	public void setTestAttribute(int testAttribute) {
		this.testAttribute = testAttribute;
	}

	public void setTestValue(Object testValue) {
		this.testValue = testValue;
	}


}
