package de.uniol.inf.is.odysseus.scars.objecttracking.temporarydatabouncer.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TemporaryDataBouncerAO<M extends IProbability> extends AbstractLogicalOperator {

	private static final long serialVersionUID = 1L;

	private String objListPath;
	private double threshold;

	private String operator;

	public TemporaryDataBouncerAO() {
		super();
	}

	public TemporaryDataBouncerAO(TemporaryDataBouncerAO<M> copy) {
		super(copy);
		this.objListPath = copy.objListPath;
		this.threshold = copy.threshold;
		this.operator = copy.operator;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema(0);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TemporaryDataBouncerAO<M>(this);
	}

	public String getObjListPath() {
		return objListPath;
	}

	public void setObjListPath(String objListPath) {
		this.objListPath = objListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
