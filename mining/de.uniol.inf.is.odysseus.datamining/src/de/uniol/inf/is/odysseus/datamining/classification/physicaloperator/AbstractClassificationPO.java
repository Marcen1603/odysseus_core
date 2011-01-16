package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.classification.IClassifier;
import de.uniol.inf.is.odysseus.datamining.classification.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class represents an abstract physical operator used for classification
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public abstract class AbstractClassificationPO<T extends IMetaAttribute>
		extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	/**
	 * the positions of the classification attributes in a tuple
	 */
	protected int[] restrictList;

	/**
	 * the position of the class in a tuple
	 */
	protected int labelPosition;

	/**
	 * create a new physical operattor
	 */
	public AbstractClassificationPO() {
		super();
	}

	/**
	 * create a new physical classification operator as a copy of another
	 * classification operator
	 * 
	 * @param classificationPO
	 *            the operator to copy
	 */
	public AbstractClassificationPO(AbstractClassificationPO<T> classificationPO) {
		super(classificationPO);
		labelPosition = classificationPO.labelPosition;
		restrictList = Arrays.copyOf(classificationPO.restrictList,
				classificationPO.restrictList.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.ISink#processPunctuation(de
	 * .uniol.inf.is.odysseus.metadata.PointInTime, int)
	 */
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#getOutputMode()
	 */
	@Override
	public OutputMode getOutputMode() {
		// this operator creates new elements
		return OutputMode.NEW_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe#process_next(java
	 * .lang.Object, int)
	 */
	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		Object label = object.getAttribute(labelPosition);
		if (label != null) {
			// if the tuple has a class label wrap it into classification object
			RelationalClassificationObject<T> tuple = new RelationalClassificationObject<T>(
					object, restrictList, labelPosition);
			tuple.setClassLabel(label);

			// process the tuple
			processNext(tuple);
		}
	}

	/**
	 * transfer a classifier
	 * 
	 * @param classifier
	 *            the classifier to transfer
	 */
	protected void transferClassifier(IClassifier<T> classifier) {
		transfer(new RelationalTuple<T>(new Object[] { classifier }));
	}

	/**
	 * process a classification object, to be implemented by physical operators
	 * 
	 * @param tuple
	 */
	protected abstract void processNext(RelationalClassificationObject<T> tuple);

	/**
	 * set the positions of the classification attributes in a tuple
	 * 
	 * @param restrictList
	 *            the positions to set
	 */
	public void setRestrictList(int[] restrictList) {
		this.restrictList = restrictList;

	}

	/**
	 * set the position of the class in a tuple
	 * 
	 * @param labelPosition
	 *            the position to set
	 */
	public void setLabelPosition(int labelPosition) {
		this.labelPosition = labelPosition;
	}

}
