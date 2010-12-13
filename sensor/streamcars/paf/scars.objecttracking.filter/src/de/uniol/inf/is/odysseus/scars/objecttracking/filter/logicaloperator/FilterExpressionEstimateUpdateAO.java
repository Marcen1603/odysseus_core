package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterExpressionEstimateUpdateAO <M extends IProbability & IConnectionContainer & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	private IStreamCarsExpression[] expressions;
	
	public FilterExpressionEstimateUpdateAO()
	{
		super();
	}
	
	public FilterExpressionEstimateUpdateAO(FilterExpressionEstimateUpdateAO<M> copy) {
		super(copy);
		this.setOldObjListPath(copy.getOldObjListPath());
		this.setNewObjListPath(copy.getNewObjListPath());
		this.setExpressions(copy.getExpressions());
	}

	
	@Override
	public AbstractLogicalOperator clone() {
		return new FilterExpressionEstimateUpdateAO<M>(this);
	}
	
	  @Override
	  public SDFAttributeList getOutputSchema() {
		
		  return this.getInputSchema();
	  }
	
	
	// Getter & Setter

	public String getOldObjListPath() {
		return oldObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}
	
	public String getNewObjListPath() {
		return newObjListPath;
	}

	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	/**
	 * @param expressionString the expressionString to set
	 */
	public void setExpressions(IStreamCarsExpression[] expressions) {
		this.expressions = expressions;
	}

	/**
	 * @return the expressionString
	 */
	public IStreamCarsExpression[] getExpressions() {
		return expressions;
	}

}

