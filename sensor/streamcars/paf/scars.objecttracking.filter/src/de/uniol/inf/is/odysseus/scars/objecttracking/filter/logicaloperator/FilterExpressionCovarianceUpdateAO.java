package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterExpressionCovarianceUpdateAO <M extends IProbability & IGain & IConnectionContainer> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;

	private String expressionString;
	private String scannedListPath;
	private String predictedListPath;
	
	public FilterExpressionCovarianceUpdateAO() {
		super();

	}
	
	public FilterExpressionCovarianceUpdateAO(FilterExpressionCovarianceUpdateAO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionCovarianceUpdateAO<M>(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	/**
	 * @param expressionString the expressionString to set
	 */
	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	/**
	 * @return the expressionString
	 */
	public String getExpressionString() {
		return expressionString;
	}
	
	public String getScannedListPath() {
		return scannedListPath;
	}

	public void setScannedListPath(String scannedListPath) {
		this.scannedListPath = scannedListPath;
	}

	public String getPredictedListPath() {
		return predictedListPath;
	}

	public void setPredictedListPath(String predictedListPath) {
		this.predictedListPath = predictedListPath;
	}
}

