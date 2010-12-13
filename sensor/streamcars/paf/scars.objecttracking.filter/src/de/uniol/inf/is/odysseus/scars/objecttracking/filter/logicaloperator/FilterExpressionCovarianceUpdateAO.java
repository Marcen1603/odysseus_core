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
	private String[] restrictedVariables;
	
	public FilterExpressionCovarianceUpdateAO()
	{
		super();

	}
	
	public FilterExpressionCovarianceUpdateAO(FilterExpressionCovarianceUpdateAO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
		this.setRestrictedVariables(copy.getRestrictedVariables());
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
	 * @param restrictedVariables the restrictedVariables to set
	 */
	public void setRestrictedVariables(String[] restrictedVariables) {
		this.restrictedVariables = restrictedVariables;
	}

	/**
	 * @return the restrictedVariables
	 */
	public String[] getRestrictedVariables() {
		return restrictedVariables;
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
}

