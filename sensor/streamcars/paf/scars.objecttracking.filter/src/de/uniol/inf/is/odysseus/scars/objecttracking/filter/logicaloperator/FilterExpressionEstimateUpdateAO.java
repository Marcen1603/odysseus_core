package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.AbstractDataUpdateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.KalmanCorrectStateEstimateFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterExpressionEstimateUpdateAO <M extends IProbability & IConnectionContainer & IGain> extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	
	// path to new and old objects
	private String oldObjListPath;
	private String newObjListPath;
	
	private String[][] expressionString;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Enum<Parameters>, Object> parameters;

	private AbstractDataUpdateFunction<M>  dataUpdateFunction;
	
	
	public FilterExpressionEstimateUpdateAO()
	{
		super();
		parameters = new HashMap<Enum<Parameters>, Object>();
	}
	
	public FilterExpressionEstimateUpdateAO(FilterExpressionEstimateUpdateAO<M> copy) {
		super(copy);
		this.setOldObjListPath(copy.getOldObjListPath());
		this.setNewObjListPath(copy.getNewObjListPath());
		this.setParameters(new HashMap<Enum<Parameters>, Object>(copy.getParameters()));	
		this.setExpressionString(copy.getExpressionString());
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

