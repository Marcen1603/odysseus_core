package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterExpressionGainAO <M extends IProbability & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	private String expressionString;
	private String scannedListPath;
	private String predictedListPath;
	private String[] restrictedVariables;

	public FilterExpressionGainAO() {
		super();
	}
	
	public FilterExpressionGainAO(FilterExpressionGainAO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
		this.setRestrictedVariables(copy.restrictedVariables.clone());
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionGainAO<M>(this);
	}
		
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	// Getter & Setter
	
	public void setRestrictedVariables(String[] restrictedVariables) {
		this.restrictedVariables = restrictedVariables;
	}
	
	public String[] getRestrictedVariables() {
		return restrictedVariables;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

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

