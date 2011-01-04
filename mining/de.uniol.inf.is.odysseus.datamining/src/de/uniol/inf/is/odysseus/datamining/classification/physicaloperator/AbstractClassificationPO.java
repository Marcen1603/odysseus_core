package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.classification.AbstractAttributeEvaluationMeasure;
import de.uniol.inf.is.odysseus.datamining.classification.IClassifier;
import de.uniol.inf.is.odysseus.datamining.classification.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public abstract class AbstractClassificationPO<T extends IMetaAttribute>
		extends AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	protected int[] restrictList;
	protected int labelPosition;
	protected AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure;

	public void setAttributeEvaluationMeasure(
			AbstractAttributeEvaluationMeasure<T> attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;
	}

	public AbstractClassificationPO() {
super();
	}

	public AbstractClassificationPO(
			AbstractClassificationPO<T> classifierLearner) {
		super(classifierLearner);
		labelPosition = classifierLearner.labelPosition;
		restrictList = Arrays.copyOf(classifierLearner.restrictList,
				classifierLearner.restrictList.length);
		attributeEvaluationMeasure = classifierLearner.attributeEvaluationMeasure;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp, port);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		Object label = object.getAttribute(labelPosition);
		if (label != null) {
			RelationalClassificationObject<T> tuple = new RelationalClassificationObject<T>(
					object, restrictList, labelPosition);
			tuple.setClassLabel(label);
			process_next(tuple);
		}
	}

	protected void transferClassifier(IClassifier<T> classifier) {
		transfer(new RelationalTuple<T>(new Object[] { classifier }));
	}

	protected abstract void process_next(
			RelationalClassificationObject<T> tuple);

	public void setRestrictList(int[] restrictList) {
		this.restrictList = restrictList;

	}

	public void setLabelPosition(int labelPosition) {
		this.labelPosition = labelPosition;
	}

	

}
