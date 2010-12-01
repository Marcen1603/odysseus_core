package de.uniol.inf.is.odysseus.datamining.classification.physicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.datamining.classification.AbstractAttributeEvaluationMeasure;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class HoeffdingTreePO<T extends IMetaAttribute> extends
		AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> {

	private int[] restrictList;
	private int labelPosition;
	private AbstractAttributeEvaluationMeasure attributeEvaluationMeasure;
	private Double probability;

	public HoeffdingTreePO(HoeffdingTreePO<T> hoeffdingTreePO) {
		super(hoeffdingTreePO);
		labelPosition = hoeffdingTreePO.labelPosition;
		restrictList = Arrays.copyOf(hoeffdingTreePO.restrictList,
				hoeffdingTreePO.restrictList.length);
		probability = hoeffdingTreePO.probability;
		attributeEvaluationMeasure = hoeffdingTreePO.attributeEvaluationMeasure;
	}

	public HoeffdingTreePO() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public HoeffdingTreePO<T> clone() {
		// TODO Auto-generated method stub
		return new HoeffdingTreePO<T>(this);
	}

	public void setRestrictList(int[] restrictList) {
		this.restrictList = restrictList;

	}

	public void setLabelPosition(int labelPosition) {
		this.labelPosition = labelPosition;
	}

	public void setAttributeEvaluationMeasure(
			AbstractAttributeEvaluationMeasure attributeEvaluationMeasure) {
		this.attributeEvaluationMeasure = attributeEvaluationMeasure;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

}
