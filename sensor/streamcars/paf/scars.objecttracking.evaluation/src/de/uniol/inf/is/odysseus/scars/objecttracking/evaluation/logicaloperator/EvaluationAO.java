package de.uniol.inf.is.odysseus.scars.objecttracking.evaluation.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class EvaluationAO<M extends IProbability> extends AbstractLogicalOperator{

	private static final long serialVersionUID = 7650711998042333164L;

	private String associationObjListPath;
	private String filteringObjListPath;
	private String brokerObjListPath;

	private double threshold;

	public EvaluationAO() {
		super();
	}

	public EvaluationAO(EvaluationAO<M> copy) {
		super(copy);
		this.associationObjListPath = copy.associationObjListPath;
		this.filteringObjListPath = copy.filteringObjListPath;
		this.brokerObjListPath = copy.brokerObjListPath;
		this.threshold = copy.getThreshold();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return getInputSchema(2);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new EvaluationAO<M>(this);
	}

	public void initPaths(String associationObjListPath, String filteringObjListPaths, String brokerObjListPath) {
		this.associationObjListPath = associationObjListPath;
		this.filteringObjListPath = filteringObjListPaths;
		this.brokerObjListPath = brokerObjListPath;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getAssociationObjListPath() {
		return associationObjListPath;
	}

	public String getFilteringObjListPath() {
		return filteringObjListPath;
	}

	public String getBrokerObjListPath() {
		return brokerObjListPath;
	}

}
