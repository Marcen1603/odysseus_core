package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.datamining.classification.AbstractAttributeEvaluationMeasure;
import de.uniol.inf.is.odysseus.datamining.classification.HoeffdingNode;
import de.uniol.inf.is.odysseus.datamining.classification.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

/**
 * This class represents a physical hoeffding tree operator. This Operator is
 * used to learn hoeffding trees from a data stream
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class HoeffdingTreePO<T extends IMetaAttribute> extends
		AbstractClassificationPO<T> {

	/**
	 * the hoeffding tree
	 */
	private HoeffdingNode<T> tree;

	/**
	 * the attribute evaluation measure to be used to split the tree
	 */
	private AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure;

	/**
	 * set the evaluation measure to be used to split the tree
	 * 
	 * @param attributeEvaluationMeasure
	 *            the evaluation measure to set
	 */
	public void setAttributeEvaluationMeasure(
			AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;
	}

	/**
	 * create a new physical hoefding tree operator as a copy of another
	 * hoeffding tree operator
	 * 
	 * @param hoeffdingTreePO
	 *            the operator to copy
	 */
	public HoeffdingTreePO(HoeffdingTreePO<T> hoeffdingTreePO) {
		super(hoeffdingTreePO);
		tree = hoeffdingTreePO.tree.clone();
		attributeEvaluationMeasure = hoeffdingTreePO.attributeEvaluationMeasure;
	}

	/**
	 * create a new physical hoeffding tree operator
	 */
	public HoeffdingTreePO() {
		super();
	}

	/**
	 * initialize the hoeffding tree
	 */
	public void initTree() {
		tree = new HoeffdingNode<T>();
		tree.setLeaf(true);
		tree.initDataCube(restrictList.length);
		tree.setAttributeEvaluationMeasure(attributeEvaluationMeasure);
		ArrayList<Integer> splitAttributes = new ArrayList<Integer>();
		for (int i = 0; i < restrictList.length; i++) {
			splitAttributes.add(i);
		}
		tree.setSplitAttributes(splitAttributes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public HoeffdingTreePO<T> clone() {
		return new HoeffdingTreePO<T>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.datamining.classification.physicaloperator.
	 * AbstractClassificationPO
	 * #processNext(de.uniol.inf.is.odysseus.datamining.classification
	 * .RelationalClassificationObject)
	 */
	@Override
	protected void processNext(RelationalClassificationObject<T> tuple) {
		boolean changed = tree.addTuple(tuple);
		if (changed) {
			transferClassifier(tree.getSnapshot());

		}
	}

}
