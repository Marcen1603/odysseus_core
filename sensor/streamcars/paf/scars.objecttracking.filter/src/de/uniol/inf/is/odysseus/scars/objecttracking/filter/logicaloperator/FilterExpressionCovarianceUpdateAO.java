package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;


import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractMetaDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateCovarianceFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterExpressionCovarianceUpdateAO <M extends IProbability & IGain & IConnectionContainer> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum<Parameters>, Object> parameters;
	
	private String[][] expressionString;
	
	public FilterExpressionCovarianceUpdateAO()
	{
		super();
		parameters = new HashMap<Enum<Parameters>, Object>();

	}
	
	public FilterExpressionCovarianceUpdateAO(FilterExpressionCovarianceUpdateAO<M> copy) {
		super(copy);
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));	
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

	
	// Getter & Setter

	public HashMap<Enum<Parameters>, Object> getParameters() {
		return parameters;
	}
	
	public void setParameters(HashMap<Enum<Parameters>, Object> parameters) {
		this.parameters = parameters;
	}


	public void setExpressionString(String[][] expressionString) {
		this.expressionString = expressionString;
	}

	public String[][] getExpressionString() {
		return expressionString;
	}
}

