package de.uniol.inf.is.odysseus.scars.objecttracking.filter.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FilterGainAO <M extends IProbability> extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private String functionID;
	
	// Optional parameters for the Filter function. Not used right now.
	private HashMap<Integer, Object> parameters;
	
	// schemas
	private SDFAttributeList leftSchema;
	private SDFAttributeList rightSchema;
	
	public FilterGainAO()
	{
    super();
  }
	
	public FilterGainAO(FilterGainAO<M> copy) {
		super(copy);
		this.leftSchema = copy.getLeftSchema().clone();
		this.rightSchema = copy.getRightSchema().clone();
	}


	@Override
	public AbstractLogicalOperator clone() {
		return new FilterGainAO<M>(this);
	}
	
	public SDFAttributeList getLeftSchema() {
		return leftSchema;
	}

	public void setLeftSchema(SDFAttributeList leftSchema) {
		this.leftSchema = leftSchema;
	}

	public SDFAttributeList getRightSchema() {
		return rightSchema;
	}

	public void setRightSchema(SDFAttributeList rightSchema) {
		this.rightSchema = rightSchema;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getInputSchema();
	}

	/**
	 * @param functionID the functionID to set
	 */
	public void setFunctionID(String functionID) {
		this.functionID = functionID;
	}

	/**
	 * @return the functionID
	 */
	public String getFunctionID() {
		return functionID;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<Integer, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public HashMap<Integer, Object> getParameters() {
		return parameters;
	}
}

